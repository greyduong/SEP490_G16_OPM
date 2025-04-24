/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import dao.FarmDAO;
import dao.PigsOfferDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.sql.Date;
import java.util.List;
import model.Category;
import model.Farm;
import model.PigsOffer;
import model.User;
import service.ImageService;

/**
 *
 * @author duong
 */
@WebServlet(name = "CreatePigsOfferController", urlPatterns = {"/createOffer"})
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getUserID();
        FarmDAO farmDAO = new FarmDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        List<Farm> myFarms = farmDAO.getFarmsBySellerId(userId);
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("myFarms", myFarms);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("createpigsoffer.jsp").forward(request, response);
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

        int sellerId = user.getUserID();
        int farmId = Integer.parseInt(request.getParameter("farmId"));

        FarmDAO farmDAO = new FarmDAO();
        Farm farm = farmDAO.getFarmById(farmId);
        if (farm == null || farm.getSellerID() != sellerId) {
            response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Bạn không có quyền truy cập farm này", "UTF-8"));
            return;
        }

        String name = request.getParameter("name");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String pigBreed = request.getParameter("pigBreed");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int minQuantity = Integer.parseInt(request.getParameter("minQuantity"));
        double minDeposit = Double.parseDouble(request.getParameter("minDeposit"));
        double retailPrice = Double.parseDouble(request.getParameter("retailPrice"));
        double totalOfferPrice = Double.parseDouble(request.getParameter("totalOfferPrice"));
        Date startDate = Date.valueOf(request.getParameter("startDate"));
        Date endDate = Date.valueOf(request.getParameter("endDate"));
        String description = request.getParameter("description");

        Part imagePart = request.getPart("image");
        ImageService imageService = new ImageService();
        String imageURL = imageService.upload(imagePart);

        if (imageURL == null) {
            response.sendRedirect("createpigsoffer.jsp?msg=" + java.net.URLEncoder.encode("Không thể tải ảnh lên, vui lòng thử lại", "UTF-8"));
            return;
        }

        PigsOffer offer = new PigsOffer();
        offer.setName(name);
        offer.setCategoryID(categoryId);
        offer.setFarmID(farmId);
        offer.setSellerID(sellerId);
        offer.setPigBreed(pigBreed);
        offer.setQuantity(quantity);
        offer.setMinQuantity(minQuantity);
        offer.setMinDeposit(minDeposit);
        offer.setRetailPrice(retailPrice);
        offer.setTotalOfferPrice(totalOfferPrice);
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
        offer.setDescription(description);
        offer.setImageURL(imageURL);
        offer.setStatus("Available");

        PigsOfferDAO offerDAO = new PigsOfferDAO();
        offerDAO.createPigsOffer(offer);

        response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Tạo chào hàng thành công!", "UTF-8"));
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
