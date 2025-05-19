package controller;

import com.google.gson.Gson;
import dal.DBContext;
import dao.DeliveryDAO;
import dao.FarmDAO;
import dao.OrderDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import model.OrderStat;
import model.User;

@WebServlet("/seller")
public class SellerHomeController extends HttpServlet {

    private LocalDate getDate(HttpServletRequest request, String param) {
        if (request.getParameter(param) == null || request.getParameter(param).isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(request.getParameter(param));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        FarmDAO farmDAO = new FarmDAO();
        PigsOfferDAO offerDAO = new PigsOfferDAO();
        OrderDAO orderDAO = new OrderDAO();
        DeliveryDAO deliveryDAO = new DeliveryDAO();
        var db = new DBContext();

        var from = getDate(req, "from");
        var to = getDate(req, "to");

        if (from == null || to == null || from.isAfter(to)) {
            to = ZonedDateTime.now(ZoneId.of("UTC+7")).toLocalDate();
            from = to.minusDays(7);
        }

        req.setAttribute("from", from.format(DateTimeFormatter.ISO_LOCAL_DATE));
        req.setAttribute("to", to.format(DateTimeFormatter.ISO_LOCAL_DATE));

        int sellerId = user.getUserID();

        int totalFarms = farmDAO.countFarmsBySeller(sellerId);
        int activeFarms = farmDAO.countActiveFarmsBySeller(sellerId);
        int availableOffers = offerDAO.countAvailableOffersBySeller(sellerId);

        int totalOrders = db.fetchOne(
                rs -> rs.getInt(1),
                "SELECT COUNT(*) FROM Orders WHERE SellerID = ? AND CreatedAt <= ? AND CreatedAt >= ?",
                user.getUserID(),
                Date.valueOf(to),
                Date.valueOf(from)
        );

        int totalOffers = db.fetchOne(
                rs -> rs.getInt(1),
                "SELECT COUNT(*) FROM PigsOffer WHERE SellerID = ? AND CreatedAt <= ? AND CreatedAt >= ?",
                user.getUserID(),
                Date.valueOf(to),
                Date.valueOf(from)
        );

        double totalRevenue = db.fetchOne(
                rs -> rs.getDouble(1),
                """
                SELECT COALESCE(SUM(d.TotalPrice), 0)
                FROM Delivery d
                JOIN Orders o ON d.OrderID = o.OrderID
                JOIN PigsOffer po ON o.OfferID = po.OfferID
                WHERE po.SellerID = ? AND d.CreatedAt <= ? AND d.CreatedAt >= ?
                """,
                user.getUserID(),
                Date.valueOf(to),
                Date.valueOf(from)
        );
        double totalConfirmedOrDepositedOrders = orderDAO.calculateTotalConfirmedOrDepositedOrdersBySeller(sellerId);
        double totalDebt = totalConfirmedOrDepositedOrders - totalRevenue;

        // Get filter parameters
        String type = "month";
        Integer year = null, month = null, day = null;

        List<OrderStat> stats = orderDAO.getOrderStatsFlexible(type, year, month, day, sellerId);

        req.setAttribute("totalFarms", totalFarms);
        req.setAttribute("activeFarms", activeFarms);
        req.setAttribute("totalOffers", totalOffers);
        req.setAttribute("availableOffers", availableOffers);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalDebt", totalDebt);
        req.setAttribute("orderStats", stats);
        req.setAttribute("statType", type);

        req.getRequestDispatcher("seller.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        var from = getDate(req, "from");
        var to = getDate(req, "to");

        if (from == null || to == null || from.isAfter(to)) {
            to = ZonedDateTime.now(ZoneId.of("UTC+7")).toLocalDate();
            from = to.minusDays(7);
        }

        var orderStat = getOrderStat(user, from, to);
        var offerStat = getOfferStat(user, from, to);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var out = resp.getWriter();
        out.print(new Gson().toJson(Map.of("order", orderStat, "offer", offerStat)));
        out.flush();
    }

    public record Stats(String label, int total) {

    }

    public Map<String, Object> getOfferStat(User user, LocalDate from, LocalDate to) {
        var stats = new DBContext().fetchAll(
                rs -> {
                    String label = rs.getDate("Label").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int total = rs.getInt("Total");
                    return new Stats(label, total);
                },
                """
                WITH DateRange AS (
                    SELECT 
                        CAST(? AS DATE) AS Label
                    UNION ALL
                    SELECT 
                        DATEADD(DAY, 1, Label)
                    FROM 
                        DateRange
                    WHERE 
                        Label < CAST(? AS DATE)
                ) SELECT 
                    d.Label,
                    COUNT(o.OfferID) AS Total
                FROM 
                    DateRange d
                LEFT JOIN 
                    PigsOffer o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.SellerID = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                user.getUserID()
        );

        List<String> labels = stats.stream().map(e -> e.label()).toList();
        List<Integer> data = stats.stream().map(e -> e.total()).toList();
        return Map.of("labels", labels, "data", data);
    }

    public Map<String, Object> getOrderStat(User user, LocalDate from, LocalDate to) {
        var stats = new DBContext().fetchAll(
                rs -> {
                    String label = rs.getDate("Label").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int total = rs.getInt("Total");
                    return new Stats(label, total);
                },
                """
                WITH DateRange AS (
                    SELECT 
                        CAST(? AS DATE) AS Label
                    UNION ALL
                    SELECT 
                        DATEADD(DAY, 1, Label)
                    FROM 
                        DateRange
                    WHERE 
                        Label < CAST(? AS DATE)
                ) SELECT 
                    d.Label,
                    COUNT(o.OrderID) AS Total
                FROM 
                    DateRange d
                LEFT JOIN 
                    Orders o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.SellerID = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                user.getUserID()
        );

        List<String> labels = stats.stream().map(e -> e.label()).toList();
        List<Integer> data = stats.stream().map(e -> e.total()).toList();
        return Map.of("labels", labels, "data", data);
    }
}
