package controller;

import dao.OrderDAO;
import dao.PigsOfferDAO;
import dao.Validation; // ✅ THÊM import này
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Email;
import model.Order;
import model.User;

import java.io.IOException;

@WebServlet(name = "RejectOrderController", urlPatterns = {"/reject-order"})
public class RejectOrderController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String orderIDStr = request.getParameter("orderID");
        String rejectReason = request.getParameter("rejectReason"); // ✅ Lấy lý do từ chối

        if (orderIDStr == null) {
            session.setAttribute("msg", "Thiếu mã đơn hàng.");
            response.sendRedirect("orders-request");
            return;
        }

        String reasonError = Validation.validateRejectReason(rejectReason);
        if (reasonError != null) {
            session.setAttribute("msg", reasonError);
            response.sendRedirect("orders-request");
            return;
        }

        try {
            int orderID = Integer.parseInt(orderIDStr);
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderID);

            if (order == null
                    || order.getSellerID() != user.getUserID()
                    || !"Pending".equalsIgnoreCase(order.getStatus())) {
                session.setAttribute("msg", "Bạn không có quyền từ chối đơn hàng này hoặc trạng thái không hợp lệ.");
                response.sendRedirect("orders-request");
                return;
            }

            java.time.Instant createdAt = order.getCreatedAt().toInstant();
            java.time.Instant now = java.time.Instant.now();
            java.time.Duration duration = java.time.Duration.between(createdAt, now);
            if (duration.toHours() > 24) {
                session.setAttribute("msg", "Không thể từ chối đơn hàng vì đã quá 24 giờ kể từ khi tạo.");
                response.sendRedirect("orders-request");
                return;
            }

            boolean updated = orderDAO.rejectOrder(orderID);
            if (updated) {
                orderDAO.updateOrderNote(orderID, rejectReason);
                PigsOfferDAO offerDAO = new PigsOfferDAO();
                int offerId = order.getOfferID();
                int quantity = order.getQuantity();

                offerDAO.updateOfferQuantity(offerId, quantity);
                int currentQuantity = offerDAO.getOfferQuantity(offerId);
                if (currentQuantity > 0) {
                    offerDAO.setOfferStatus(offerId, "Available");
                }

                // Gửi email cho cả hai bên (giữ nguyên)
                try {
                    String toBuyer = order.getDealer().getEmail();
                    String buyerName = order.getDealer().getFullName();
                    String subjectBuyer = "Đơn hàng #" + orderID + " đã bị từ chối";
                    String contentBuyer = "Xin chào " + buyerName + ",\n\n"
                            + "Rất tiếc, đơn hàng #" + orderID + " của bạn đã bị người bán từ chối.\n"
                            + "Lý do từ chối: " + rejectReason + "\n\n"
                            + "Số lượng heo đã được hoàn trả vào chào bán.\n"
                            + "Bạn có thể đặt đơn hàng khác hoặc liên hệ nếu cần thêm thông tin.\n\n"
                            + "Trân trọng,\nOnline Pig Market.";
                    Email.sendEmail(toBuyer, subjectBuyer, contentBuyer);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String toSeller = order.getSeller().getEmail();
                    String sellerName = order.getSeller().getFullName();
                    String subjectSeller = "Bạn đã từ chối đơn hàng #" + orderID;
                    String contentSeller = "Xin chào " + sellerName + ",\n\n"
                            + "Bạn đã từ chối đơn hàng #" + orderID + " với lý do:\n" + rejectReason + "\n\n"
                            + "Số lượng heo đã được hoàn trả vào chào bán.\n\n"
                            + "Trân trọng,\nOnline Pig Market.";
                    Email.sendEmail(toSeller, subjectSeller, contentSeller);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.setAttribute("msg", "Đã từ chối đơn hàng thành công.");
                response.sendRedirect("orders-request");
            } else {
                session.setAttribute("msg", "Lỗi: Không thể từ chối đơn hàng.");
                response.sendRedirect("orders-request");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("msg", "Mã đơn hàng không hợp lệ.");
            response.sendRedirect("orders-request");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Đã xảy ra lỗi khi từ chối đơn hàng.");
            response.sendRedirect("orders-request");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý từ chối đơn hàng";
    }
}
