/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ConfirmOrderController", urlPatterns = {"/ConfirmOrderController"})
public class ConfirmOrderController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConfirmOrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfirmOrderController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current session to check user info
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in and has the Seller role
        if (user == null || user.getRoleID() != 4) {  // Ensure the user is logged in and is a Seller
            response.sendRedirect("login-register.jsp");  // Redirect to login page if not logged in or not a Seller
            return;
        }

        // Get the orderID from the request (sent by the confirm button in the form)
        String orderIDStr = request.getParameter("orderID");

        if (orderIDStr != null) {
            try {
                int orderID = Integer.parseInt(orderIDStr);

                // Create an instance of OrderDAO to retrieve the order
                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(orderID);

                // Ensure that the order belongs to the current logged-in seller
                if (order != null && order.getSellerID() == user.getUserID() && order.getStatus().equals("Pending")) {
                    // Call the method to confirm the order (change its status to 'Confirmed')
                    boolean isUpdated = orderDAO.confirmOrder(orderID);

                    if (isUpdated) {
                        // Redirect back to the order request page with updated status
                        response.sendRedirect("OrdersRequestController");
                    } else {
                        // If the update failed, show an error message
                        request.setAttribute("msg", "Error: Could not confirm the order.");
                        request.getRequestDispatcher("OrdersRequestController").forward(request, response);
                    }
                } else {
                    // If the order is not found or does not belong to the seller, redirect with an error
                    request.setAttribute("msg", "You are not authorized to confirm this order.");
                    request.getRequestDispatcher("OrdersRequestController").forward(request, response);
                }

            } catch (NumberFormatException e) {
                // Handle invalid orderID format
                response.sendRedirect("OrdersRequestController");
            }
        } else {
            // If no orderID is found in the request, redirect back to the order request page
            response.sendRedirect("OrdersRequestController");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
