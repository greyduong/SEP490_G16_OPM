package controller;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageApplicationController", urlPatterns = {"/manage-application"})
public class ManageApplicationController extends HttpServlet {

    private boolean isAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        return user != null && (user.getRoleID() == 2 || user.getRoleID() == 3);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthorized(session)) {
            response.sendRedirect("login?error=access-denied");
            return;
        }

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        int page = 1;
        int pageSize = 5;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        ApplicationDAO dao = new ApplicationDAO();
        try {
            int totalRecords = dao.countApplicationsForManager(keyword, status);
            List<Application> applicationList = dao.getApplicationsForManager(keyword, status, sort, page, pageSize);

            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            request.setAttribute("applicationList", applicationList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("paramKeyword", keyword);
            request.setAttribute("paramStatus", status);
            request.setAttribute("paramSort", sort);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Không thể tải danh sách đơn.");
        }

        request.getRequestDispatcher("manage-application.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        

        String idRaw = request.getParameter("applicationID");
        String reply = request.getParameter("reply");
        String status = request.getParameter("status");

        if (idRaw == null || reply == null || status == null
                || reply.trim().isEmpty() || status.trim().isEmpty()) {
            session.setAttribute("errorMsg", "Vui lòng nhập đầy đủ thông tin.");
            response.sendRedirect("manage-application");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            ApplicationDAO dao = new ApplicationDAO();
            dao.updateApplicationStatus(id, reply.trim(), status.trim(), new java.sql.Timestamp(System.currentTimeMillis()));
            session.setAttribute("successMsg", "Xử lý đơn thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Lỗi khi xử lý đơn.");
        }

        response.sendRedirect("manage-application");
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị danh sách đơn đăng ký cho Staff/Manager với phân trang và lọc";
    }
}
