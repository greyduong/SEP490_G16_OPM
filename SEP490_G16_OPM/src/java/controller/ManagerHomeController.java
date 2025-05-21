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
import java.util.List;
import java.util.Map;

@WebServlet("/manager-home")
public class ManagerHomeController extends HttpServlet {
    public record Parameters(LocalDate from, LocalDate to) {}

    public LocalDate getDateParameter(HttpServletRequest req, String key) {
        try {
            return LocalDate.parse(req.getParameter(key));
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public Parameters getParameters(HttpServletRequest req) {
        LocalDate from = getDateParameter(req, "from");
        LocalDate to = getDateParameter(req, "to");
        if (from == null || to == null || from.isAfter(to)) {
            to = ZonedDateTime.now(ZoneId.of("UTC+7")).toLocalDate();
            from = to.minusDays(7);
        }
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        return new Parameters(from, to);
    }

    public record Stats(String labels, int total) {}

    public List<Stats> getFarmStatByStatus(DBContext db, LocalDate from, LocalDate to, String status) {
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
                    COUNT(f.FarmID) AS Total
                FROM 
                    DateRange d
                LEFT JOIN 
                    Farm f ON CAST(f.CreatedAt AS DATE) = d.Label AND f.Status = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                status
        );
    }

    public List<Stats> getOfferStatByStatus(DBContext db, LocalDate from, LocalDate to, String status) {
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
                    PigsOffer o ON CAST(o.CreatedAt AS DATE) = d.Label AND Status = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                status
        );
    }
    public List<Stats> getOrderStatByStatus(DBContext db, LocalDate from, LocalDate to, String status) {
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
                    Orders o ON CAST(o.CreatedAt AS DATE) = d.Label AND o.Status = ?
                GROUP BY 
                    d.Label
                ORDER BY 
                    d.Label
                """,
                from,
                to,
                status
        );
    }

    public Map<String, Object> getOrderChart(DBContext db, LocalDate from, LocalDate to) {
        var pending = getOrderStatByStatus(db, from, to, "Pending");
        var canceled = getOrderStatByStatus(db, from, to, "Canceled");
        var confirmed = getOrderStatByStatus(db, from, to, "Confirmed");
        var processing = getOrderStatByStatus(db, from, to, "Processing");
        List<String> labels = pending.stream().map(e -> e.labels()).toList();
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

    public Map<String, Object> getFarmChart(DBContext db, LocalDate from, LocalDate to) {
        var active = getFarmStatByStatus(db, from, to, "Active");
        var pending = getFarmStatByStatus(db, from, to, "Pending");
        var labels = active.stream().map(e -> e.labels()).toList();
        var datasets = List.of(
                Map.of("data", active.stream().map(e -> e.total()).toList(), "label", "Hoạt động"),
                Map.of("data", pending.stream().map(e -> e.total()).toList(), "label", "Chờ duyệt")
        );
        return Map.of("name", "farmChart", "labels", labels, "datasets", datasets);
    }

    public Map<String, Object> getOfferChart(DBContext db, LocalDate from, LocalDate to) {
        var available = getOfferStatByStatus(db, from, to, "Available");
        var unavailable = getOfferStatByStatus(db, from, to, "Unavailable");
        var labels = available.stream().map(e -> e.labels()).toList();
        var availableData = available.stream().map(e -> e.total()).toList();
        var unavailableData = unavailable.stream().map(e -> e.total()).toList();
        var datasets = List.of(
                Map.of("data", availableData, "label", "Đã kiểm định"),
                Map.of("data", unavailableData, "label", "Hết hạn")
        );
        return Map.of("name", "offerChart", "labels", labels, "datasets", datasets);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var params = getParameters(req);
        req.getRequestDispatcher("manager.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var params = getParameters(req);
        var db = new DBContext();
        var charts = List.of(
                getFarmChart(db, params.from(), params.to()),
                getOfferChart(db, params.from(), params.to()),
                getOrderChart(db, params.from(), params.to())
        );
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var out = resp.getWriter();
        out.print(new Gson().toJson(charts));
        out.flush();
    }
}
