/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import dao.FarmDAO;
import dao.PigsOfferDAO;
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
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
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

        List<Farm> myFarms = farmDAO.getActiveFarmsBySellerId(userId);
        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("myFarms", myFarms);
        request.setAttribute("categories", categories);

        request.setAttribute("page", request.getParameter("page"));
        request.setAttribute("farmIdParam", request.getParameter("farmId"));
        request.setAttribute("search", request.getParameter("search"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("sort", request.getParameter("sort"));

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
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        try {
            int sellerId = user.getUserID();
            int farmId = Integer.parseInt(request.getParameter("farmId"));

            FarmDAO farmDAO = new FarmDAO();
            Farm farm = farmDAO.getFarmById(farmId);
            if (farm == null) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Trang trại này không tồn tại", "UTF-8"));
                return;
            } else if (farm.getSellerID() != sellerId) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Bạn không có quyền truy cập farm này", "UTF-8"));
                return;
            } else if (!farmDAO.isActiveFarm(farmId)) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Trang trại bạn chọn không còn hoạt động", "UTF-8"));
                return;
            }

            // Lấy dữ liệu từ form
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

            // Validate
            String nameError = Validation.validateOfferName(name);
            String breedError = Validation.validatePigBreed(pigBreed);
            String quantityError = Validation.validateQuantity(quantity, minQuantity);
            String priceError = Validation.validatePrices(retailPrice, totalOfferPrice, minDeposit);
            String dateError = Validation.validateDates(startDate, endDate);
            String descriptionError = Validation.validateOfferDescription(description);

            if (nameError != null || breedError != null || quantityError != null
                    || priceError != null || dateError != null || descriptionError != null) {

                // Set lỗi để hiển thị trên JSP
                request.setAttribute("error_name", nameError);
                request.setAttribute("error_breed", breedError);
                request.setAttribute("error_quantity", quantityError);
                request.setAttribute("error_price", priceError);
                request.setAttribute("error_date", dateError);
                request.setAttribute("error_description", descriptionError);

                // Giữ lại dữ liệu đã nhập
                request.setAttribute("name", name);
                request.setAttribute("categoryId", categoryId);
                request.setAttribute("farmId", farmId);
                request.setAttribute("pigBreed", pigBreed);
                request.setAttribute("quantity", quantity);
                request.setAttribute("minQuantity", minQuantity);
                request.setAttribute("minDeposit", minDeposit);
                request.setAttribute("retailPrice", retailPrice);
                request.setAttribute("totalOfferPrice", totalOfferPrice);
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("description", description);

                request.setAttribute("page", request.getParameter("page"));
                request.setAttribute("farmIdParam", request.getParameter("farmId"));
                request.setAttribute("search", request.getParameter("search"));
                request.setAttribute("status", request.getParameter("status"));
                request.setAttribute("sort", request.getParameter("sort"));

                // Load lại farm và category
                CategoryDAO categoryDAO = new CategoryDAO();
                request.setAttribute("myFarms", farmDAO.getActiveFarmsBySellerId(sellerId));
                request.setAttribute("categories", categoryDAO.getAllCategories());

                request.getRequestDispatcher("createpigsoffer.jsp").forward(request, response);
                return;
            }

            // Xử lý ảnh
            Part imagePart = request.getPart("image");
            ImageService imageService = new ImageService();
            String imageURL = imageService.upload(imagePart);
            if (imageURL == null) {
                request.setAttribute("error", "Không thể tải ảnh lên, vui lòng thử lại.");

                // Giữ lại dữ liệu và reload farm + category
                request.setAttribute("name", name);
                request.setAttribute("categoryId", categoryId);
                request.setAttribute("farmId", farmId);
                request.setAttribute("pigBreed", pigBreed);
                request.setAttribute("quantity", quantity);
                request.setAttribute("minQuantity", minQuantity);
                request.setAttribute("minDeposit", minDeposit);
                request.setAttribute("retailPrice", retailPrice);
                request.setAttribute("totalOfferPrice", totalOfferPrice);
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("description", description);

                CategoryDAO categoryDAO = new CategoryDAO();
                request.setAttribute("myFarms", farmDAO.getActiveFarmsBySellerId(sellerId));
                request.setAttribute("categories", categoryDAO.getAllCategories());

                request.getRequestDispatcher("createpigsoffer.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng offer
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

            // Tự động set trạng thái theo ngày bắt đầu
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            if (!startDate.after(currentDate)) {
                offer.setStatus("Available");
            } else {
                offer.setStatus("Upcoming");
            }
            offer.setNote(" Tạo ngày " + currentDate.toString());
            PigsOfferDAO offerDAO = new PigsOfferDAO();
            boolean success = offerDAO.createPigsOffer(offer);

            if (success) {
                String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
                String encodedMsg = java.net.URLEncoder.encode("Tạo chào bán thành công!", "UTF-8");
                response.sendRedirect("my-offers?farmId=" + farmId + "&search=" + encodedName + "&status=" + offer.getStatus() + "&msg=" + encodedMsg);
            } else {
                request.setAttribute("error", "Không thể lưu chào bán, vui lòng thử lại.");

                // Giữ lại dữ liệu và reload farm + category
                request.setAttribute("name", name);
                request.setAttribute("categoryId", categoryId);
                request.setAttribute("farmId", farmId);
                request.setAttribute("pigBreed", pigBreed);
                request.setAttribute("quantity", quantity);
                request.setAttribute("minQuantity", minQuantity);
                request.setAttribute("minDeposit", minDeposit);
                request.setAttribute("retailPrice", retailPrice);
                request.setAttribute("totalOfferPrice", totalOfferPrice);
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("description", description);

                CategoryDAO categoryDAO = new CategoryDAO();
                request.setAttribute("myFarms", farmDAO.getActiveFarmsBySellerId(sellerId));
                request.setAttribute("categories", categoryDAO.getAllCategories());

                request.getRequestDispatcher("createpigsoffer.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng kiểm tra lại dữ liệu.");

            // Load lại farm + category để tránh lỗi NullPointer trong JSP
            FarmDAO farmDAO = new FarmDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            request.setAttribute("myFarms", farmDAO.getFarmsBySellerId(user.getUserID()));
            request.setAttribute("categories", categoryDAO.getAllCategories());

            request.setAttribute("page", request.getParameter("page"));
            request.setAttribute("farmIdParam", request.getParameter("farmId"));
            request.setAttribute("search", request.getParameter("search"));
            request.setAttribute("status", request.getParameter("status"));
            request.setAttribute("sort", request.getParameter("sort"));

            request.getRequestDispatcher("createpigsoffer.jsp").forward(request, response);
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
