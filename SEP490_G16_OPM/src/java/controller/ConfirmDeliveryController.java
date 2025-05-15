package controller;

import dao.DeliveryDAO;
import dao.OrderDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "ConfirmDeliveryController", urlPatterns = {"/confirm-delivery"})
public class ConfirmDeliveryController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int deliveryID = Integer.parseInt(request.getParameter("deliveryID"));

            DeliveryDAO deliveryDAO = new DeliveryDAO();
            OrderDAO orderDAO = new OrderDAO();

            int orderID = deliveryDAO.getOrderIdByDeliveryId(deliveryID);
            int dealerID = deliveryDAO.getDealerIdByDeliveryId(deliveryID);

            if (user == null || user.getRoleID() != 5 || user.getUserID() != dealerID) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode("Bạn không có quyền xác nhận giao hàng này.", "UTF-8"));
                return;
            }

            String deliveryStatus = deliveryDAO.getDeliveryStatusById(deliveryID);
            String orderStatus = deliveryDAO.getOrderStatusByDeliveryId(deliveryID);

            if (!"Pending".equalsIgnoreCase(deliveryStatus)) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode("Chỉ được xác nhận giao hàng khi đang ở trạng thái 'Chờ xác nhận'.", "UTF-8"));
                return;
            }

            if (!"Processing".equalsIgnoreCase(orderStatus)) {
                response.sendRedirect("view-order-detail?id=" + orderID + "&msg="
                        + URLEncoder.encode("Chỉ được xác nhận khi đơn hàng đang ở trạng thái 'Đang xử lý'.", "UTF-8"));
                return;
            }

            boolean updated = deliveryDAO.confirmDelivery(deliveryID);
            String msg;

            if (updated) {
                orderDAO.updateOrderNote(orderID, "Đơn hàng đang được xử lý.");
                Order order = orderDAO.getOrderById(orderID);
                int deliveredQuantity = deliveryDAO.getTotalDeliveredQuantity(orderID);
                double deliveredPrice = deliveryDAO.getTotalDeliveredPrice(orderID);

                UserDAO userDAO = new UserDAO();
                User seller = userDAO.getUserById(order.getSellerID());
                User dealer = userDAO.getUserById(order.getDealerID());

                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedPrice = formatter.format(deliveredPrice) + " VND";

                String subject = "Xác nhận giao hàng - Đơn hàng #" + orderID;

                String contentForSeller = "Xin chào " + seller.getFullName() + ",\n\n"
                        + "Giao hàng (Mã giao hàng: #" + deliveryID + ") trong đơn hàng #" + orderID + " đã được người mua xác nhận.\n"
                        + "- Số lượng đã giao: " + deliveredQuantity + " con\n"
                        + "- Tổng giá trị đã giao: " + formattedPrice + "\n\n"
                        + "Vui lòng kiểm tra trong hệ thống.\n\nOnline Pig Market.";

                String contentForDealer = "Xin chào " + dealer.getFullName() + ",\n\n"
                        + "Bạn đã xác nhận thành công giao hàng (Mã giao hàng: #" + deliveryID + ") cho đơn hàng #" + orderID + ".\n"
                        + "- Số lượng xác nhận: " + deliveredQuantity + " con\n"
                        + "- Tổng giá trị xác nhận: " + formattedPrice + "\n\n"
                        + "Cảm ơn bạn đã sử dụng hệ thống Online Pig Market.";

                try {
                    Email.sendEmail(seller.getEmail(), subject, contentForSeller);
                    Email.sendEmail(dealer.getEmail(), subject, contentForDealer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (deliveredQuantity >= order.getQuantity() && Math.abs(deliveredPrice - order.getTotalPrice()) < 0.001) {
                    orderDAO.updateOrderStatus(orderID, "Completed");
                    orderDAO.updateOrderNote(orderID, "Đơn hàng đã hoàn tất.");
                    msg = "Giao hàng đã được xác nhận. Đơn hàng đã hoàn tất.";

                    String completedSubject = "Đơn hàng #" + orderID + " đã hoàn tất";

                    String completedForSeller = "Xin chào " + seller.getFullName() + ",\n\n"
                            + "Tất cả giao hàng trong đơn hàng #" + orderID + " đã được xác nhận.\n"
                            + "Đơn hàng hiện đã hoàn tất.\n\n"
                            + "Cảm ơn bạn đã đồng hành cùng Online Pig Market.";

                    String completedForDealer = "Xin chào " + dealer.getFullName() + ",\n\n"
                            + "Bạn đã xác nhận đủ mọi giao hàng trong đơn hàng #" + orderID + ".\n"
                            + "Đơn hàng hiện đã hoàn tất.\n\n"
                            + "Cảm ơn bạn đã sử dụng hệ thống Online Pig Market.";

                    try {
                        Email.sendEmail(seller.getEmail(), completedSubject, completedForSeller);
                        Email.sendEmail(dealer.getEmail(), completedSubject, completedForDealer);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    msg = "Giao hàng đã được xác nhận.";
                }
            } else {
                msg = "Xác nhận giao hàng thất bại.";
            }

            response.sendRedirect("view-order-detail?id=" + orderID + "&msg=" + URLEncoder.encode(msg, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myorders?msg=" + URLEncoder.encode("Đã xảy ra lỗi khi xác nhận giao hàng.", "UTF-8"));
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles delivery confirmation and notification.";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>ConfirmDeliveryController</title></head>");
            out.println("<body><h1>Accessed via GET, use POST instead</h1></body></html>");
        }
    }
}
