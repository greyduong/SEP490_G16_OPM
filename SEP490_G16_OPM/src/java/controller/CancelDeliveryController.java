package controller;

import dao.DeliveryDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dao.Validation;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Email;
import model.Order;
import model.User;

@WebServlet(name = "CancelDeliveryController", urlPatterns = {"/cancel-delivery"})
public class CancelDeliveryController extends HttpServlet {
    private DeliveryDAO deliveryDAO = new DeliveryDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int deliveryID = Integer.parseInt(request.getParameter("deliveryID"));

            int orderID = deliveryDAO.getOrderIdByDeliveryId(deliveryID);
            int dealerID = deliveryDAO.getDealerIdByDeliveryId(deliveryID);

            if (user == null || user.getRoleID() != 5 || user.getUserID() != dealerID) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode("Bạn không có quyền hủy giao hàng này.", "UTF-8"));
                return;
            }

            String deliveryStatus = deliveryDAO.getDeliveryStatusById(deliveryID);
            String orderStatus = deliveryDAO.getOrderStatusByDeliveryId(deliveryID);
            String cancelReason = request.getParameter("cancelReason");

            if (!"Pending".equalsIgnoreCase(deliveryStatus) || !"Processing".equalsIgnoreCase(orderStatus)) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode("Chỉ được hủy khi đơn giao hàng đang chờ và đơn hàng đang xử lý.", "UTF-8"));
                return;
            }

            String validationError = Validation.validateCancelReason(cancelReason);
            if (validationError != null) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode(validationError, "UTF-8"));
                return;
            }

            String cleanedReason = cancelReason.trim().replaceAll("\\s{2,}", " ");

            boolean canceled = deliveryDAO.updateDeliveryStatus(deliveryID, "Canceled");
            if (canceled) {
                deliveryDAO.appendToDeliveryComments(deliveryID, "[HỦY] Lý do: " + cleanedReason);
            }

            String msg;
            if (canceled) {
                Order order = orderDAO.getOrderById(orderID);
                User seller = userDAO.getUserById(order.getSellerID());
                User dealer = userDAO.getUserById(order.getDealerID());

                double canceledPrice = deliveryDAO.getDeliveryTotalPrice(deliveryID);
                int canceledQuantity = deliveryDAO.getDeliveryQuantity(deliveryID);
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedPrice = formatter.format(canceledPrice) + " VND";

                msg = "Giao hàng đã được hủy.";
                String subject = "Thông báo: Giao hàng đã bị hủy - Đơn hàng #" + orderID;

                String contentForSeller = "Xin chào " + seller.getFullName() + ",\n\n"
                        + "Giao hàng (Mã giao hàng: #" + deliveryID + ") trong đơn hàng #" + orderID + " đã bị người mua hủy.\n"
                        + "- Số lượng: " + canceledQuantity + " con\n"
                        + "- Tổng giá trị: " + formattedPrice + "\n"
                        + "- Lý do: " + cleanedReason + "\n\n"
                        + "Vui lòng kiểm tra đơn hàng trong hệ thống.\n\nOnline Pig Market.";

                String contentForDealer = "Xin chào " + dealer.getFullName() + ",\n\n"
                        + "Bạn đã hủy thành công giao hàng (Mã giao hàng: #" + deliveryID + ") trong đơn hàng #" + orderID + ".\n"
                        + "- Số lượng: " + canceledQuantity + " con\n"
                        + "- Tổng giá trị: " + formattedPrice + "\n"
                        + "- Lý do: " + cleanedReason + "\n\n"
                        + "Nếu đây không phải là bạn thực hiện, vui lòng liên hệ hỗ trợ ngay.\n\nOnline Pig Market.";

                try {
                    Email.sendEmail(seller.getEmail(), subject, contentForSeller);
                    Email.sendEmail(dealer.getEmail(), subject, contentForDealer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msg = "Hủy giao hàng thất bại.";
            }

            response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                    + URLEncoder.encode(msg, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myorders?msg=" + URLEncoder.encode("Lỗi khi hủy giao hàng.", "UTF-8"));
        }
    }
}