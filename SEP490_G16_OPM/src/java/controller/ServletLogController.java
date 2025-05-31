package controller;

import dao.ServerLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@WebServlet("/server-log")
public class ServletLogController extends HttpServlet {

    public Params getAndSaveParams(HttpServletRequest req) {
        LocalDate from = null;
        LocalDate to = null;
        int page = 0;
        try {
            from = LocalDate.parse(req.getParameter("from"));
            to = LocalDate.parse(req.getParameter("to"));
        } catch (DateTimeParseException | NullPointerException e) {
        }
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException e) {
        }
        if (from == null || to == null || from.isAfter(to)) {
            to = ZonedDateTime.now(ZoneId.of("UTC+7")).toLocalDate();
            from = to.minusDays(7);
        }
        if (page <= 0) {
            page = 1;
        }
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        req.setAttribute("page", page);
        return new Params(from, to, page);
    }

    public record Params(LocalDate from, LocalDate to, int page) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var params = getAndSaveParams(req);
        var logs = new ServerLogDAO().getAllLogs(params.from(), params.to(), params.page(), 10);
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("server-log.jsp").forward(req, resp);
    }
}
