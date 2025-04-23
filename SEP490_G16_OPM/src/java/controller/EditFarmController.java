/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FarmDAO;
import dao.Validation;
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
@WebServlet(name = "EditFarmController", urlPatterns = {"/editFarm"})
public class EditFarmController extends HttpServlet {

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
            out.println("<title>Servlet EditFarmController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditFarmController at " + request.getContextPath() + "</h1>");
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

        // Preserve query params
        String page = request.getParameter("page");
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        String queryParams = String.format("page=%s&sort=%s&search=%s&status=%s",
                page == null ? "" : page,
                sort == null ? "" : sort,
                search == null ? "" : search,
                status == null ? "" : status
        );

        String idParam = request.getParameter("id");
        if (idParam == null) {
            String msg = java.net.URLEncoder.encode("Thiếu ID trang trại", "UTF-8");
            response.sendRedirect("ViewMyFarmsController?msg=" + msg + "&" + queryParams);
            return;
        }

        int id = Integer.parseInt(idParam);
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getFarmById(id);

        if (farm == null || farm.getSellerID() != user.getUserID()) {
            String msg = java.net.URLEncoder.encode("Không tìm thấy hoặc không có quyền sửa trang trại này", "UTF-8");
            response.sendRedirect("ViewMyFarmsController?msg=" + msg + "&" + queryParams);
            return;
        }

        request.setAttribute("farm", farm);
        request.getRequestDispatcher("editfarm.jsp").forward(request, response);
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

        if (user == null || user.getRoleID() != 4) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        int farmId = Integer.parseInt(request.getParameter("farmId"));
        String farmName = request.getParameter("farmName");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        String nameError = Validation.validateFarmName(farmName);
        String locationError = Validation.validateFarmLocation(location);
        String descriptionError = Validation.validateFarmDescription(description);

        // Preserve query params
        String page = request.getParameter("page");
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        String queryParams = String.format("page=%s&sort=%s&search=%s&status=%s",
                page == null ? "" : page,
                sort == null ? "" : sort,
                search == null ? "" : search,
                status == null ? "" : status
        );

        if (nameError != null || locationError != null || descriptionError != null) {
            Farm farm = new Farm();
            farm.setFarmID(farmId);
            farm.setFarmName(farmName);
            farm.setLocation(location);
            farm.setDescription(description);

            request.setAttribute("page", page);
            request.setAttribute("sort", sort);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("farm", farm);
            request.setAttribute("nameError", nameError);
            request.setAttribute("locationError", locationError);
            request.setAttribute("descriptionError", descriptionError);
            request.getRequestDispatcher("editfarm.jsp").forward(request, response);
            return;
        }

        farmName = Validation.formatToTitleCase(farmName);
        location = Validation.formatToTitleCase(location);

        Farm farm = new Farm();
        farm.setFarmID(farmId);
        farm.setFarmName(farmName);
        farm.setLocation(location);
        farm.setDescription(description);

        FarmDAO dao = new FarmDAO();
        Farm oldFarm = dao.getFarmById(farmId);

        if (oldFarm.getFarmName().equals(farm.getFarmName())
                && oldFarm.getLocation().equals(farm.getLocation())
                && oldFarm.getDescription().equals(farm.getDescription())) {
            String msg = java.net.URLEncoder.encode("Thông tin cập nhật giống như cũ, không cần thay đổi!", "UTF-8");
            response.sendRedirect("ViewMyFarmsController?msg=" + msg + "&" + queryParams);
            return;
        }

        boolean success = dao.updateOldFarm(farm);
        if (success) {
            String msg = java.net.URLEncoder.encode("Cập nhật trang trại thành công", "UTF-8");
            response.sendRedirect("ViewMyFarmsController?msg=" + msg + "&" + queryParams);
        } else {
            request.setAttribute("page", page);
            request.setAttribute("sort", sort);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("msg", "Cập nhật không thành công");
            request.setAttribute("farm", farm);
            request.getRequestDispatcher("editfarm.jsp").forward(request, response);

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
