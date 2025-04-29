package controller;

import dao.UserDAO;
import model.User;
import model.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 phút

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        UserDAO userDao = new UserDAO();
        User user = userDao.getUserByEmail(email);

        if (user == null) {
            // ✅ Không tìm thấy user, báo lỗi
            request.setAttribute("error", "Email không tồn tại trong hệ thống.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        // ✅ Có user rồi, tiếp tục gửi OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));
        long otpTime = System.currentTimeMillis();

        boolean emailSent = false;
        try {
            String subject = "Mã OTP xác thực đặt lại mật khẩu";
            String content = "Xin chào " + user.getFullName() + ",\n\n" +
                             "Mã OTP để đặt lại mật khẩu của bạn là: " + otp +
                             "\nMã này có hiệu lực trong 5 phút.\n\nOnline Pig Market.";
            Email.sendEmail(email, subject, content);
            emailSent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!emailSent) {
            request.setAttribute("error", "Không thể gửi OTP. Vui lòng thử lại sau.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        // ✅ Lưu vào session
        HttpSession session = request.getSession();
        session.setAttribute("tempUser", user); // để sau verify OTP đổi mật khẩu
        session.setAttribute("otp", otp);
        session.setAttribute("otpTime", otpTime);
        session.setAttribute("otpPurpose", "forgot-password"); // phân biệt mục đích OTP

        // ✅ Forward tới trang nhập OTP
        request.setAttribute("otpRemainingSeconds", 300); // countdown 5 phút
        request.setAttribute("resendCooldownLeft", 0);    // chưa cooldown resend
        request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
    }
}
