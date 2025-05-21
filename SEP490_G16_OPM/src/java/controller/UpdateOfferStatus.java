/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PigsOfferDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "UpdateOfferStatus", urlPatterns = {"/updateOfferStatus"})
public class UpdateOfferStatus extends HttpServlet {

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
            out.println("<title>Servlet UpdateOfferStatus</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOfferStatus at " + request.getContextPath() + "</h1>");
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

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        try {
            int offerId = Integer.parseInt(request.getParameter("id"));
            String newStatus = request.getParameter("status");

            PigsOfferDAO offerDAO = new PigsOfferDAO();
            PigsOffer offer = offerDAO.getOfferById(offerId);

            if (offer == null || offer.getSellerID() != user.getUserID()) {
                response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Không tìm thấy chào bán.", "UTF-8"));
                return;
            }

            if (offer.getSellerID() != user.getUserID()) {
                response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Bạn không có quyền chỉnh sửa chào bán này.", "UTF-8"));
                return;
            }

            if ("Unavailable".equals(offer.getStatus())) {
                response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Không thể đặt trạng thái 'Không Hoạt động' vì chào bán đã không hoạt động", "UTF-8"));
                return;
            }

            if ("Banned".equalsIgnoreCase(offer.getStatus())) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Chào bán đã bị cấm và không thể chỉnh sửa.", "UTF-8"));
                return;
            }

            // Không cho phép đặt "Available" nếu điều kiện không hợp lệ
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            if ("Available".equalsIgnoreCase(newStatus)) {
                if (offer.getStartDate().after(today)) {
                    response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Không thể đặt trạng thái 'Hoạt động' vì chưa đến ngày bắt đầu.", "UTF-8"));
                    return;
                }
                if (offer.getEndDate().before(today)) {
                    response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Không thể đặt trạng thái 'Hoạt động' vì chào bán đã kết thúc.", "UTF-8"));
                    return;
                }
                if (offer.getQuantity() <= 0) {
                    response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Không thể đặt trạng thái 'Hoạt động' vì số lượng bằng 0.", "UTF-8"));
                    return;
                }
            }

            // Cập nhật trạng thái
            offer.setStatus(newStatus);
            offerDAO.updateStatus(offer);

            String msg = "Đã cập nhật trạng thái chào bán thành công!";
            response.sendRedirect(String.format("my-offers?page=%s&farmId=%s&search=%s&status=%s&sort=%s&msg=%s",
                    request.getParameter("page"),
                    request.getParameter("farmId"),
                    URLEncoder.encode(request.getParameter("search"), "UTF-8"),
                    request.getParameter("status"),
                    request.getParameter("sort"),
                    URLEncoder.encode(msg, "UTF-8")
            ));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("my-offers?msg=" + URLEncoder.encode("Có lỗi xảy ra khi cập nhật trạng thái.", "UTF-8"));
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
