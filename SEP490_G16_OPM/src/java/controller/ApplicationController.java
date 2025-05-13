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
        if (session == null) return false;
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
        String keyword = request.getParameter("keyword");

        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applications;

        if (keyword != null && !keyword.trim().isEmpty()) {
            applications = dao.searchByUserAndKeyword(user.getUserID(), keyword.trim());
        } else {
            applications = dao.getByUserId(user.getUserID());
        }

        request.setAttribute("applicationList", applications);
        request.getRequestDispatcher("viewapplication.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthorized(session)) {
            response.sendRedirect("home?error=access-denied");
            return;
        }

        try {
            int applicationId = Integer.parseInt(request.getParameter("applicationId"));
            String action = request.getParameter("action"); // approve or reject
            String reply = request.getParameter("reply");

            Application application = new Application();
            application.setApplicationID(applicationId);
            application.setStatus(action);
            application.setReply(reply);
            application.setProcessingDate(new Date());

            ApplicationDAO dao = new ApplicationDAO();
            boolean success = dao.updateApplication(application);

            if (success) {
                session.setAttribute("successMsg", "Cập nhật đơn đăng ký thành công.");
            } else {
                session.setAttribute("errorMsg", "Cập nhật đơn đăng ký thất bại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Đã xảy ra lỗi trong quá trình xử lý đơn đăng ký.");
        }

        // Always redirect to refresh the list and avoid form re-submission
        response.sendRedirect("application");
    }

    @Override
    public String getServletInfo() {
        return "Application processing controller for Staff and Manager";
    }
}
