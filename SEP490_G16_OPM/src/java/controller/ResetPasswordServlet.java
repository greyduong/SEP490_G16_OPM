package controller;

import dao.UserDAO;
import model.User;
import dao.Validation; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User tempUser = (User) session.getAttribute("tempUser");

        if (tempUser == null) {
            response.sendRedirect("login");
            return;
        }

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        
        if (Validation.isEmpty(password) || Validation.isEmpty(confirmPassword)) {
            request.setAttribute("msg", "Vui lòng nhập đầy đủ thông tin.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        if (!Validation.isValidPassword(password)) {
            request.setAttribute("msg", "Mật khẩu phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        if (!Validation.passwordsMatch(password, confirmPassword)) {
            request.setAttribute("msg", "Mật khẩu và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        // ✅ Cập nhật mật khẩu mới
        boolean updated = new UserDAO().updatePassword(tempUser.getUsername(), password);

        if (updated) {
            session.removeAttribute("tempUser");
            session.removeAttribute("otp");
            session.removeAttribute("otpTime");
            session.removeAttribute("lastResendTime");
            session.removeAttribute("otpPurpose");

            request.setAttribute("successMsg", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
            response.sendRedirect("login");
        } else {
            request.setAttribute("msg", "Đặt lại mật khẩu thất bại. Vui lòng thử lại.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }
}
