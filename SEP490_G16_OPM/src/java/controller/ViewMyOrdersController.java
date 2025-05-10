/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
import model.Order;
import model.Page;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ViewMyOrdersController", urlPatterns = {"/myorders"})
public class ViewMyOrdersController extends HttpServlet {

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
            out.println("<title>Servlet ViewMyOrdersController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewMyOrdersController at " + request.getContextPath() + "</h1>");
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
        User currentUser = (User) session.getAttribute("user");

        int userId = currentUser.getUserID();
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");
        int pageIndex = 1;

        try {
            if (pageRaw != null) {
                pageIndex = Integer.parseInt(pageRaw);
            }
        } catch (NumberFormatException ignored) {
        }
        OrderDAO orderDAO = new OrderDAO();
        int totalData = orderDAO.countOrdersByBuyerWithFilter(userId, search, status);
        List<Order> orders = orderDAO.getOrdersByBuyerWithFilter(userId, search, status, sort, pageIndex, PAGE_SIZE);

        Page<Order> page = new Page<>();
        page.setPageNumber(pageIndex);
        page.setPageSize(PAGE_SIZE);
        page.setTotalData(totalData);
        page.setTotalPage((int) Math.ceil((double) totalData / PAGE_SIZE));
        page.setData(orders);

        request.setAttribute("page", page);

        String msg = request.getParameter("msg");
        if (msg != null) {
            request.setAttribute("msg", msg);
        }

        request.getRequestDispatcher("myorders.jsp").forward(request, response);
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
