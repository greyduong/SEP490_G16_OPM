package controller;

import dao.UserDAO;
import dao.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

@WebServlet(name = "ChangePasswordController", urlPatterns = {"/change-password"})
public class ChangePasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmNewPassword");

        if (Validation.isEmpty(currentPassword) || Validation.isEmpty(newPassword) || Validation.isEmpty(confirmPassword)) {
            session.setAttribute("error", "Vui lòng nhập đầy đủ các trường.");
            response.sendRedirect("profile");
            return;
        }

        if (!user.getPassword().equals(currentPassword)) {
            session.setAttribute("error", "Mật khẩu hiện tại không đúng.");
            response.sendRedirect("profile");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "Mật khẩu mới không khớp.");
            response.sendRedirect("profile");
            return;
        }

        if (!Validation.isValidPassword(newPassword)) {
            session.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự, không có khoảng trắng.");
            response.sendRedirect("profile");
            return;
        }

        user.setPassword(newPassword);
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updatePassword(user.getUsername(), newPassword);

        if (success) {
            session.setAttribute("success", "Đổi mật khẩu thành công.");
        } else {
            session.setAttribute("error", "Đổi mật khẩu thất bại.");
        }

        response.sendRedirect("profile");
    }
}
