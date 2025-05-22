/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
import dao.OrderDAO;
import dao.PigsOfferDAO;
import dao.UserDAO;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cart;
import model.Email;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "CheckOutController", urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    public CartDAO getCartDAO() {
        return new CartDAO();
    }

    public PigsOfferDAO getOfferDAO() {
        return new PigsOfferDAO();
    }

    public OrderDAO getOrderDAO() {
        return new OrderDAO();
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int offerId = Integer.parseInt(request.getParameter("offerId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            CartDAO cartDAO = getCartDAO();
            PigsOfferDAO offerDAO = getOfferDAO();
            OrderDAO orderDAO = getOrderDAO();
            UserDAO userDAO = getUserDAO();

            Cart cart = cartDAO.getCartById(cartId);
            if (cart == null || cart.getUser().getUserID() != user.getUserID()) {
                request.setAttribute("error", "Giỏ hàng không hợp lệ.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            PigsOffer offer = offerDAO.getOfferById(offerId);
            if (offer == null) {
                request.setAttribute("error", "Không tìm thấy offer.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            String status = offer.getStatus();
            if ("Unavailable".equalsIgnoreCase(status) || "Upcoming".equalsIgnoreCase(status) || "Banned".equalsIgnoreCase(status)) {
                request.setAttribute("error", "Chào bán này hiện không thể đặt hàng do đang ngưng bán hoặc bị cấm.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            if (quantity < offer.getMinQuantity() || quantity > offer.getQuantity()) {
                System.out.println("quantity = " + quantity);
                System.out.println("offer.minQuantity = " + offer.getMinQuantity());
                System.out.println("offer.quantity = " + offer.getQuantity());
                request.setAttribute("error", "Số lượng không hợp lệ.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            User seller = userDAO.getUserById(offer.getSellerID());

            // Tính tổng giá
            double totalPrice;
            if (quantity == offer.getQuantity()) {
                totalPrice = offer.getTotalOfferPrice();
            } else {
                totalPrice = offer.getRetailPrice() * quantity;
            }

            // Thêm đơn hàng
            orderDAO.insertOrder(user.getUserID(), offer.getSellerID(), offerId, quantity, totalPrice);
            // Cập nhật số lượng sau checkout
            offerDAO.updateOfferQuantityAfterCheckout(offerId, quantity);

            // Kiểm tra lại số lượng sau khi trừ
            PigsOffer updatedOffer = offerDAO.getOfferById(offerId);
            if (updatedOffer.getQuantity() == 0) {
                // Cập nhật trạng thái chào bán thành ngưng bán
                offerDAO.updateOfferStatus(offerId, "Unavailable");
            }

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
            } catch (MessagingException | UnsupportedEncodingException e) {
                Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, null, e);
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
            } catch (MessagingException | UnsupportedEncodingException e) {
                Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, null, e);
            }
            // Redirect
            response.sendRedirect("myorders");

        } catch (Exception e) {
            Logger.getLogger(CheckOutController.class.getName()).log(Level.SEVERE, null, e);
            request.setAttribute("error", "Có lỗi xảy ra khi đặt hàng.");
            request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
        }
    }
}
