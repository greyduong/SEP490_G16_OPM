/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FarmDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Farm;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "DeleteFarmController", urlPatterns = {"/deleteFarm"})
public class DeleteFarmController extends HttpServlet {

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
            out.println("<title>Servlet DeleteFarmController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteFarmController at " + request.getContextPath() + "</h1>");
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

        // Giữ các tham số truy vấn để không mất khi redirect
        String page = request.getParameter("page");
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        String baseRedirectURL = "ViewMyFarmsController"
                + (page != null ? "?page=" + page : "")
                + (sort != null ? "&sort=" + sort : "")
                + (search != null ? "&search=" + java.net.URLEncoder.encode(search, "UTF-8") : "")
                + (status != null ? "&status=" + status : "");

        String idParam = request.getParameter("id");
        if (idParam == null) {
            String msg = java.net.URLEncoder.encode("Thiếu ID trang trại để xóa", "UTF-8");
            response.sendRedirect(baseRedirectURL + "&msg=" + msg);
            return;
        }

        try {
            int farmId = Integer.parseInt(idParam);
            FarmDAO dao = new FarmDAO();
            Farm farm = dao.getFarmById(farmId);

            if (farm == null || farm.getSellerID() != user.getUserID()) {
                String msg = java.net.URLEncoder.encode("Không tìm thấy hoặc không có quyền xóa trang trại này", "UTF-8");
                response.sendRedirect(baseRedirectURL + "&msg=" + msg);
                return;
            }

            boolean deleted = dao.deleteOldFarm(farmId, user.getUserID());
            String msg = java.net.URLEncoder.encode(deleted
                    ? "Đã xóa trang trại thành công"
                    : "Xóa trang trại thất bại", "UTF-8");
            response.sendRedirect(baseRedirectURL + "&msg=" + msg);

        } catch (NumberFormatException e) {
            String msg = java.net.URLEncoder.encode("ID không hợp lệ", "UTF-8");
            response.sendRedirect(baseRedirectURL + "&msg=" + msg);
        }
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
