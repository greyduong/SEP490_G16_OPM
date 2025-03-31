/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import config.UploadConfig;
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
import java.util.UUID;
import model.PigsOffer;
import model.PigsOfferDAO;

/**
 *
 * @author duong
 */
@MultipartConfig
public class UpdatePigsOfferController extends HttpServlet {

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
            out.println("<title>Servlet EditPigsOfferController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditPigsOfferController at " + request.getContextPath() + "</h1>");
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
        String offerId = request.getParameter("offerId");
        if (offerId == null || offerId.isEmpty()) {
            response.getWriter().print("Offer ID is missing");
            return;
        }

        PigsOfferDAO dao = new PigsOfferDAO();
        PigsOffer offer = dao.getOfferById(Integer.parseInt(offerId));
        if (offer == null) {
            response.getWriter().print("Offer not found!");
            return;
        }

        request.setAttribute("offer", offer);
        request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
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
        String offerId = request.getParameter("offerId");
        PigsOfferDAO dao = new PigsOfferDAO();

        // Kiểm tra tồn tại trước khi update
        PigsOffer existingOffer = dao.getOfferById(Integer.parseInt(offerId));
        if (existingOffer == null) {
            response.getWriter().print("Offer not found!");
        } else {
            PigsOffer offer = new PigsOffer();
            offer.setOfferID(Integer.parseInt(offerId));
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

            // Đảm bảo thư mục tồn tại
            String uploadPath = UploadConfig.PIGS_UPLOAD_PATH;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Xử lý upload ảnh
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                // Lấy tên file, chống path traversal
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                fileName = UUID.randomUUID() + "_" + fileName;
                String filePath = uploadPath + File.separator + fileName;

                // Xóa file ảnh cũ nếu tồn tại
                String oldFileName = existingOffer.getImageURL();
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    File oldFile = new File(uploadPath + File.separator + oldFileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // Lưu file mới
                filePart.write(filePath);

                // Lưu tên file mới vào DB
                offer.setImageURL(fileName);
            } else {
                // Không upload mới => Giữ ảnh cũ
                offer.setImageURL(existingOffer.getImageURL());
            }

            // Xử lý ngày
            try {
                offer.setStartDate(Date.valueOf(request.getParameter("startDate")));
                offer.setEndDate(Date.valueOf(request.getParameter("endDate")));
            } catch (IllegalArgumentException e) {
                response.getWriter().println("Ngày không đúng định dạng yyyy-MM-dd");
                return;
            }

            offer.setStatus("Active");
            boolean check = dao.updatePigsOffer(offer);
            if (check) {
                response.sendRedirect("PigsOfferDetails?offerId=" + offer.getOfferID());
            } else {
                response.getWriter().print("Update Failed!");
            }
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
