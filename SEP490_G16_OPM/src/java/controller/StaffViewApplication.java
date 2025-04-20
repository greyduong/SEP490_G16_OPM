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

@WebServlet(name = "StaffViewApplication", urlPatterns = {"/StaffViewApplication"})
public class StaffViewApplication extends HttpServlet {

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    private boolean isStaffOrManager(User user) {
        return user.getRoleID() == 2 || user.getRoleID() == 3;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isLoggedIn(session)) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!isStaffOrManager(user)) {
            response.sendRedirect("login-register.jsp?error=access-denied");
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applicationList = dao.getAllApplications();
        request.setAttribute("applicationList", applicationList);
        request.getRequestDispatcher("staffviewapplication.jsp").forward(request, response);
    }

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login-register.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Allows Staff and Manager to view all applications";
    }
}
