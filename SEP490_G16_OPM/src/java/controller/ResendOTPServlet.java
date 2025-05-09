package controller;

import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "ResendOTPServlet", urlPatterns = {"/resend-otp"})
public class ResendOTPServlet extends HttpServlet {

    private static final long RESEND_COOLDOWN = 30 * 1000; // 30s cooldown
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 phút

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User tempUser = (User) session.getAttribute("tempUser");

        if (tempUser == null) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        Long lastResendTime = (Long) session.getAttribute("lastResendTime");
        long currentTime = System.currentTimeMillis();

        if (lastResendTime != null && currentTime - lastResendTime < RESEND_COOLDOWN) {
            long resendCooldownLeft = (RESEND_COOLDOWN - (currentTime - lastResendTime)) / 1000;
            request.setAttribute("msg", "Please wait " + resendCooldownLeft + " seconds before resending OTP.");
            request.setAttribute("otpRemainingSeconds", getOtpRemainingSeconds(session, currentTime));
            request.setAttribute("resendCooldownLeft", resendCooldownLeft);
            request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
            return;
        }

        // ✅ Gửi OTP mới
        String newOtp = String.format("%06d", new Random().nextInt(1000000));
        try {
            String subject = "Mã OTP mới";
            String content = "Mã OTP mới của bạn là: " + newOtp + ". OTP vẫn hết hạn sau 5 phút từ lúc gửi lần đầu.";
            model.Email.sendEmail(tempUser.getEmail(), subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Update lại OTP mới, nhưng KHÔNG reset otpTime
        session.setAttribute("otp", newOtp);
        session.setAttribute("lastResendTime", currentTime);

        // 🔥 Cập nhật thời gian còn lại đúng
        request.setAttribute("msg", "OTP has been resent. Please check your email.");
        request.setAttribute("otpRemainingSeconds", getOtpRemainingSeconds(session, currentTime));
        request.setAttribute("resendCooldownLeft", 30);
        request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
    }

    private long getOtpRemainingSeconds(HttpSession session, long currentTime) {
        Long otpTime = (Long) session.getAttribute("otpTime");
        if (otpTime == null) return 0;
        long elapsed = currentTime - otpTime;
        long remaining = (OTP_VALID_DURATION - elapsed) / 1000;
        return Math.max(remaining, 0);
    }
}
