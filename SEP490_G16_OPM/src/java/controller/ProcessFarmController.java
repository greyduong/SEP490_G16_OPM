/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FarmDAO;
import dao.PigsOfferDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import model.Email;
import model.Farm;

/**
 *
 * @author duong
 */
@WebServlet(name = "ProcessFarmController", urlPatterns = {"/process-farm"})
public class ProcessFarmController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        String page = request.getParameter("page");
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");

        String queryParams = String.format("page=%s&sort=%s&search=%s",
                page == null ? "" : page,
                sort == null ? "" : sort,
                search == null ? "" : search
        );

        if (idParam == null || action == null) {
            response.sendRedirect("pending-farms?msg=" + URLEncoder.encode("Thiếu dữ liệu yêu cầu", StandardCharsets.UTF_8) + "&" + queryParams);
            return;
        }

        int farmId = Integer.parseInt(idParam);
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getFarmWithSellerById(farmId);

        if (farm == null || farm.getSeller() == null) {
            response.sendRedirect("pending-farms?msg=" + URLEncoder.encode("Không tìm thấy trang trại hoặc người bán", StandardCharsets.UTF_8) + "&" + queryParams);
            return;
        }

        String msg;
        try {
            if ("approve".equalsIgnoreCase(action)) {
                farm.setStatus("Active");
                farm.setNote("Đã phê duyệt");
                boolean success = dao.updateStatusAndNote(farm);

                if (success) {
                    msg = "Đã xác nhận thành công";
                    Email.sendEmail(
                            farm.getSeller().getEmail(),
                            "Trang trại đã được phê duyệt",
                            "Xin chào " + farm.getSeller().getFullName()
                            + ",\n\nTrang trại \"" + farm.getFarmName() + "\" của bạn đã được phê duyệt và đang hoạt động trên hệ thống."
                            + "\n\nTrân trọng,\nOnline Pig Market"
                    );
                } else {
                    msg = "Xác nhận thất bại";
                }

            } else if ("reject".equalsIgnoreCase(action)) {
                String note = request.getParameter("note");
                if (note == null || note.trim().isEmpty()) {
                    response.sendRedirect("pending-farms?msg=" + URLEncoder.encode("Vui lòng nhập lý do từ chối", StandardCharsets.UTF_8) + "&" + queryParams);
                    return;
                }

                farm.setStatus("Cancel");
                farm.setNote(note.trim());
                boolean success = dao.updateStatusAndNote(farm);

                if (success) {
                    msg = "Đã từ chối trang trại";
                    Email.sendEmail(
                            farm.getSeller().getEmail(),
                            "Trang trại bị từ chối",
                            "Xin chào " + farm.getSeller().getFullName()
                            + ",\n\nTrang trại \"" + farm.getFarmName() + "\" của bạn đã bị từ chối với lý do:\n\n"
                            + note.trim()
                            + "\n\nVui lòng kiểm tra lại và tạo lại yêu cầu mới nếu cần."
                            + "\n\nTrân trọng,\nOnline Pig Market"
                    );
                } else {
                    msg = "Từ chối thất bại";
                }

            } else if ("ban".equalsIgnoreCase(action)) {

                String note = request.getParameter("note");
                if (note == null || note.trim().isEmpty()) {
                    response.sendRedirect("manage-farms?msg=" + URLEncoder.encode("Vui lòng nhập lý do cấm hoạt động", StandardCharsets.UTF_8) + "&" + queryParams);
                    return;
                }

                farm.setStatus("Banned");
                farm.setNote(note.trim());

                boolean success = dao.updateStatusAndNote(farm);

                if (success) {

                    try {
                        dao.banOffersByFarmId(farmId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msg = "Đã cấm trang trại nhưng lỗi khi cập nhật chào bán";
                    }

                    msg = "Đã cấm hoạt động trang trại";
                    Email.sendEmail(
                            farm.getSeller().getEmail(),
                            "Thông báo: Trang trại và các chào bán bị cấm hoạt động",
                            "Xin chào " + farm.getSeller().getFullName()
                            + ",\n\nChúng tôi xin thông báo rằng trang trại \"" + farm.getFarmName() + "\" của bạn đã bị **cấm hoạt động** với lý do sau:\n"
                            + "👉 " + note.trim()
                            + "\n\nToàn bộ các **chào bán** thuộc trang trại này cũng đã bị **tạm ngưng** với trạng thái 'Bị cấm'."
                            + "\n\nNếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với quản trị viên hệ thống để được hỗ trợ."
                            + "\n\nTrân trọng,\nBan quản trị Online Pig Market"
                    );
                } else {
                    msg = "Cấm hoạt động thất bại";
                }
            } else if ("reactivate".equalsIgnoreCase(action)) {
                farm.setStatus("Active");
                farm.setNote("Được kích hoạt lại");
                boolean success = dao.updateStatusAndNote(farm);

                if (success) {

                    try {
                        dao.unbanOffersByFarmId(farmId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        msg = "Trang trại được mở lại nhưng lỗi khi cập nhật chào bán.";
                    }

                    msg = "Đã cho phép hoạt động lại trang trại";
                    Email.sendEmail(
                            farm.getSeller().getEmail(),
                            "Thông báo: Trang trại đã được khôi phục hoạt động",
                            "Xin chào " + farm.getSeller().getFullName()
                            + ",\n\nTrang trại \"" + farm.getFarmName() + "\" của bạn đã được **khôi phục trạng thái hoạt động** bởi quản lý hệ thống."
                            + "\n\nCác **chào bán trước đó bị cấm** cũng đã được chuyển sang trạng thái 'Ngưng bán'."
                            + "\nBạn có thể đăng nhập hệ thống và cập nhật lại các chào bán nếu cần."
                            + "\n\nTrân trọng,\nBan quản trị Online Pig Market"
                    );
                } else {
                    msg = "Kích hoạt lại thất bại";
                }
            } else {
                msg = "Hành động không hợp lệ";
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "Có lỗi khi gửi email hoặc xử lý dữ liệu";
        }

        String redirectPage = "approve".equalsIgnoreCase(action) || "reject".equalsIgnoreCase(action) ? "pending-farms" : "manage-farms";
        response.sendRedirect(redirectPage + "?msg=" + URLEncoder.encode(msg, StandardCharsets.UTF_8) + "&" + queryParams);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
