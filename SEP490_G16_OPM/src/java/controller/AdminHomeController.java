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

@WebServlet("/admin")
public class AdminHomeController extends HttpServlet {
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
        var db = new DBContext();
        req.setAttribute("totalUser", getTotalUser(db));
        req.getRequestDispatcher("admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var out = resp.getWriter();
        var db = new DBContext();
        var charts = List.of(getUserChart(db));
        out.print(new Gson().toJson(charts));
        out.flush();
    }

    public int getTotalUser(DBContext db) {
        return db.fetchOne(rs -> rs.getInt(1), "SELECT COUNT(*) FROM UserAccount");
    }

    public record Stats(String label, int total) {

    }

    public Map<String, Object> getUserChart(DBContext db) {
        var stats = db.fetchAll(
                rs -> {
                    String label = rs.getString("Label");
                    int total = rs.getInt("Total");
                    return new Stats(label, total);
                },
                """
                SELECT COUNT(UserID) AS 'Total', Status AS 'Label'
                FROM UserAccount
                GROUP BY Status
                """
        );
        var labels = stats.stream().map(e -> e.label()).toList();
        var data = stats.stream().map(e -> e.total()).toList();
        return Map.of(
                "name", "userChart",
                "labels", labels,
                "datasets", List.of(
                        Map.of(
                                "label", "Người dùng",
                                "data", data
                        )
                ),
                "type", "pie"
        );
    }
}
