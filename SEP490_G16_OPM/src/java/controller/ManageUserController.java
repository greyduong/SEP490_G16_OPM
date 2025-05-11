package controller;

import dal.DBContext;
import dao.UserDAO;
import dao.Validation;
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
        String action = getParam(req, "action").orElse("");
        switch (action) {
            case "add" ->
                showAddPage(req, resp);
            case "edit" ->
                showEditPage(req, resp);
            default ->
                showListUsers(req, resp);
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
                showListUsers(req, resp);
        }
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

    private void showAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> success = Optional.ofNullable(req.getAttribute("success")).map(obj -> (String) obj);
        if (success.isPresent()) {
            req.getRequestDispatcher("manage-user-add.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("manage-user-add.jsp").forward(req, resp);
    }

    private void showEditPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> success = Optional.ofNullable(req.getAttribute("success")).map(obj -> (String) obj);
        if (success.isPresent()) {
            req.getRequestDispatcher("manage-user-edit.jsp").forward(req, resp);
            return;
        }
        try {
            int id = getIntParam(req, "id").orElseThrow();
            User user = new UserDAO().findById(id).orElseThrow();
            req.setAttribute("fullname", user.getFullName());
            req.setAttribute("username", user.getUsername());
            req.setAttribute("id", id);
            req.setAttribute("email", user.getEmail());
            req.setAttribute("address", user.getAddress());
            req.setAttribute("phone", user.getPhone());
            req.setAttribute("password", user.getPassword());
            req.setAttribute("role", user.getRoleID());
            req.setAttribute("status", user.getStatus());
        } catch (NoSuchElementException e) {
            req.setAttribute("error", "Invalid user id!");
        }
        req.getRequestDispatcher("manage-user-edit.jsp").forward(req, resp);
    }

    private void showListUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = getParam(req, "search").orElse("");
        int page = getIntParam(req, "page").orElse(1);
        if (page < 1) page = 1;
        int size = 5;
        int offset = (page - 1) * size;
        UserDAO db = new UserDAO();
        int total = db.countSearch(search);
        if (total <= offset) {
            offset = 0;
            page = 1;
        }
        List<User> users = db.search(search, page, size);
        req.setAttribute("users", users);
        req.setAttribute("offset", offset);
        req.setAttribute("total", total);
        req.setAttribute("page", page);
        req.setAttribute("search", search);
        req.getRequestDispatcher("manage-user.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullname = getParam(req, "fullname").orElse("").trim();
        String username = getParam(req, "username").orElse("").trim();
        String email = getParam(req, "email").orElse("").trim();
        String password = getParam(req, "password").orElse("").trim();
        int role = getIntParam(req, "role").orElse(5);
        req.setAttribute("fullname", fullname);
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("password", password);
        req.setAttribute("role", role);
        req.setAttribute("error", null);
        var db = new UserDAO();
        try {
            if (fullname.isEmpty()) {
                throw new RuntimeException("Tên đầy đủ không được trống!");
            }
            if (username.isEmpty()) {
                throw new RuntimeException("Tên đăng nhập không được trống!");
            }
            if (username.matches("^[a-zA-Z0-9]$") && username.length() > 32 && username.length() < 4) {
                throw new RuntimeException("Tên đăng nhập chỉ có thể chứa các ký tự a-z, A-Z, 0-9, tối thiểu 4, tối đa 32 ký tự");
            }
            if (email.isEmpty()) {
                throw new RuntimeException("Email không được trống!");
            }
            if (!Validation.isValidEmail(email)) {
                throw new RuntimeException("Email không hợp lệ!");
            }
            if (password.isEmpty()) {
                throw new RuntimeException("Mật khẩu không được trống!");
            }
            if (password.length() < 4) {
                throw new RuntimeException("Mật khẩu tối thiểu 4 ký tự!");
            }
            if (db.checkExistsEmail(email)) {
                throw new RuntimeException("Email đã tồn tại!");
            }
            if (db.checkExistsUsername(username)) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
            }
            User user = new User();
            user.setFullName(fullname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRoleID(role);
            user.setStatus("Active");
            new UserDAO().addNewUser(user);
            req.setAttribute("success", "Thêm người dùng thành công!");
            showAddPage(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("manage-user-add.jsp").forward(req, resp);
        }
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id;
        User user;
        try {
            id = getIntParam(req, "id").orElseThrow();
            user = new UserDAO().findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            req.setAttribute("error", "Invalid user id");
            req.getRequestDispatcher("manage-user-edit.jsp").forward(req, resp);
            return;
        }
        String fullname = getParam(req, "fullname").orElse("").trim();
        String username = getParam(req, "username").orElse("").trim();
        String email = getParam(req, "email").orElse("").trim();
        String password = getParam(req, "password").orElse("").trim();
        String address = getParam(req, "address").orElse("").trim();
        String phone = getParam(req, "phone").orElse("").trim();
        String status = getParam(req, "status").orElse("").trim();
        if (!status.equals("Active") && !status.equals("Inactive")) {
            status = "Active";
        }
        int role = getIntParam(req, "role").orElse(5);
        req.setAttribute("fullname", fullname);
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("password", password);
        req.setAttribute("role", role);
        req.setAttribute("error", null);
        req.setAttribute("id", id);
        try {
            if (fullname.isEmpty()) {
                throw new RuntimeException("Fullname cannot empty!");
            }
            if (username.isEmpty()) {
                throw new RuntimeException("Username cannot empty!");
            }
            if (username.matches("^[a-zA-Z0-9]$") && username.length() > 32 && username.length() < 4) {
                throw new RuntimeException("Username only contains a-z, A-Z, 0-9, min 4, max 32 characters");
            }
            if (email.isEmpty()) {
                throw new RuntimeException("Email cannot empty!");
            }
            if (!Validation.isValidEmail(email)) {
                throw new RuntimeException("Invalid email format!");
            }
            if (password.isEmpty()) {
                throw new RuntimeException("Password cannot empty!");
            }
            if (password.length() < 4) {
                throw new RuntimeException("Password min length 4!");
            }
            if (!username.equals(user.getUsername()) && new UserDAO().checkExistsUsername(username)) {
                throw new RuntimeException("Username existed!");
            }
            if (!email.equals(user.getEmail()) && new DBContext().count("SELECT COUNT(*) FROM UserAccount WHERE Email = ?", email) > 0) {
                throw new RuntimeException("Email existed!");
            }
            user.setFullName(fullname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRoleID(role);
            user.setAddress(address);
            user.setPhone(phone);
            user.setStatus(status);
            new DBContext().update("UPDATE UserAccount SET Username = ?, Email = ?, Password = ?, RoleID = ?, Address = ?, Phone = ?, Status = ?, FullName = ? WHERE UserID = ?",
                    username, email, password, role, address, phone, status, fullname, id);
            req.setAttribute("success", "Edit user success!");
            showEditPage(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("manage-user-edit.jsp").forward(req, resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
