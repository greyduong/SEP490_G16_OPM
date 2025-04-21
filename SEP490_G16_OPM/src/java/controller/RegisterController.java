package controller;

import dao.UserDAO;
import exeception.AppException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import model.User;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Optional.ofNullable(req.getParameter("username")).orElse("");
        String password = Optional.ofNullable(req.getParameter("password")).orElse("");
        String confirmPassword = Optional.ofNullable(req.getParameter("confirmPassword")).orElse("");
        String email = Optional.ofNullable(req.getParameter("email")).orElse("");
        String fullname = Optional.ofNullable(req.getParameter("fullname")).orElse("");
        int role = Optional.ofNullable(req.getParameter("role")).map(obj -> {
            try {
                return Integer.valueOf(obj);
            } catch (NumberFormatException e) {
                return 0;
            }
        }).orElse(0);
        
        req.setAttribute("username", username);
        req.setAttribute("password", password);
        req.setAttribute("email", email);
        req.setAttribute("confirmPassword", confirmPassword);
        req.setAttribute("role", role);
        req.setAttribute("fullname", fullname);
        req.setAttribute("error", null);

        try {
            if (role == 0 || username.isEmpty() || password.isEmpty() || email.isEmpty() || fullname.isEmpty() || confirmPassword.isEmpty()) {
                throw new AppException("Each fields cannot be empty!");
            }
            if (role < 1 || role > 5) {
                throw new AppException("Invalid role");
            }
            if (!username.matches("^[a-zA-Z0-9]{4,100}$")) {
                throw new AppException("Username must contain only a-z A-Z 0-9 character, length in range 4-100");
            }
            if (!password.equals(confirmPassword)) {
                throw new AppException("Password and confirm password dont match!");
            }
            if (!fullname.matches("^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐa-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐa-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*$")) {
                throw new AppException("Fullname only contains alphabet (with accent) and space!");
            }
            if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
                throw new AppException("Invalid email format!");
            }
            UserDAO db = new UserDAO();
            if (db.checkExistsUsername(username)) {
                throw new AppException("Username already existed!");
            }
            if (db.count("SELECT COUNT(*) FROM UserAccount WHERE Email = ?", email) > 0) {
                throw new AppException("Email already existed!");
            }
            
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullname);
            user.setPassword(password);
            user.setRoleID(role);
            user.setStatus("Active");
            user.setWallet(0);
            
            if (!db.addNewUser(user)) {
                throw new AppException("Error occur when register new user. Contact admin page.");
            }
            
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
