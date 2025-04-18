package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = {"/verify-otp"})
public class VerifyOTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String inputOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        Long otpTime = (Long) session.getAttribute("otpTime");
        User tempUser = (User) session.getAttribute("tempUser");

        long currentTime = System.currentTimeMillis();
        long fiveMinutes = 5 * 60 * 1000;

        // ✅ Check if OTP has expired
        if (otpTime == null || currentTime - otpTime > fiveMinutes) {
            session.removeAttribute("otp");
            session.removeAttribute("otpTime");
            session.removeAttribute("tempUser");

            request.setAttribute("msg", "OTP has expired. Please sign up again.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // ✅ If OTP is valid and not expired
        if (sessionOtp != null && inputOtp != null && inputOtp.equals(sessionOtp)) {
            boolean added = new UserDAO().addNewUser(tempUser);

            session.removeAttribute("otp");
            session.removeAttribute("otpTime");
            session.removeAttribute("tempUser");

            if (added) {
                request.setAttribute("successMsg", "Signup successful. Please log in.");
            } else {
                request.setAttribute("msg", "Registration failed. Please try again.");
            }
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
        } else {
            // ❌ KHÔNG reset otpTime

            // ✅ Tính thời gian còn lại cho OTP countdown
            long otpRemainingSeconds = 0;
            if (otpTime != null && currentTime - otpTime < fiveMinutes) {
                otpRemainingSeconds = (fiveMinutes - (currentTime - otpTime)) / 1000;
            }

            // ✅ Tính thời gian còn lại cho resend cooldown
            Long lastResendTime = (Long) session.getAttribute("lastResendTime");
            long resendCooldown = 30 * 1000;
            long resendCooldownLeft = 0;
            if (lastResendTime != null && currentTime - lastResendTime < resendCooldown) {
                resendCooldownLeft = (resendCooldown - (currentTime - lastResendTime)) / 1000;
            }

            request.setAttribute("msg", "Invalid OTP. Please try again.");
            request.setAttribute("otpRemainingSeconds", otpRemainingSeconds);
            request.setAttribute("resendCooldownLeft", resendCooldownLeft);
            request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
        }
    }
}
