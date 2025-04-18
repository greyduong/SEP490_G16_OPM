/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.Validation;
import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author tuan
 */
@WebServlet(name = "AuthenticationController", urlPatterns = {"/auth"})
public class AuthenticationController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthenticationController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthenticationController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Handles GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    // Handles POST requests (login & signup)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equalsIgnoreCase(action)) {
            handleLogin(request, response);
        } else if ("signup".equalsIgnoreCase(action)) {
            handleSignup(request, response);
        } else if ("resend-otp".equalsIgnoreCase(action)) {
            handleResendOtp(request, response); // ✅ add this
        } else {
            response.sendRedirect("login-register.jsp");
        }
    }

    // LOGIN
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String errorMsg = Validation.validateLogin(username, password);
        if (errorMsg != null) {
            request.setAttribute("msg", errorMsg);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean isValidUser = userDAO.checkUser(username, password);

        if (isValidUser) {
            User user = userDAO.getUserByUsername(username);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            session.setAttribute("username", username);

            session.setAttribute("successMsg", "Login successful!");

            int roleId = user.getRoleID();
            switch (roleId) {
                case 1 ->
                    response.sendRedirect("admin.jsp");
                case 2 ->
                    response.sendRedirect("manager.jsp");
                case 3 ->

                    response.sendRedirect("staff.jsp");
                case 4 -> 
                    response.sendRedirect("sellerpage.jsp");
                case 5 ->

                    response.sendRedirect("home");

                default ->
                    response.sendRedirect("index.html");
            }
        } else {
            request.setAttribute("username", username);
            request.setAttribute("msg", "Incorrect username or password.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
        }
    }

    // SIGNUP
    private void handleSignup(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String cfpassword = request.getParameter("cfpassword");
        String role = request.getParameter("role");
        String acceptedTerms = request.getParameter("terms"); // 👈 lấy checkbox

        // Validate thông tin đầu vào
        String errorMsg = Validation.validateRegister(fullname, username, password, cfpassword);
        if (errorMsg != null) {
            setSignupAttributes(request, fullname, username, email, phone, address);
            request.setAttribute("msg", errorMsg);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.checkExistsUsername(username)) {
            setSignupAttributes(request, fullname, username, email, phone, address);
            request.setAttribute("msg", "Username này đã được sử dụng.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }
        if (userDAO.checkExistsEmail(email)) {
            setSignupAttributes(request, fullname, username, email, phone, address);
            request.setAttribute("msg", "Email này đã được sử dụng.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        if (role == null || role.equals("Choose Role")) {
            setSignupAttributes(request, fullname, username, email, phone, address);
            request.setAttribute("msg", "Please select a role.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // ✅ Kiểm tra checkbox "Tôi đồng ý điều khoản"
        if (acceptedTerms == null || !acceptedTerms.equals("accepted")) {
            setSignupAttributes(request, fullname, username, email, phone, address);
            request.setAttribute("msg", "Bạn phải đồng ý với điều khoản và điều kiện để tiếp tục.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        int roleID = role.equals("Seller") ? 4 : role.equals("Dealer") ? 5 : 0;

        // ✅ Generate OTP
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        // ✅ Store temp user & OTP in session
        User tempUser = new User();
        tempUser.setFullName(fullname);
        tempUser.setUsername(username);
        tempUser.setEmail(email);
        tempUser.setPhone(phone);
        tempUser.setAddress(address);
        tempUser.setPassword(password);
        tempUser.setRoleID(roleID);
        tempUser.setWallet(0.0);
        tempUser.setStatus("Active");

        HttpSession session = request.getSession();
        session.setAttribute("tempUser", tempUser);
        session.setAttribute("otp", otp);
        long otpTime = System.currentTimeMillis();
        session.setAttribute("otpTime", otpTime); // track thời gian OTP
        session.setAttribute("lastResendTime", otpTime); // khởi tạo resend cooldown

        try {
            String subject = "Online Pig Market - Your OTP Code";
            String body = "Hello " + fullname + ",\n\nYour OTP code is: " + otp + "\nIt will expire in 5 minutes.\n\nThanks,\nOnline Pig Market Team";
            model.Email.sendEmail(email, subject, body);

            // Gửi các thông số countdown xuống JSP
            request.setAttribute("otpRemainingSeconds", 300); // 5 phút
            request.setAttribute("resendCooldownLeft", 30);   // 30 giây cooldown
            request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Failed to send OTP. Please try again.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
        }
    }

    private void setSignupAttributes(HttpServletRequest request, String fullname, String username, String email, String phone, String address) {
        request.setAttribute("fullname", fullname);
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
    }

    private void handleResendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User tempUser = (User) session.getAttribute("tempUser");

        if (tempUser == null) {
            request.setAttribute("msg", "Session expired. Please register again.");
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        Long lastResendTime = (Long) session.getAttribute("lastResendTime");
        long cooldown = 30 * 1000;
        long now = System.currentTimeMillis();

        if (lastResendTime != null && now - lastResendTime < cooldown) {
            long secondsLeft = (cooldown - (now - lastResendTime)) / 1000;
            request.setAttribute("msg", "Please wait " + secondsLeft + " seconds before requesting another OTP.");
            request.setAttribute("resendCooldownLeft", secondsLeft);

            // ✅ Tính thời gian còn lại của OTP để giữ countdown đúng
            Long otpTime = (Long) session.getAttribute("otpTime");
            long otpRemaining = 0;
            if (otpTime != null && now - otpTime < 5 * 60 * 1000) {
                otpRemaining = ((5 * 60 * 1000) - (now - otpTime)) / 1000;
            }
            request.setAttribute("otpRemainingSeconds", otpRemaining);

            request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
            return;
        }

        // ✅ Gửi lại OTP
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        session.setAttribute("otp", otp);
        session.setAttribute("otpTime", now); // reset OTP time
        session.setAttribute("lastResendTime", now);

        try {
            String subject = "Online Pig Market - New OTP Code";
            String body = "Hello " + tempUser.getFullName() + ",\n\nYour new OTP code is: " + otp
                    + "\nIt will expire in 5 minutes.\n\nThanks,\nOnline Pig Market Team";
            model.Email.sendEmail(tempUser.getEmail(), subject, body);

            request.setAttribute("msg", "OTP has been resent successfully.");
        } catch (Exception e) {
            request.setAttribute("msg", "Failed to resend OTP. Please try again.");
            e.printStackTrace();
        }

        request.setAttribute("resendCooldownLeft", 30);
        request.setAttribute("otpRemainingSeconds", 300);
        request.getRequestDispatcher("otp-verification.jsp").forward(request, response);
    }

}
