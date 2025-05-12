/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FarmDAO;
import dao.OrderDAO;
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
import model.Order;
import model.Page;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "CustomerOrderController", urlPatterns = {"/customer-orders"})
public class CustomerOrderController extends HttpServlet {

    private static final int PAGE_SIZE = 10;

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
            out.println("<title>Servlet CustomerOrderPageController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerOrderPageController at " + request.getContextPath() + "</h1>");
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

        if (user == null || user.getRoleID() != 4) { // Chỉ seller mới được xem
            response.sendRedirect("login-register.jsp");
            return;
        }

        int sellerId = user.getUserID();
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String farmIdRaw = request.getParameter("farmId");
        String pageRaw = request.getParameter("page");

        Integer farmId = null;
        if (farmIdRaw != null && !farmIdRaw.trim().isEmpty()) {
            try {
                farmId = Integer.parseInt(farmIdRaw);
            } catch (NumberFormatException ignored) {
            }
        }

        int pageIndex = 1;
        try {
            if (pageRaw != null) {
                pageIndex = Integer.parseInt(pageRaw);
            }
        } catch (NumberFormatException ignored) {
        }

        OrderDAO orderDAO = new OrderDAO();

        int totalData = orderDAO.countOrdersExcludingPendingWithFilter(sellerId, farmId, search, status);
        List<Order> orders = orderDAO.getOrdersExcludingPendingWithFilter(sellerId, farmId, search, status, sort, pageIndex, PAGE_SIZE);

        Page<Order> page = new Page<>();
        page.setPageNumber(pageIndex);
        page.setPageSize(PAGE_SIZE);
        page.setTotalData(totalData);
        page.setTotalPage((int) Math.ceil((double) totalData / PAGE_SIZE));
        page.setData(orders);

        // Gửi lên JSP
        request.setAttribute("page", page);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("farmId", farmId);

        // Load farm list để lọc
        FarmDAO farmDAO = new FarmDAO();
        List<Farm> farmList = farmDAO.getActiveFarmsBySellerId(sellerId);
        request.setAttribute("farmList", farmList);

        request.getRequestDispatcher("customerorderpage.jsp").forward(request, response);
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
        doGet(request, response);
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
