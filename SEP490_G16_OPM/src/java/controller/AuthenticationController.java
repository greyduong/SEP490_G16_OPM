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
                    response.sendRedirect("StaffViewApplication");
                case 4, 5 ->

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

        String errorMsg = Validation.validateRegister(fullname, username, password, cfpassword);
        if (errorMsg != null) {
            request.setAttribute("msg", errorMsg);
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();

        if (userDAO.checkExistsUsername(username)) {
            request.setAttribute("msg", "Username already exists.");
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        if (role == null || role.equals("Choose Role")) {
            request.setAttribute("msg", "Please select a role.");
            request.setAttribute("fullname", fullname);
            request.setAttribute("username", username);
            request.getRequestDispatcher("login-register.jsp").forward(request, response);
            return;
        }

        int roleID = role.equals("Seller") ? 4 : role.equals("Dealer") ? 5 : 0;

        User newUser = new User();
        newUser.setFullName(fullname);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setPassword(password);
        newUser.setRoleID(roleID);
        newUser.setWallet(0.0);
        newUser.setStatus("Active");

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
