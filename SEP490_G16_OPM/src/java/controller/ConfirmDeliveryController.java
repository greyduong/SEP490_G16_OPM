/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DeliveryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ConfirmDeliveryController", urlPatterns = {"/ConfirmDeliveryController"})
public class ConfirmDeliveryController extends HttpServlet {

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
            out.println("<title>Servlet ConfirmDeliveryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfirmDeliveryController at " + request.getContextPath() + "</h1>");
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
        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getRoleID() != 5) { // chỉ dealer mới được xác nhận
            response.sendRedirect("login-register.jsp");
            return;
        }

        try {
            int deliveryID = Integer.parseInt(request.getParameter("deliveryID"));
            DeliveryDAO deliveryDAO = new DeliveryDAO();

            // Kiểm tra dealer có quyền với delivery này không
            int orderID = deliveryDAO.getOrderIdByDeliveryId(deliveryID);
            int dealerID = deliveryDAO.getDealerIdByDeliveryId(deliveryID);

            if (dealerID != user.getUserID()) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + java.net.URLEncoder.encode("Bạn không có quyền xác nhận giao hàng này.", "UTF-8"));
                return;
            }

            // Cập nhật trạng thái delivery
            boolean updated = deliveryDAO.confirmDelivery(deliveryID);
            String msg = updated ? "Giao hàng đã được xác nhận." : "Xác nhận thất bại.";

            response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                    + java.net.URLEncoder.encode(msg, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myorders?msg="
                    + java.net.URLEncoder.encode("Lỗi khi xác nhận giao hàng.", "UTF-8"));
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
