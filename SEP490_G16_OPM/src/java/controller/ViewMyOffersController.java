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
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Farm;
import model.Page;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ViewMyOffersController", urlPatterns = {"/my-offers"})
public class ViewMyOffersController extends HttpServlet {

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
            out.println("<title>Servlet ViewMyOffersController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewMyOffersController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRoleID() != 4) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        String msg = request.getParameter("msg");
        if (msg != null && !msg.isEmpty()) {
            request.setAttribute("msg", msg);
        }

        int userId = user.getUserID();
        int pageNumber = 1;
        int pageSize = 5;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                pageNumber = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            pageNumber = 1;
        }

        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String farmId = request.getParameter("farmId");

        request.setAttribute("sort", sort);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("farmId", farmId);

        PigsOfferDAO offerDAO = new PigsOfferDAO();
        FarmDAO farmDAO = new FarmDAO();

        Page<PigsOffer> page = offerDAO.getOffersBySeller(userId, pageNumber, pageSize, sort, search, status, farmId);
        List<Farm> myFarms = farmDAO.getFarmsBySellerId(userId);

        request.setAttribute("myFarms", myFarms);
        request.setAttribute("page", page);
        request.setAttribute("currentPage", pageNumber);

        request.getRequestDispatcher("myoffers.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
