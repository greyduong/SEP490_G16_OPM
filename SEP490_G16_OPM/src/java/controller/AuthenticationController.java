/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
        } else {
            response.sendRedirect("login-register.jsp");
        }
    }

    // LOGIN
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();

        // Validate input
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("msg", "Username or password cannot be empty.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // Check credentials
        boolean isValidUser = userDAO.checkUser(username, password);

        if (isValidUser) {
            User user = userDAO.getUserByUsername(username);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("successMsg", "Login successful!");

            int roleId = user.getRoleID();

            switch (roleId) {
                case 1: // Admin
                    response.sendRedirect("admin.jsp");
                    break;
                case 2: // Manager
                    response.sendRedirect("manager.jsp");
                    break;
                case 3: // Staff
                    response.sendRedirect("staff.jsp");
                    break;
                case 4: // Seller
                    response.sendRedirect("index.html");
                    break;
                case 5: // Dealer
                    response.sendRedirect("index.html");
                    break;
                default: // Unknown or future role
                    response.sendRedirect("index.html");
                    break;
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
        String role = request.getParameter("role"); // Capture selected role

        UserDAO userDAO = new UserDAO();

        // Check if username exists
        if (userDAO.checkExistsUsername(username)) {
            request.setAttribute("msg", "Username already exists.");
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // Check if passwords match
        if (!password.equals(cfpassword)) {
            request.setAttribute("msg", "Passwords do not match.");
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // Check if a role is selected
        if (role == null || role.equals("Choose Role")) {
            request.setAttribute("msg", "Please select a role.");
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        // Convert role to RoleID (assuming role 'Seller' corresponds to RoleID = 4 and 'Dealer' to RoleID = 5)
        int roleID = 0;
        if (role.equals("Seller")) {
            roleID = 4;
        } else if (role.equals("Dealer")) {
            roleID = 5;
        }

        // Create new user object and set fields
        User newUser = new User();
        newUser.setFullName(fullname);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setPassword(password);
        newUser.setRoleID(roleID);  // Set the role ID based on selected role
        newUser.setWallet(0.0);      // Default wallet
        newUser.setStatus("Active");

        // Save to database
        boolean isAdded = userDAO.addNewUser(newUser);

        if (isAdded) {
            request.setAttribute("successMsg", "Signup successful. Please log in.");
        } else {
            request.setAttribute("msg", "Signup failed. Please try again.");
        }

        request.getRequestDispatcher("login-register.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles login and signup actions";
    }
}
