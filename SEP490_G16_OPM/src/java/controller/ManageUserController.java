package controller;

import dal.DBContext;
import dal.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import model.User;

@WebServlet("/manage-user")
public class ManageUserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = getParam(req, "search").orElse("");
        int page = getIntParam(req, "page").map(p -> p < 1 ? 1 : p).orElse(1);
        int size = 5;
        int offset = (page - 1) * size;
        String pattern = "%" + search + "%";
        int total = new DBContext().count("SELECT COUNT(*) FROM UserAccount WHERE Username LIKE ?", pattern);
        if (total <= offset) {
            offset = 0;
            page = 1;
        }
        List<User> users = new DBContext().fetchAll(UserMapper.toUser(), "SELECT * FROM UserAccount WHERE Username LIKE ? ORDER BY UserID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                pattern,
                offset,
                size);

        req.setAttribute("users", users);
        req.setAttribute("offset", offset);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("search", search);
        req.getRequestDispatcher("manage-user.jsp").forward(req, resp);
    }

    private Optional<String> getParam(HttpServletRequest req, String key) {
        return Optional.ofNullable(req.getParameter(key)).map(val -> val.isBlank() ? null : val);
    }

    private Optional<Integer> getIntParam(HttpServletRequest req, String key) {
        try {
            return Optional.ofNullable(req.getParameter(key)).map(Integer::parseInt);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getParam(req, "action").orElse("");
        switch (action) {
            case "delete" ->
                delete(req, resp);
            case "edit" ->
                edit(req, resp);
            case "recover" ->
                recover(req, resp);
            case "add" ->
                add(req, resp);
            default ->
                req.getRequestDispatcher("manage-user.jsp").forward(req, resp);
        }
    }
    
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("manage-user.jsp").forward(req, resp);
    }
    
    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("manage-user.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Delete user");
        try {
            int userID = getIntParam(req, "id").orElseThrow();
            DBContext db = new DBContext();
            db.update("UPDATE UserAccount SET Status = ? WHERE UserID = ?", "Inactive", userID);
        } catch (NoSuchElementException e) {
            req.setAttribute("error", "Invalid user");
        }
        resp.sendRedirect(req.getContextPath() + "/manage-user");
    }
    
    private void recover(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("recoverUser");
        try {
            int userID = getIntParam(req, "id").orElseThrow();
            DBContext db = new DBContext();
            db.update("UPDATE UserAccount SET Status = ? WHERE UserID = ?", "Active", userID);
        } catch (NoSuchElementException e) {
            req.setAttribute("error", "Invalid user");
        }
        resp.sendRedirect(req.getContextPath() + "/manage-user");
    }
}
