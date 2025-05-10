/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DeliveryDAO;
import dao.FarmDAO;
import dao.OrderDAO;
import dao.UserDAO;
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
import model.Farm;
import model.Order;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ViewOrderDetailController", urlPatterns = {"/view-order-detail"})
public class ViewOrderDetailController extends HttpServlet {

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
            out.println("<title>Servlet ViewOrderDetailController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewOrderDetailController at " + request.getContextPath() + "</h1>");
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

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderId);

            if (order == null || order.getDealerID() != user.getUserID()) {
                request.setAttribute("msg", "Bạn không có quyền truy cập đơn hàng này hoặc đơn hàng không tồn tại.");
                request.getRequestDispatcher("/myorders").forward(request, response);
                return;
            }

            UserDAO userDAO = new UserDAO();
            User buyer = userDAO.getUserById(order.getDealerID());
            User seller = userDAO.getUserById(order.getSellerID());

            DeliveryDAO deliveryDAO = new DeliveryDAO();
            List<Delivery> deliveries = deliveryDAO.getDeliveriesByOrderId(orderId);

            // Tính tổng số lượng và tổng giá trị chỉ từ delivery đã Confirmed
            int totalDeliveredQuantity = deliveryDAO.getTotalDeliveredQuantity(orderId);
            double totalDeliveredPrice = deliveryDAO.getTotalDeliveredPrice(orderId);

            int totalConfirmedOrPendingQty = deliveryDAO.getTotalQuantityByStatuses(orderId);
            double totalConfirmedOrPendingPrice = deliveryDAO.getTotalPriceByStatuses(orderId);

            int totalPendingQty = totalConfirmedOrPendingQty - totalDeliveredQuantity;
            double totalPendingPrice = totalConfirmedOrPendingPrice - totalDeliveredPrice;

            int realRemainingQty = order.getQuantity() - totalConfirmedOrPendingQty;
            double realRemainingPrice = order.getTotalPrice() - totalConfirmedOrPendingPrice;

            request.setAttribute("order", order);
            request.setAttribute("buyer", buyer);
            request.setAttribute("seller", seller);
            request.setAttribute("deliveryList", deliveries);
            request.setAttribute("totalDeliveredQuantity", totalDeliveredQuantity);
            request.setAttribute("totalDeliveredPrice", totalDeliveredPrice);
            request.setAttribute("totalPendingQuantity", totalPendingQty);
            request.setAttribute("totalPendingPrice", totalPendingPrice);
            request.setAttribute("totalCreatedQuantity", totalConfirmedOrPendingQty);
            request.setAttribute("totalCreatedPrice", totalConfirmedOrPendingPrice);
            request.setAttribute("realRemainingQuantity", realRemainingQty);
            request.setAttribute("realRemainingPrice", realRemainingPrice);

            String msg = request.getParameter("msg");
            if (msg != null) {
                request.setAttribute("msg", java.net.URLDecoder.decode(msg, "UTF-8"));
            }

            request.getRequestDispatcher("order_detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Đã xảy ra lỗi trong quá trình tải đơn hàng.");
            request.getRequestDispatcher("/myorders").forward(request, response);
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
        doGet(request, response);
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
