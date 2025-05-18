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
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "AddToCartController", urlPatterns = {"/AddToCartController"})
public class AddToCartController extends HttpServlet {

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
            out.println("<title>Servlet AddToCartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddToCartController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int userId = user.getUserID();
            int offerId = Integer.parseInt(request.getParameter("offerId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            PigsOfferDAO pigsOfferDAO = new PigsOfferDAO();
            PigsOffer pigsOffer = pigsOfferDAO.getOfferById(offerId);

            if (pigsOffer == null || !"Available".equalsIgnoreCase(pigsOffer.getStatus())) {
                session.setAttribute("msg", "Chào bán đã ngưng bán hoặc không tồn tại!");
                response.sendRedirect("home");
                return;
            }

            String status = pigsOffer.getStatus();
            if ("Unavailable".equalsIgnoreCase(status) || "Upcoming".equalsIgnoreCase(status) || "Banned".equalsIgnoreCase(status)) {
                request.setAttribute("error", "Chào bán này hiện không thể đặt hàng do đang ngưng bán hoặc bị cấm.");
                request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
                return;
            }

            if (quantity < pigsOffer.getMinQuantity() || quantity > pigsOffer.getQuantity()) {
                session.setAttribute("msg", "Số lượng không phù hợp!");
                response.sendRedirect("home");
                return;
            }

            CartDAO cartDAO = new CartDAO();
            cartDAO.addToCart(userId, offerId, quantity);

            // Lấy tên offer để search
            String offerName = pigsOffer.getName();

            // Redirect sang giỏ hàng với search theo tên offer vừa thêm
            String redirectUrl = "cart?search=" + java.net.URLEncoder.encode(offerName, "UTF-8");
            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("msg", "Dữ liệu nhập không hợp lệ.");
            response.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra trong quá trình xử lý.");
        }
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
