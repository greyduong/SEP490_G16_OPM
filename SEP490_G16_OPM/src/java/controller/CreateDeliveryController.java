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
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "CreateDeliveryController", urlPatterns = {"/CreateDeliveryController"})
public class CreateDeliveryController extends HttpServlet {

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
            out.println("<title>Servlet CreateDeliveryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateDeliveryController at " + request.getContextPath() + "</h1>");
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

        // Kiểm tra đăng nhập và quyền
        if (user == null || user.getRoleID() != 4) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        int orderID = Integer.parseInt(request.getParameter("orderID"));

        try {
            String recipientName = request.getParameter("recipientName");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
            String comments = request.getParameter("comments");
            int sellerId = Integer.parseInt(request.getParameter("sellerID"));
            int dealerId = Integer.parseInt(request.getParameter("dealerID"));

            OrderDAO orderDAO = new OrderDAO();
            DeliveryDAO deliveryDAO = new DeliveryDAO();

            // Kiểm tra quyền sở hữu đơn hàng
            if (!orderDAO.isOrderOwnedBySeller(orderID, user.getUserID())) {
                session.setAttribute("msg", "Bạn không có quyền tạo giao hàng cho đơn này.");
                response.sendRedirect("CustomerOrderPageController");
                return;
            }

            // Lấy thông tin đã giao
            int deliveredQty = deliveryDAO.getTotalDeliveredQuantity(orderID);
            double deliveredTotal = deliveryDAO.getTotalDeliveredPrice(orderID);

            int orderQuantity = orderDAO.getOrderQuantity(orderID);
            double orderTotalPrice = orderDAO.getOrderTotalPrice(orderID);

            int remainingQty = orderQuantity - deliveredQty;
            double remainingPrice = orderTotalPrice - deliveredTotal;

            // Validate dữ liệu nhập
            if (remainingQty == 0 && remainingPrice == 0) {
                session.setAttribute("msg", "Đơn hàng đã được giao xong, không thể tạo!");
                response.sendRedirect("CustomerOrderDetailController?id=" + orderID + "&openCreateDelivery=true");
                return;
            }

            // Validate dữ liệu nhập
            if (quantity < 0 || quantity > remainingQty || totalPrice < 0 || totalPrice > remainingPrice) {
                session.setAttribute("msg", "Số lượng hoặc tổng tiền vượt quá phần còn lại của đơn hàng.");
                response.sendRedirect("CustomerOrderDetailController?id=" + orderID + "&openCreateDelivery=true");
                return;
            }

            // Thêm giao hàng
            boolean success = deliveryDAO.createDelivery(orderID, sellerId, dealerId, recipientName, quantity, totalPrice, comments);

            if (success) {
                session.setAttribute("msg", "Tạo giao hàng thành công.");
            } else {
                session.setAttribute("msg", "Đã xảy ra lỗi khi tạo giao hàng.");
            }

            response.sendRedirect("CustomerOrderDetailController?id=" + orderID);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Đã xảy ra lỗi hệ thống.");
            response.sendRedirect("CustomerOrderDetailController?id=" + request.getParameter("orderID"));
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
