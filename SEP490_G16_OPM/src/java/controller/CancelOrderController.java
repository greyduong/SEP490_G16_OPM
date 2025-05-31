package controller;

import dao.OrderDAO;
import dao.PigsOfferDAO;
import dao.Validation;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CancelOrderController", urlPatterns = {"/cancel-order"})
public class CancelOrderController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String orderIdStr = request.getParameter("orderId");
        String cancelReason = request.getParameter("cancelReason");

        if (orderIdStr == null) {
            session.setAttribute("msg", "Thiếu mã đơn hàng.");
            response.sendRedirect("myorders");
            return;
        }

        // ✅ Validate lý do hủy
        String reasonError = Validation.validateCancelReason(cancelReason);
        if (reasonError != null) {
            session.setAttribute("msg", reasonError);
            response.sendRedirect("myorders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO orderDAO = getOrderDAO();
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                session.setAttribute("msg", "Đơn hàng không tồn tại.");
                response.sendRedirect("myorders");
                return;
            }

            if (!"Pending".equalsIgnoreCase(order.getStatus())) {
                session.setAttribute("msg", "Chỉ có đơn trạng thái chờ xác nhận mới được hủy.");
                response.sendRedirect("myorders");
                return;
            }

            if (order.getDealer() == null || order.getDealer().getUserID() != user.getUserID()) {
                session.setAttribute("msg", "Bạn không có quyền hủy đơn này.");
                response.sendRedirect("myorders");
                return;
            }

            LocalDateTime createdAt = order.getCreatedAt().toLocalDateTime();
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            Duration duration = Duration.between(createdAt, now);
            if (duration.toHours() > 24) {
                session.setAttribute("msg", "Đơn hàng đã quá 24 giờ, không thể hủy.");
                response.sendRedirect("myorders");
                return;
            }

            boolean canceled = orderDAO.cancelOrder(orderId);

            if (canceled) {

                boolean noted = orderDAO.updateOrderNote(orderId, cancelReason);
                // ✅ Khôi phục số lượng
                PigsOfferDAO offerDAO = getPigsOfferDAO();
                int offerId = order.getOfferID();
                int quantity = order.getQuantity();

                offerDAO.updateOfferAfterReject(offerId, quantity);
                int currentQuantity = offerDAO.getOfferQuantity(offerId);

                if (currentQuantity > 0) {
                    offerDAO.setOfferStatus(offerId, "Available");
                }

                session.setAttribute("msg", noted
                        ? "Hủy đơn thành công."
                        : "Hủy đơn thành công, nhưng không thể lưu ghi chú.");

                // ✅ Gửi email cho cả hai bên
                try {
                    String buyerEmail = order.getDealer().getEmail();
                    String buyerName = order.getDealer().getFullName();
                    String subject = "Thông báo hủy đơn hàng #" + orderId;
                    String content = "Xin chào " + buyerName + ",\n\n"
                            + "Đơn hàng #" + orderId + " của bạn đã được hủy thành công.\n"
                            + "Lý do: " + cancelReason + "\n"
                            + "Số lượng heo đã được hoàn trả vào chào bán.\n\n"
                            + "Cảm ơn bạn đã sử dụng dịch vụ của Online Pig Market.";
                    model.Email.sendEmail(buyerEmail, subject, content);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    Logger.getLogger(CancelOrderController.class.getName()).log(Level.SEVERE, null, e);
                }

                try {
                    String sellerEmail = order.getSeller().getEmail();
                    String sellerName = order.getSeller().getFullName();
                    String subject = "Đơn hàng #" + orderId + " đã bị hủy";
                    String content = "Xin chào " + sellerName + ",\n\n"
                            + "Đơn hàng #" + orderId + " mà bạn bán cho khách đã bị hủy.\n"
                            + "Lý do: " + cancelReason + "\n"
                            + "Số lượng heo đã được hoàn trả vào chào bán.\n\n"
                            + "Trân trọng,\nOnline Pig Market.";
                    model.Email.sendEmail(sellerEmail, subject, content);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    Logger.getLogger(CancelOrderController.class.getName()).log(Level.SEVERE, null, e);
                }

            } else {
                session.setAttribute("msg", "Hủy đơn thất bại, vui lòng thử lại.");
            }

            response.sendRedirect("myorders");

        } catch (NumberFormatException e) {
            session.setAttribute("msg", "Mã đơn hàng không hợp lệ.");
            response.sendRedirect("myorders");
        } catch (Exception e) {
            Logger.getLogger(CancelOrderController.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("msg", "Đã xảy ra lỗi khi hủy đơn.");
            response.sendRedirect("myorders");
        }
    }

    public PigsOfferDAO getPigsOfferDAO() {
        return new PigsOfferDAO();
    }

    public OrderDAO getOrderDAO() {
        return new OrderDAO();
    }
}
