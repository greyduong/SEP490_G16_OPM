/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ApplicationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import model.Application;

/**
 *
 * @author tuan
 */
@WebServlet(name = "ApplicationController", urlPatterns = {"/application"})
public class ApplicationController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ApplicationController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApplicationController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applications = dao.getAllApplications();
        request.setAttribute("applicationList", applications);
        request.getRequestDispatcher("viewapplication.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                request.getSession().setAttribute("successMsg", "Application updated successfully.");
            } else {
                request.getSession().setAttribute("errorMsg", "Failed to update application.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMsg", "Error occurred while processing the application.");
        }

        // Always redirect to refresh the list and avoid form re-submission
        response.sendRedirect("application");
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
