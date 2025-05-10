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
@WebServlet(name = "OrdersRequestController", urlPatterns = {"/orders-request"})
public class OrdersRequestController extends HttpServlet {

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
            out.println("<title>Servlet OrdersRequestController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrdersRequestController at " + request.getContextPath() + "</h1>");
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

        int sellerId = user.getUserID();

        // Nhận tham số lọc
        String farmIdRaw = request.getParameter("farmId");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");

        Integer farmId = null;
        int pageIndex = 1;

        try {
            if (farmIdRaw != null && !farmIdRaw.isEmpty()) {
                farmId = Integer.parseInt(farmIdRaw);
            }
        } catch (NumberFormatException ignored) {
        }

        try {
            if (pageRaw != null) {
                pageIndex = Integer.parseInt(pageRaw);
            }
        } catch (NumberFormatException ignored) {
        }

        int pageSize = 10;

        // Gọi DAO
        OrderDAO orderDAO = new OrderDAO();
        int totalData = orderDAO.countPendingOrdersBySellerWithFilter(sellerId, farmId, search);
        List<Order> orderList = orderDAO.getPendingOrdersBySellerWithFilter(
                sellerId, farmId, search, sort, pageIndex, pageSize
        );

        // Tạo Page object
        Page<Order> page = new Page<>();
        page.setPageNumber(pageIndex);
        page.setPageSize(pageSize);
        page.setTotalData(totalData);
        page.setData(orderList);
        int totalPage = (int) Math.ceil((double) totalData / pageSize);
        page.setTotalPage(totalPage);

        request.setAttribute("page", page);

        // Lấy danh sách trang trại để lọc
        FarmDAO farmDAO = new FarmDAO();
        List<Farm> farmList = farmDAO.getActiveFarmsBySellerId(sellerId);
        request.setAttribute("farmList", farmList);

        request.getRequestDispatcher("orderrequestpage.jsp").forward(request, response);
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
