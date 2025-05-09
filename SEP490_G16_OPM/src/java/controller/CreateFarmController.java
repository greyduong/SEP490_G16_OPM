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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Farm;
import model.User;
import service.ImageService;

/**
 *
 * @author duong
 */
@WebServlet(name = "CreateFarmController", urlPatterns = {"/createFarm"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CreateFarmController extends HttpServlet {

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
            out.println("<title>Servlet CreateFarmController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateFarmController at " + request.getContextPath() + "</h1>");
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

        request.getRequestDispatcher("createfarm.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String farmName = request.getParameter("farmName");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        // Validate từng trường
        String nameError = Validation.validateFarmName(farmName);
        String locationError = Validation.validateFarmLocation(location);
        String descriptionError = Validation.validateFarmDescription(description);

        if (nameError != null || locationError != null || descriptionError != null) {
            request.setAttribute("farmName", farmName);
            request.setAttribute("location", location);
            request.setAttribute("description", description);

            request.setAttribute("nameError", nameError);
            request.setAttribute("locationError", locationError);
            request.setAttribute("descriptionError", descriptionError);

            request.getRequestDispatcher("createfarm.jsp").forward(request, response);
            return;
        }

        // Format Title Case
        farmName = Validation.formatToTitleCase(farmName);
        location = Validation.formatToTitleCase(location);

        // Upload ảnh nếu có
        Part imagePart = request.getPart("image");
        String imageUrl = null;
        if (imagePart != null && imagePart.getSize() > 0) {
            ImageService imageService = new ImageService();
            imageUrl = imageService.upload(imagePart);
        }

        Farm farm = new Farm();
        farm.setFarmName(farmName);
        farm.setLocation(location);
        farm.setDescription(description);
        farm.setSellerID(user.getUserID());
        farm.setImageURL(imageUrl);

        FarmDAO farmDAO = new FarmDAO();
        if (farmDAO.createNewFarm(farm)) {
            String msg = java.net.URLEncoder.encode("Tạo trang trại thành công", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg);
        } else {
            request.setAttribute("msg", "Tạo trang trại không thành công");
            request.getRequestDispatcher("createfarm.jsp").forward(request, response);
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
