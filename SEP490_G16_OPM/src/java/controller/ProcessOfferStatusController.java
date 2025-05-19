package controller;

import dao.PigsOfferDAO;
import model.Email;
import model.PigsOffer;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcessOfferStatusController", urlPatterns = {"/process-offer"})
public class ProcessOfferStatusController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        String note = request.getParameter("note");

        String page = request.getParameter("page");
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String farmId = request.getParameter("farmId");
        String status = request.getParameter("status");

        String queryParams = String.format("page=%s&sort=%s&search=%s&farmId=%s&status=%s",
                page == null ? "" : page,
                sort == null ? "" : sort,
                search == null ? "" : search,
                farmId == null ? "" : farmId,
                status == null ? "" : status
        );

        if (idParam == null || action == null) {
            response.sendRedirect("manage-offers?msg=" + URLEncoder.encode("Thiếu dữ liệu yêu cầu", StandardCharsets.UTF_8) + "&" + queryParams);
            return;
        }

        int offerId = Integer.parseInt(idParam);
        PigsOfferDAO dao = new PigsOfferDAO();
        PigsOffer offer = dao.getOfferById(offerId);

        if (offer == null || offer.getSeller() == null) {
            response.sendRedirect("manage-offers?msg=" + URLEncoder.encode("Không tìm thấy chào bán hoặc người bán", StandardCharsets.UTF_8) + "&" + queryParams);
            return;
        }

        String msg;
        try {
            if ("ban".equalsIgnoreCase(action)) {
                if (note == null || note.trim().isEmpty()) {
                    response.sendRedirect("manage-offers?msg=" + URLEncoder.encode("Vui lòng nhập lý do ban", StandardCharsets.UTF_8) + "&" + queryParams);
                    return;
                }

                offer.setStatus("Banned");
                offer.setNote(note.trim());
                boolean success = dao.updateStatusAndNote(offer);

                if (success) {
                    try {
                        dao.cancelPendingOrdersByOfferId(offer.getOfferID(), note.trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msg = "Đã ban chào bán nhưng lỗi khi huỷ các đơn đang chờ xác nhận.";
                    }

                    msg = "Đã cấm chào bán";
                    Email.sendEmail(
                            offer.getSeller().getEmail(),
                            "Chào bán bị cấm",
                            "Xin chào " + offer.getSeller().getFullName()
                            + ",\n\nChào bán \"" + offer.getName() + "\" đã bị cấm bởi quản trị viên vì lý do:\n"
                            + "👉 " + note.trim()
                            + "\n\nToàn bộ đơn hàng ở trạng thái **chờ xác nhận** của chào bán này cũng đã bị **huỷ**."
                            + "\n\nNếu bạn cần hỗ trợ, vui lòng liên hệ quản trị viên."
                            + "\n\nTrân trọng,\nOnline Pig Market"
                    );
                } else {
                    msg = "Ban chào bán thất bại";
                }

            } else if ("unban".equalsIgnoreCase(action)) {

                if (offer.getFarm() == null || "Banned".equalsIgnoreCase(offer.getFarm().getStatus())) {
                    msg = "Không thể mở cấm chào bán vì trang trại đang bị cấm.";
                } else {
                    offer.setStatus("Unavailable");
                    offer.setNote("Được mở ban");
                    boolean success = dao.updateStatusAndNote(offer);

                    if (success) {
                        msg = "Đã mở ban chào bán";
                        Email.sendEmail(
                                offer.getSeller().getEmail(),
                                "Chào bán được mở lại",
                                "Xin chào " + offer.getSeller().getFullName()
                                + ",\n\nChào bán \"" + offer.getName() + "\" đã được mở ban và có thể được tiếp tục cập nhật trạng thái bán."
                                + "\n\nTrân trọng,\nOnline Pig Market"
                        );
                    } else {
                        msg = "Mở ban thất bại";
                    }
                }

            } else {
                msg = "Hành động không hợp lệ";
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "Có lỗi xảy ra khi xử lý";
        }

        response.sendRedirect("manage-offers?msg=" + URLEncoder.encode(msg, StandardCharsets.UTF_8) + "&" + queryParams);
    }

    @Override
    public String getServletInfo() {
        return "Process offer ban/unban controller";
    }
}
