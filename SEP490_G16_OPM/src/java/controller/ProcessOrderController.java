package controller;

import dao.OrderDAO;
import dao.Validation;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProcessOrderController", urlPatterns = {"/process-order"})
public class ProcessOrderController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String orderIdStr = request.getParameter("orderId");
        String reason = request.getParameter("reason");

        try {
            int orderId = Integer.parseInt(orderIdStr);
            reason = reason != null ? reason.trim() : "";

            Validation validator = new Validation();
            if (validator.validateCancelReason(reason) != null) {
                session.setAttribute("msg", reason);
                response.sendRedirect("manage-orders");
                return;
            }

            OrderDAO orderDAO = new OrderDAO();

            if (!orderDAO.doesOrderExist(orderId)) {
                session.setAttribute("msg", "Đơn hàng không tồn tại hoặc đã bị xoá.");
                response.sendRedirect("manage-orders");
                return;
            }

            String status = orderDAO.getOrderStatus(orderId);
            if (!"Pending".equalsIgnoreCase(status)) {
                session.setAttribute("msg", "Chỉ có thể huỷ đơn hàng ở trạng thái Chờ xác nhận.");
                response.sendRedirect("manage-orders");
                return;
            }

            orderDAO.cancelOrderAndDeliveries(orderId, reason);
            session.setAttribute("msg", "Đơn hàng #" + orderId + " đã được huỷ thành công.");

        } catch (NumberFormatException e) {
            session.setAttribute("msg", "Mã đơn hàng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Có lỗi xảy ra khi xử lý huỷ đơn.");
        }

        response.sendRedirect("manage-orders");
    }
}
