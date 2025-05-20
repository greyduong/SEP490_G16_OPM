package controller;

import com.google.gson.Gson;
import dal.DBContext;
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
        PigsOfferDAO offerDAO = new PigsOfferDAO();
        OrderDAO orderDAO = new OrderDAO();
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

        int availableOffers = offerDAO.countAvailableOffersBySeller(sellerId);

        int totalOrders = db.fetchOne(
                rs -> rs.getInt(1),
                "SELECT COUNT(*) FROM Orders WHERE SellerID = ? AND CAST(CreatedAt AS DATE) <= ? AND CAST(CreatedAt AS DATE) >= ?",
                user.getUserID(),
                Date.valueOf(to),
                Date.valueOf(from)
        );

        int totalOffers = db.fetchOne(
                rs -> rs.getInt(1),
                "SELECT COUNT(*) FROM PigsOffer WHERE SellerID = ? AND CAST(CreatedAt AS DATE) <= ? AND CAST(CreatedAt AS DATE) >= ?",
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
                WHERE po.SellerID = ? AND CAST(d.CreatedAt AS DATE) <= ? AND CAST(d.CreatedAt AS DATE) >= ?
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

        var db = new DBContext();

        var orderChart = getOrderChart(db, user, from, to);
        var offerChart = getOfferChart(db, user, from, to);
        var farmChart = getFarmChart(db, user);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var out = resp.getWriter();
        var charts = List.of(orderChart, offerChart, farmChart);
        out.print(new Gson().toJson(charts));
        out.flush();
    }

    public record Stats(String label, int total) {

    }

    public Map<String, Object> getFarmChart(DBContext db, User user) {
        int totalFarms = db.fetchOne(rs -> rs.getInt(1), "SELECT COUNT(*) FROM Farm WHERE SellerID = ?", user.getUserID());
        int totalActiveFarms = db.fetchOne(rs -> rs.getInt(1), "SELECT COUNT(*) FROM Farm WHERE SellerID = ? AND Status = 'Active'", user.getUserID());
        int totalInactiveFarms = totalFarms - totalActiveFarms;
        return Map.of(
                "name", "farmChart",
                "labels", List.of(
                        "Hoạt động",
                        "Không hoạt động"
                ),
                "datasets", List.of(Map.of(
                        "label", "Trang trại",
                        "data", List.of(
                                totalActiveFarms,
                                totalInactiveFarms
                        )
                )),
                "type", "pie"
        );
    }

    public List<Stats> getOrderStatByStatus(DBContext db, User user, LocalDate from, LocalDate to, String status) {
        return db.fetchAll(
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
                    Orders o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.SellerID = ? AND o.Status = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                user.getUserID(),
                status
        );
    }

    public List<Stats> getOfferStatByStatus(DBContext db, User user, LocalDate from, LocalDate to, String status) {
        return db.fetchAll(
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
                    PigsOffer o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.SellerID = ? AND Status = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                user.getUserID(),
                status
        );
    }

    public Map<String, Object> getOfferChart(DBContext db, User user, LocalDate from, LocalDate to) {
        var available = getOfferStatByStatus(db, user, from, to, "Available");
        var unavailable = getOfferStatByStatus(db, user, from, to, "Unavailable");
        var labels = available.stream().map(e -> e.label()).toList();
        var availableData = available.stream().map(e -> e.total()).toList();
        var unavailableData = unavailable.stream().map(e -> e.total()).toList();
        var datasets = List.of(
                Map.of("data", availableData, "label", "Đã kiểm định"),
                Map.of("data", unavailableData, "label", "Hết hạn")
        );
        return Map.of("name", "offersChart", "labels", labels, "datasets", datasets);
    }

    public Map<String, Object> getOrderChart(DBContext db, User user, LocalDate from, LocalDate to) {
        var pending = getOrderStatByStatus(db, user, from, to, "Pending");
        var canceled = getOrderStatByStatus(db, user, from, to, "Canceled");
        var confirmed = getOrderStatByStatus(db, user, from, to, "Confirmed");
        var processing = getOrderStatByStatus(db, user, from, to, "Processing");
        List<String> labels = pending.stream().map(e -> e.label()).toList();
        List<Integer> pendingData = pending.stream().map(e -> e.total()).toList();
        List<Integer> canceledData = canceled.stream().map(e -> e.total()).toList();
        List<Integer> confirmedData = confirmed.stream().map(e -> e.total()).toList();
        List<Integer> processingData = processing.stream().map(e -> e.total()).toList();
        List<Map<String, Object>> datasets = List.of(
                Map.of("label", "Chờ xử lý", "data", pendingData),
                Map.of("label", "Đã hủy", "data", canceledData),
                Map.of("label", "Đã xác nhận", "data", confirmedData),
                Map.of("label", "Đang xử lý", "data", processingData)
        );
        return Map.of("labels", labels, "datasets", datasets, "name", "orderChart");
    }
}
