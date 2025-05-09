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

        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applications = dao.getAllApplications();
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
                session.setAttribute("successMsg", "Application updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to update application.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Error occurred while processing the application.");
        }

        // Always redirect to refresh the list and avoid form re-submission
        response.sendRedirect("application");
    }

    @Override
    public String getServletInfo() {
        return "Application processing controller for Staff and Manager";
    }
}
