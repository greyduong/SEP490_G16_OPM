package listener;

import dao.OrderDAO;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import model.Email;
import model.Order;

public class ProcessOrderTask implements Runnable {

    @Override
    public void run() {
        try {
            var db = new OrderDAO();
            var expired = db.getExpiredOrders();
            db.cancelOrders(expired, "Hủy đơn do quá hạn xác nhận");
            Logger.getLogger(ProcessOfferTask.class.getName()).info("Đã hủy %s đơn do quá hạn xác nhận".formatted(expired.size()));
            expired.forEach(order -> {
                // sendCancelOrderEmail(order, "Hết hạn");
            });
            var overProcess = db.getOverProcessedDateOrders();
            db.cancelOrders(overProcess, "Hủy đơn do quá thời gian xử lý");
            overProcess.forEach(order -> {
                // sendCancelOrderEmail(order, "Quá hạn xử lý");
            });
            Logger.getLogger(ProcessOfferTask.class.getName()).info("Đã hủy %s đơn do quá thời gian xử lý".formatted(overProcess.size()));
        } catch(Exception e) {

        }
    }

    public void sendCancelOrderEmail(Order order, String reason) {
        String dealerEmail = order.getDealer().getEmail();
        String sellerEmail = order.getSeller().getEmail();
        int orderID = order.getOrderID();
        String subject = "Đơn hàng #" + order.getOrderID() + " đã bị hủy";
        String templateDealer = """
                         Đơn hàng #%s của bạn đã bị hủy.
                         Lý do: %s
                         
                         Bạn có thể đặt đơn hàng khác hoặc liên hệ lại người bán nếu cần thêm thông tin.
                         
                         Trân trọng,
                         Online Pig Market.
                         """;

        String templateSeller = """
                         Đơn hàng #%s đã bị hủy.
                         Lý do: %s

                         Trân trọng,
                         Online Pig Market.
                         """;
        try {
            Email.sendEmail(dealerEmail, subject, templateDealer.formatted(
                    orderID,
                    reason
            ));
            Email.sendEmail(sellerEmail, subject, templateSeller.formatted(
                    orderID,
                    reason
            ));
        } catch (MessagingException | UnsupportedEncodingException e) {
            java.util.logging.Logger.getLogger(ProcessOrderTask.class.getName()).severe(e.getMessage());
        }
    }
}
