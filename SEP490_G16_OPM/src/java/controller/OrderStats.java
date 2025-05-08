/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.OrderStat;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "OrderStats", urlPatterns = {"/order-stats"})
public class OrderStats extends HttpServlet {

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
            out.println("<title>Servlet OrderStats</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderStats at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            User seller = (User) request.getSession().getAttribute("user");
            if (seller == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Chưa đăng nhập\"}");
                return;
            }

            String type = request.getParameter("type");
            String yearStr = request.getParameter("year");
            String monthStr = request.getParameter("month");
            String dayStr = request.getParameter("day");

            if (type == null || type.trim().isEmpty()) {
                type = "month";
            }

            Integer year = null, month = null, day = null;
            try {
                if (yearStr != null && yearStr.matches("\\d+")) {
                    year = Integer.parseInt(yearStr);
                }
                if (monthStr != null && monthStr.matches("\\d+")) {
                    month = Integer.parseInt(monthStr);
                }
                if (dayStr != null && dayStr.matches("\\d+")) {
                    day = Integer.parseInt(dayStr);
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Lỗi parse số: " + e.getMessage());
            }

            int sellerId = seller.getUserID();
            OrderDAO dao = new OrderDAO();
            List<OrderStat> stats = dao.getOrderStatsFlexible(type, year, month, day, sellerId);

            // Log
            System.out.printf("📊 [PARAMS] type=%s, year=%s, month=%s, day=%s, sellerId=%d%n",
                    type, year, month, day, sellerId);
            System.out.println("📊 [Kết quả]:");
            stats.forEach(s -> System.out.printf(" - %s: %d%n", s.getLabel(), s.getTotal()));

            // Chuyển về JSON và trả về
            Gson gson = new Gson();
            out.print(gson.toJson(stats));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
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
