/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
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
import model.Cart;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ShoppingCartController", urlPatterns = {"/cart"})
public class ShoppingCartController extends HttpServlet {

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
            out.println("<title>Servlet ShoppingCartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShoppingCartController at " + request.getContextPath() + "</h1>");
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

        int userId = user.getUserID();

        // Lấy tham số phân trang, tìm kiếm, sắp xếp
        String indexParam = request.getParameter("page");
        int pageIndex = (indexParam != null) ? Integer.parseInt(indexParam) : 1;
        int pageSize = 3;

        String search = request.getParameter("search");
        String sort = request.getParameter("sort");

        CartDAO cartDAO = new CartDAO();
        PigsOfferDAO offerDAO = new PigsOfferDAO();

        // Lấy danh sách giỏ hàng có phân trang, search, sort
        List<Cart> cartList = cartDAO.getCartByUserIdWithFilter(userId, pageIndex, pageSize, search, sort);

        // Tổng số mục trong giỏ (phục vụ phân trang)
        int totalItems = cartDAO.countCartItemsByUserWithFilter(userId, search);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        request.setAttribute("cartList", cartList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("param", request.getParameterMap()); // giữ lại filter khi phân trang

        request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
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
