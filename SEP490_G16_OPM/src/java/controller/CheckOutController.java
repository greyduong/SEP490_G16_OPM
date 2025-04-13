/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
import dao.OrderDAO;
import dao.PigsOfferDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.DecimalFormat;
import model.Email;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "CheckOutController", urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

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
            out.println("<title>Servlet CheckOutController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckOutController at " + request.getContextPath() + "</h1>");
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
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int offerId = Integer.parseInt(request.getParameter("offerId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login-register.jsp");
                return;
            }

            CartDAO cartDAO = new CartDAO();
            PigsOfferDAO offerDAO = new PigsOfferDAO();
            OrderDAO orderDAO = new OrderDAO();
            UserDAO userDAO = new UserDAO();

            // Lấy offer
            PigsOffer offer = offerDAO.getOfferById(offerId);
            if (offer == null) {
                request.setAttribute("error", "Không tìm thấy offer.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            User seller = userDAO.getUserById(offer.getSellerID());

            // Tính tổng giá
            double totalPrice = offer.getRetailPrice() * quantity;

            // Thêm đơn hàng
            orderDAO.insertOrder(user.getUserID(), offer.getSellerID(), offerId, quantity, totalPrice);

            // Xoá cart
            cartDAO.deleteCartById(cartId);

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedTotalPrice = formatter.format(totalPrice) + " VND";
            // Gửi email xác nhận
            try {
                String subject = "Xác nhận đặt hàng thành công";
                String content = "Chào " + user.getFullName() + ",\n\n"
                        + "Bạn đã đặt hàng thành công sản phẩm: " + offer.getName() + "\n"
                        + "Số lượng: " + quantity + "\n"
                        + "Tổng tiền: " + formattedTotalPrice + "\n\n"
                        + "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!";
                Email.sendEmail(user.getEmail(), subject, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String subject = "Bạn có đơn hàng mới";
                String content = "Chào " + seller.getFullName() + ",\n\n"
                        + "Bạn vừa nhận được một đơn hàng mới:\n"
                        + "- Người mua: " + user.getFullName() + "\n"
                        + "- Sản phẩm: " + offer.getName() + "\n"
                        + "- Số lượng: " + quantity + "\n"
                        + "- Tổng tiền: " + formattedTotalPrice + "\n\n"
                        + "Vui lòng đăng nhập hệ thống để xử lý đơn hàng.\n\n"
                        + "Trân trọng,\nOnline Pig Market";
                Email.sendEmail(seller.getEmail(), subject, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Redirect
            response.sendRedirect("myorders");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi đặt hàng.");
            request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
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
