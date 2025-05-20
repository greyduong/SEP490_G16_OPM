package controller;

import com.google.gson.Gson;
import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import model.User;

@WebServlet("/dealer-dashboard")
public class DealerDashboardController extends HttpServlet {

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
        var from = getDate(req, "from");
        var to = getDate(req, "to");

        if (from == null || to == null || from.isAfter(to)) {
            to = ZonedDateTime.now(ZoneId.of("UTC+7")).toLocalDate();
            from = to.minusDays(7);
        }

        req.setAttribute("from", from.format(DateTimeFormatter.ISO_LOCAL_DATE));
        req.setAttribute("to", to.format(DateTimeFormatter.ISO_LOCAL_DATE));

        var db = new DBContext();

        long totalSpend = getTotalSpend(db, user, from, to);
        long totalOrders = getTotalOrders(db, user, from, to);
        long totalTopup = getTotalTopup(db, user, from, to);
        req.setAttribute("totalSpend", totalSpend);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalTopup", totalTopup);

        req.getRequestDispatcher("dealer-dashboard.jsp").forward(req, resp);
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

        req.setAttribute("from", from.format(DateTimeFormatter.ISO_LOCAL_DATE));
        req.setAttribute("to", to.format(DateTimeFormatter.ISO_LOCAL_DATE));

        var orderStat = getOrderStat(user, from, to);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var out = resp.getWriter();
        var charts = List.of(orderStat);
        out.print(new Gson().toJson(charts));
        out.flush();
    }

    public long getTotalSpend(DBContext db, User user, LocalDate from, LocalDate to) {
        return db.fetchOne(rs -> rs.getLong(1), "SELECT SUM(TotalPrice) FROM Orders WHERE DealerID = ? AND CAST(CreatedAT AS DATE) >= ? AND CAST(CreatedAT AS DATE) <= ?", user.getUserID(), Date.valueOf(from), Date.valueOf(to));
    }

    public long getTotalOrders(DBContext db, User user, LocalDate from, LocalDate to) {
        return db.fetchOne(rs -> rs.getLong(1), "SELECT COUNT(*) FROM Orders WHERE DealerID = ? AND CAST(CreatedAT AS DATE) >= ? AND CAST(CreatedAT AS DATE) <= ?", user.getUserID(), Date.valueOf(from), Date.valueOf(to));
    }

    public long getTotalTopup(DBContext db, User user, LocalDate from, LocalDate to) {
        return db.fetchOne(rs -> rs.getLong(1), "SELECT SUM(Amount) FROM WalletTopupHistory WHERE UserID = ? AND CAST(CreatedAT AS DATE) >= ? AND CAST(CreatedAT AS DATE) <= ? AND Status = 'Success'", user.getUserID(), Date.valueOf(from), Date.valueOf(to));
    }

    public record Stats(String label, int total) {

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
                    Orders o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.DealerID = ? AND o.Status = ?
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

    public Map<String, Object> getOrderStat(User user, LocalDate from, LocalDate to) {
        var db = new DBContext();

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
