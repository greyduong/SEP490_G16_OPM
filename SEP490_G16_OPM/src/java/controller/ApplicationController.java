package controller;

import dao.ApplicationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import model.Application;
import model.User;

@WebServlet(name = "ApplicationController", urlPatterns = {"/application"})
public class ApplicationController extends HttpServlet {

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        return user != null && (user.getRoleID() == 4 || user.getRoleID() == 5); // 2 = Staff, 3 = Manager
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthorized(session)) {
            response.sendRedirect("home?error=access-denied");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Lấy tham số lọc
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String sortByDate = request.getParameter("sortByDate");

        // Chuyển trạng thái từ tiếng Anh sang tiếng Việt để khớp DB
        if ("Pending".equals(status)) {
            status = "Đang chờ xử lý";
        } else if ("Confirmed".equals(status)) {
            status = "Đã phê duyệt";
        } else if ("Rejected".equals(status)) {
            status = "Đã từ chối";
        } else if ("Canceled".equals(status)) {
            status = "Đã hủy";
        }

        // Phân trang
        int page = 1;
        int pageSize = 5; // số lượng đơn mỗi trang
        try {
            page = Integer.parseInt(request.getParameter("page"));
            if (page < 1) {
                page = 1;
            }
        } catch (Exception e) {
            page = 1;
        }

        ApplicationDAO dao = new ApplicationDAO();

        // Lấy danh sách đơn theo trang
        List<Application> applications = dao.getApplicationsByFilterPaged(
                user.getUserID(), keyword, status, sortByDate, page, pageSize
        );

        // Tổng số đơn để tính tổng số trang
        int total = dao.countApplicationsByFilter(user.getUserID(), keyword, status);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        // Trả về lại các giá trị gốc để giữ filter
        request.setAttribute("applicationList", applications);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", request.getParameter("keyword"));  // giữ giá trị gốc
        request.setAttribute("status", request.getParameter("status"));    // giữ giá trị gốc tiếng Anh
        request.setAttribute("sortByDate", sortByDate);

        request.getRequestDispatcher("viewapplication.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Application processing controller for Staff and Manager";
    }
}
