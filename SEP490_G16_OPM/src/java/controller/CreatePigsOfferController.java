/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Date;
import model.PigsOffer;
import model.PigsOfferDAO;

/**
 *
 * @author duong
 */

// xử lý file upload qua form có enctype="multipart/form-data"
@MultipartConfig
public class CreatePigsOfferController extends HttpServlet {

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
            out.println("<title>Servlet CreatePigsOfferController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreatePigsOfferController at " + request.getContextPath() + "</h1>");
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
        PigsOffer offer = new PigsOffer();
        offer.setSellerID(1); // Hardcode hoặc lấy theo session user
        offer.setFarmID(1);
        offer.setCategoryID(1);
        offer.setName(request.getParameter("name"));
        offer.setPigBreed(request.getParameter("pigBreed"));
        offer.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        offer.setMinQuantity(Integer.parseInt(request.getParameter("minQuantity")));
        offer.setMinDeposit(Double.parseDouble(request.getParameter("minDeposit")));
        offer.setRetailPrice(Double.parseDouble(request.getParameter("retailPrice")));
        offer.setTotalOfferPrice(Double.parseDouble(request.getParameter("totalOfferPrice")));
        offer.setDescription(request.getParameter("description"));

        // Xử lý upload ảnh
        String uploadPath = getServletContext().getRealPath("/img/pigs");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Part filePart = request.getPart("imageFile");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // Lưu đường dẫn ảnh vào DB
        offer.setImageURL("img/pigs/" + fileName);

        // Xử lý ngày
        try {
            offer.setStartDate(Date.valueOf(request.getParameter("startDate")));
            offer.setEndDate(Date.valueOf(request.getParameter("endDate")));
        } catch (IllegalArgumentException e) {
            response.getWriter().println("Ngày không đúng định dạng yyyy-MM-dd");
            return;
        }

        offer.setStatus("Active");

        PigsOfferDAO dao = new PigsOfferDAO();
        boolean check = dao.createPigsOffer(offer);
        if (check) {
            response.sendRedirect("homepage.jsp");
        } else {
            response.getWriter().print("Create Failed!");
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
