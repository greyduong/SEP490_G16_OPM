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
                throw new AppException("Tất cả các trường không thể trống!");
            }
            if (role < 1 || role > 5) {
                throw new AppException("Loại tài khoản không tồn tại");
            }
            if (!username.matches("^[a-zA-Z0-9]{4,100}$")) {
                throw new AppException("Tên đăng nhập chỉ được sử dụng các ký tự a-z A-Z 0-9, độ dài trong khoảng 4-100 ký tự");
            }
            if (!password.equals(confirmPassword)) {
                throw new AppException("Mật khẩu và xác nhận mật khẩu không khớp!");
            }
            if (!fullname.matches("^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐa-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐa-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*$")) {
                throw new AppException("Tên đầy đủ chỉ được sử dụng các ký tự trong bảng chữ cái và dấu cách!");
            }
            if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
                throw new AppException("Lỗi định dạng email!");
            }
            UserDAO db = new UserDAO();
            if (db.checkExistsUsername(username)) {
                throw new AppException("Tên đăng nhập đã tồn tại!");
            }
            if (db.count("SELECT COUNT(*) FROM UserAccount WHERE Email = ?", email) > 0) {
                throw new AppException("Email đã tồn tại!");
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
                throw new AppException("Lỗi hệ thống. Liên hệ admin để biết thêm thông tin.");
            }
            
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
