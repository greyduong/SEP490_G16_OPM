package controller;

import dao.DeliveryDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dao.Validation;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "CreateDeliveryController", urlPatterns = {"/create-delivery"})
public class CreateDeliveryController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int orderID;
        try {
            orderID = Integer.parseInt(request.getParameter("orderID"));
        } catch (NumberFormatException e) {
            session.setAttribute("msg", "ID đơn hàng không hợp lệ.");
            response.sendRedirect("customer-order-details?id=0");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrderById(orderID);
        if (order == null) {
            session.setAttribute("msg", "Đơn hàng không tồn tại hoặc đã bị xóa.");
            response.sendRedirect("customer-order-details?id=" + orderID);
            return;
        }

        if (!orderDAO.isOrderOwnedBySeller(orderID, user.getUserID())) {
            session.setAttribute("msg", "Bạn không có quyền tạo giao hàng cho đơn này.");
            response.sendRedirect("customer-order-details?id=" + orderID);
            return;
        }

        if (!"Deposited".equals(order.getStatus()) && !"Processing".equals(order.getStatus())) {
            session.setAttribute("msg", "Chỉ có thể tạo giao hàng khi đơn hàng đang ở trạng thái 'Đã đặt cọc' hoặc 'Đang xử lý'.");
            response.sendRedirect("customer-order-details?id=" + orderID + "&openCreateDelivery=true");
            return;
        }

        try {
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String quantityStr = request.getParameter("quantity");
            String totalPriceStr = request.getParameter("totalPrice");
            String comments = request.getParameter("comments");
            int sellerId = Integer.parseInt(request.getParameter("sellerID"));
            int dealerId = Integer.parseInt(request.getParameter("dealerID"));

            session.setAttribute("prevRecipient", recipientName);
            session.setAttribute("prevPhone", phone);
            session.setAttribute("prevQuantity", quantityStr);
            session.setAttribute("prevTotalPrice", totalPriceStr);
            session.setAttribute("prevComment", comments);

            int quantity = 0;
            double totalPrice = 0;
            boolean hasError = false;

            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                session.setAttribute("quantityError", "Số lượng không hợp lệ.");
                hasError = true;
            }

            try {
                totalPrice = Double.parseDouble(totalPriceStr);
            } catch (NumberFormatException e) {
                session.setAttribute("priceError", "Tổng giá không hợp lệ.");
                hasError = true;
            }
            
            if (quantity == 0 && totalPrice == 0) {
                session.setAttribute("msg", "Số lượng và tổng giá không được đồng thời bằng 0!");
                hasError = true;
            }

            DeliveryDAO deliveryDAO = new DeliveryDAO();

            int deliveredQty = deliveryDAO.getTotalQuantityByStatuses(orderID);
            double deliveredTotal = deliveryDAO.getTotalPriceByStatuses(orderID);

            int remainingQty = order.getQuantity() - deliveredQty;
            double remainingPrice = order.getTotalPrice() - deliveredTotal;

            if ("Completed".equals(order.getStatus()) || (remainingQty <= 0 && remainingPrice <= 0)) {
                session.setAttribute("msg", "Đơn hàng đã được giao xong, không thể tạo!");
                response.sendRedirect("customer-order-details?id=" + orderID + "&openCreateDelivery=true");
                return;
            }

            String recipientError = Validation.validateRecipientName(recipientName);
            if (recipientError != null) {
                session.setAttribute("recipientError", recipientError);
                hasError = true;
            }

            String phoneError = Validation.validatePhone(phone);
            if (phoneError != null) {
                session.setAttribute("phoneError", phoneError);
                hasError = true;
            }

            if (quantity > 0) {
                String quantityError = Validation.validateDeliveryQuantity(quantity, remainingQty);
                if (quantityError != null) {
                    session.setAttribute("quantityError", quantityError);
                    hasError = true;
                }
            }

            if (totalPrice > 0) {
                String priceError = Validation.validateDeliveryPrice(totalPrice, remainingPrice);
                if (priceError != null) {
                    session.setAttribute("priceError", priceError);
                    hasError = true;
                }
            }

            String commentError = Validation.validateDeliveryComment(comments);
            if (commentError != null) {
                session.setAttribute("commentError", commentError);
                hasError = true;
            }

            if (hasError) {
                response.sendRedirect("customer-order-details?id=" + orderID + "&openCreateDelivery=true");
                return;
            }

            session.removeAttribute("recipientError");
            session.removeAttribute("quantityError");
            session.removeAttribute("priceError");
            session.removeAttribute("commentError");
            session.removeAttribute("phoneError");

            int deliveryID = deliveryDAO.createDelivery(orderID, sellerId, dealerId, recipientName.trim(), phone.trim(), quantity, totalPrice, comments);

            session.removeAttribute("prevRecipient");
            session.removeAttribute("prevPhone");
            session.removeAttribute("prevQuantity");
            session.removeAttribute("prevTotalPrice");
            session.removeAttribute("prevComment");

            if (deliveryID > 0) {
                if (!"Completed".equals(order.getStatus())) {
                    orderDAO.updateOrderStatus(orderID, "Processing");
                }

                orderDAO.updateOrderNote(orderID, "Đơn hàng đang được xử lý.");

                UserDAO userDAO = new UserDAO();
                User seller = userDAO.getUserById(sellerId);
                User dealer = userDAO.getUserById(dealerId);

                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedPrice = formatter.format(totalPrice) + " VND";

                String subject = "Thông báo: Đơn hàng #" + orderID + " có giao hàng mới";

                String contentForDealer = "Xin chào " + dealer.getFullName() + ",\n\n"
                        + "Người bán đã tạo một giao hàng mới cho đơn hàng #" + orderID + ".\n"
                        + "- Mã giao hàng: #" + deliveryID + "\n"
                        + "- Người nhận: " + recipientName + "\n"
                        + "- SĐT người nhận: " + phone + "\n"
                        + "- Số lượng: " + quantity + "\n"
                        + "- Tổng giá trị: " + formattedPrice + "\n"
                        + "- Ghi chú: " + comments + "\n\n"
                        + "Vui lòng đăng nhập để xác nhận giao hàng.";

                String contentForSeller = "Xin chào " + seller.getFullName() + ",\n\n"
                        + "Bạn đã tạo thành công một giao hàng mới cho đơn hàng #" + orderID + ".\n"
                        + "- Mã giao hàng: #" + deliveryID + "\n"
                        + "- Người nhận: " + recipientName + "\n"
                        + "- SĐT người nhận: " + phone + "\n"
                        + "- Số lượng: " + quantity + "\n"
                        + "- Tổng giá trị: " + formattedPrice + "\n"
                        + "- Ghi chú: " + comments + "\n\n"
                        + "Đang chờ người mua xác nhận.";

                try {
                    Email.sendEmail(dealer.getEmail(), subject, contentForDealer);
                    Email.sendEmail(seller.getEmail(), subject, contentForSeller);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.setAttribute("msg", "Tạo giao hàng thành công. Đang chờ người mua xác nhận.");
            }

            response.sendRedirect("customer-order-details?id=" + orderID);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Đã xảy ra lỗi hệ thống.");
            response.sendRedirect("customer-order-details?id=" + orderID);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles delivery creation with full field validation.";
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
            out.println("<html><head><title>CreateDeliveryController</title></head>");
            out.println("<body><h1>Accessed via GET, use POST instead</h1></body></html>");
        }
    }
}
