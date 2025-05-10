/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DeliveryDAO;
import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Delivery;
import model.Order;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "CustomerOrderDetailController", urlPatterns = {"/customer-order-details"})
public class CustomerOrderDetailController extends HttpServlet {

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
            out.println("<title>Servlet CustomerOrderDetailController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerOrderDetailController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String orderIdStr = request.getParameter("id");
        if (orderIdStr != null) {
            try {
                int orderID = Integer.parseInt(orderIdStr);
                String openCreateDelivery = request.getParameter("openCreateDelivery");
                if ("true".equals(openCreateDelivery)) {
                    request.setAttribute("showCreateDeliveryForm", true);
                }
                // Fetch order and delivery details
                OrderDAO orderDAO = new OrderDAO();
                DeliveryDAO deliveryDAO = new DeliveryDAO();

                Order order = orderDAO.getOrderById(orderID);  // Fetch the order by ID
                List<Delivery> deliveries = deliveryDAO.getDeliveriesByOrderId(orderID);  // Fetch deliveries for this seller

                int totalDeliveredQuantity = deliveries.stream().mapToInt(Delivery::getQuantity).sum();
                double totalDeliveredPrice = deliveries.stream().mapToDouble(Delivery::getTotalPrice).sum();

                int remainingQuantity = order.getQuantity() - totalDeliveredQuantity;
                double remainingPrice = order.getTotalPrice() - totalDeliveredPrice;

                request.setAttribute("totalDeliveredQuantity", totalDeliveredQuantity);
                request.setAttribute("totalDeliveredPrice", totalDeliveredPrice);
                request.setAttribute("remainingQuantity", remainingQuantity);
                request.setAttribute("remainingPrice", remainingPrice);

                // Set attributes to be accessed in JSP
                request.setAttribute("order", order);
                request.setAttribute("deliveryList", deliveries);

                // Forward to the customer order detail page
                String msg = (String) session.getAttribute("msg");
                if (msg != null) {
                    request.setAttribute("msg", msg);
                    session.removeAttribute("msg"); // remove để tránh hiển thị lại khi refresh
                }

                request.getRequestDispatcher("customerorderdetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect("home");
            }
        } else {
            response.sendRedirect("home");
        }
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
        processRequest(request, response);
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
