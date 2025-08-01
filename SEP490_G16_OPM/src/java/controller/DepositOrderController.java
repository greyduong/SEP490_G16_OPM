package controller;

import dao.OrderDAO;
import dao.WalletUseHistoryDAO;
import jakarta.mail.MessagingException;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Email;
import model.Order;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "DepositOrderController", urlPatterns = {"/deposit-order"})
public class DepositOrderController extends HttpServlet {

    public OrderDAO getOrderDAO() {
        return new OrderDAO();
    }

    public WalletUseHistoryDAO getWalletUseHistoryDAO() {
        return new WalletUseHistoryDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String orderIDStr = request.getParameter("orderId");

        // Lấy các tham số lọc để giữ lại khi redirect
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String page = request.getParameter("page");

        // Tạo chuỗi query giữ lại filter
        String baseQuery = "myorders?search=" + URLEncoder.encode(search != null ? search : "", StandardCharsets.UTF_8)
                + "&status=" + URLEncoder.encode(status != null ? status : "", StandardCharsets.UTF_8)
                + "&sort=" + URLEncoder.encode(sort != null ? sort : "", StandardCharsets.UTF_8)
                + "&page=" + URLEncoder.encode(page != null ? page : "1", StandardCharsets.UTF_8);

        if (orderIDStr != null) {
            try {
                OrderDAO orderDAO = getOrderDAO();
                int orderID = Integer.parseInt(orderIDStr);
                Order order = orderDAO.getOrderById(orderID);

                if (order != null && order.getDealerID() == user.getUserID() && "Confirmed".equals(order.getStatus())) {
                    long createdAtMillis = order.getProcessedDate().getTime();
                    long nowMillis = System.currentTimeMillis();
                    long diffInHours = (nowMillis - createdAtMillis) / (1000 * 60 * 60);

                    if (diffInHours >= 24) {
                        String msg = URLEncoder.encode("Đơn hàng đã quá hạn 24 giờ và không thể đặt cọc.", StandardCharsets.UTF_8);
                        response.sendRedirect(baseQuery + "&msg=" + msg);
                        return;
                    }
                    long amount = (long) (order.getTotalPrice() * 0.01);
                    if (!getWalletUseHistoryDAO().use(user.getUserID(), amount, "Đặt cọc cho đơn hàng #%s".formatted(order.getOrderID()))) {
                        String msg = URLEncoder.encode("Không đủ tiền trong ví!", StandardCharsets.UTF_8);
                        response.sendRedirect(baseQuery + "&msg=" + msg);
                        return;
                    }

                    boolean isUpdated = orderDAO.updateOrderStatus(orderID, "Deposited");

                    if (isUpdated) {
                        orderDAO.updateOrderNote(orderID, "Đơn hàng đã được đặt cọc.");
                        String buyerEmail = order.getDealer().getEmail();
                        String sellerEmail = order.getSeller().getEmail();

                        String subject = "Thông báo: Đơn hàng #" + orderID + " đã được đặt cọc";
                        String content = "Đơn hàng #" + orderID + " đã được người mua đặt cọc thành công.\n"
                                + "Hãy kiểm tra lại thông tin đơn hàng và chuẩn bị giao hàng.\n\n"
                                + "Trân trọng,\nOnline Pig Market";

                        try {
                            Email.sendEmail(buyerEmail, subject, content);
                            Email.sendEmail(sellerEmail, subject, content);
                        } catch (MessagingException | UnsupportedEncodingException e) {
                            Logger.getLogger(DepositOrderController.class.getName()).log(Level.SEVERE, null, e);
                        }

                        String msg = URLEncoder.encode("Đặt cọc thành công cho đơn hàng #" + orderID, StandardCharsets.UTF_8);
                        response.sendRedirect(baseQuery + "&msg=" + msg);
                    } else {
                        String msg = URLEncoder.encode("Không thể cập nhật trạng thái đơn hàng.", StandardCharsets.UTF_8);
                        response.sendRedirect(baseQuery + "&msg=" + msg);
                    }

                } else {
                    String msg = URLEncoder.encode("Bạn không có quyền đặt cọc đơn hàng này.", StandardCharsets.UTF_8);
                    response.sendRedirect(baseQuery + "&msg=" + msg);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(baseQuery);
            }
        } else {
            response.sendRedirect(baseQuery);
        }
    }
}
