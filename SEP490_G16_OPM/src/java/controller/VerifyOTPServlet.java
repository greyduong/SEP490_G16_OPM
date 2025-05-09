package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = {"/verify-otp"})
public class VerifyOTPServlet extends HttpServlet {

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 phút
    private static final long RESEND_COOLDOWN = 30 * 1000; // 30 giây

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String inputOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        Long otpTime = (Long) session.getAttribute("otpTime");
        User tempUser = (User) session.getAttribute("tempUser");
        String otpPurpose = (String) session.getAttribute("otpPurpose"); // 🆕 Lấy mục đích OTP

        long currentTime = System.currentTimeMillis();

        // ✅ Check OTP hết hạn
        if (otpTime == null || currentTime - otpTime > OTP_VALID_DURATION) {
            session.removeAttribute("otp");
            session.removeAttribute("otpTime");
            session.removeAttribute("tempUser");
            session.removeAttribute("otpPurpose");

            request.setAttribute("msg", "OTP đã hết hạn. Vui lòng thực hiện lại.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        // ✅ Nếu OTP đúng
        if (sessionOtp != null && inputOtp != null && inputOtp.equals(sessionOtp)) {

            if ("signup".equals(otpPurpose)) {
                // 🔵 Xử lý đăng ký tài khoản mới
                boolean added = new UserDAO().addNewUser(tempUser);

                // Xóa session tạm
                session.removeAttribute("otp");
                session.removeAttribute("otpTime");
                session.removeAttribute("tempUser");
                session.removeAttribute("otpPurpose");

                if (added) {
                    request.setAttribute("successMsg", "Đăng ký thành công. Vui lòng đăng nhập.");
                } else {
                    request.setAttribute("msg", "Đăng ký thất bại. Vui lòng thử lại.");
                }
                request.getRequestDispatcher("login-register.jsp").forward(request, response);

            } else if ("forgot-password".equals(otpPurpose)) {
                // 🟢 Xử lý quên mật khẩu: chuyển tới form đổi mật khẩu
                request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            } else {
                // ❌ Nếu otpPurpose null hoặc sai
                session.invalidate();
                response.sendRedirect("login-register.jsp");
            }

        } else {
            // ❌ Nếu OTP sai
            long otpRemainingSeconds = (OTP_VALID_DURATION - (currentTime - otpTime)) / 1000;
            if (otpRemainingSeconds < 0) otpRemainingSeconds = 0;

            Long lastResendTime = (Long) session.getAttribute("lastResendTime");
            long resendCooldownLeft = 0;
            if (lastResendTime != null && currentTime - lastResendTime < RESEND_COOLDOWN) {
                resendCooldownLeft = (RESEND_COOLDOWN - (currentTime - lastResendTime)) / 1000;
            }

            request.setAttribute("msg", "OTP không chính xác. Vui lòng thử lại.");
            request.setAttribute("otpRemainingSeconds", otpRemainingSeconds);
            request.setAttribute("resendCooldownLeft", resendCooldownLeft);
            request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
        }
    }
}
