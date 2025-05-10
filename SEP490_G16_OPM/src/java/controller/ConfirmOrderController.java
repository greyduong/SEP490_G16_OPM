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
import model.Email;
import model.Order;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ConfirmOrderController", urlPatterns = {"/confirm-order"})
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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String orderIDStr = request.getParameter("orderID");

        if (orderIDStr != null) {
            try {
                int orderID = Integer.parseInt(orderIDStr);

                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(orderID);

                if (order != null && order.getSellerID() == user.getUserID() && order.getStatus().equals("Pending")) {
                    boolean isUpdated = orderDAO.confirmOrder(orderID);

                    if (isUpdated) {
                        String toEmail = order.getDealer().getEmail();
                        String buyerName = order.getDealer().getFullName();

                        String subject = "Đơn hàng #" + orderID + " đã được xác nhận";
                        String content = "Xin chào " + buyerName + ",\n\n"
                                + "Đơn hàng của bạn với mã #" + orderID + " đã được người bán xác nhận.\n"
                                + "Vui lòng truy cập hệ thống để xem chi tiết đơn hàng.\n\n"
                                + "Trân trọng,\nOnline Pig Market.";

                        try {
                            Email.sendEmail(toEmail, subject, content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String encodedMsg = java.net.URLEncoder.encode("Xác nhận đơn hàng thành công.", "UTF-8");
                        response.sendRedirect("orders-request?msg=" + encodedMsg);
                    } else {
                        request.setAttribute("msg", "Lỗi: Không thể xác nhận đơn hàng.");
                        request.getRequestDispatcher("orders-request").forward(request, response);
                    }
                } else {
                    request.setAttribute("msg", "Bạn không có quyền xác nhận đơn hàng này.");
                    request.getRequestDispatcher("orders-request").forward(request, response);
                }

            } catch (NumberFormatException e) {
                response.sendRedirect("orders-request");
            }
        } else {
            response.sendRedirect("orders-request");
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
