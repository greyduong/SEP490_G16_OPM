package controller;

import dao.ApplicationDAO;
import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Application;

@WebServlet(name = "ProcessApplication", urlPatterns = {"/ProcessApplication"})
public class ProcessApplication extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int applicationId = Integer.parseInt(request.getParameter("applicationId"));
            String action = request.getParameter("action"); // approve or reject
            String replyText = request.getParameter("reply");

            ApplicationDAO dao = new ApplicationDAO();
            Application application = dao.getApplicationById(applicationId);

            if (application != null) {
                if ("approve".equalsIgnoreCase(action)) {
                    application.setStatus("Approved");
                } else if ("reject".equalsIgnoreCase(action)) {
                    application.setStatus("Rejected");
                }

                // ✅ Always update reply if present
                application.setReply(replyText);
                application.setProcessingDate(new Date());

                boolean updated = dao.updateApplication(application);

                if (updated) {
                    request.getSession().setAttribute("successMsg", "Application processed successfully.");
                } else {
                    request.getSession().setAttribute("errorMsg", "Failed to update application.");
                }
            } else {
                request.getSession().setAttribute("errorMsg", "Application not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMsg", "An error occurred while processing the application.");
        }

        // ✅ Redirect to staff view
        response.sendRedirect("StaffViewApplication");
    }

    @Override
    public String getServletInfo() {
        return "Handles application approval, rejection, and reply for staff.";
    }
}
