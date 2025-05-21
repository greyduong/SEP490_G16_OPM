package controller;

import dao.CategoryDAO;
import dao.FarmDAO;
import dao.PigsOfferDAO;
import dao.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.PigsOffer;
import model.User;
import service.ImageService;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "UpdatePigsOfferController", urlPatterns = {"/updateOffer"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class UpdatePigsOfferController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        int offerId = Integer.parseInt(request.getParameter("id"));
        PigsOfferDAO offerDAO = new PigsOfferDAO();
        PigsOffer offer = offerDAO.getOfferById(offerId);

        if (offer == null || offer.getSellerID() != user.getUserID()) {
            response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Không thể truy cập chào bán này.", "UTF-8"));
            return;
        }

        // Lấy lại các tham số lọc và phân trang
        request.setAttribute("page", request.getParameter("page"));
        request.setAttribute("farmIdParam", request.getParameter("farmId"));
        request.setAttribute("search", request.getParameter("search"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("sort", request.getParameter("sort"));

        request.setAttribute("offer", offer);
        request.setAttribute("categories", new CategoryDAO().getAllCategories());
        request.setAttribute("myFarms", new FarmDAO().getActiveFarmsBySellerId(user.getUserID()));
        request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        // Lấy lại các tham số lọc và phân trang
        String pageParam = request.getParameter("page");
        String farmIdParam = request.getParameter("farmId");
        String searchParam = request.getParameter("search");
        String statusParam = request.getParameter("status");
        String sortParam = request.getParameter("sort");

        try {
            int sellerId = user.getUserID();
            int offerId = Integer.parseInt(request.getParameter("offerId"));

            PigsOfferDAO offerDAO = new PigsOfferDAO();
            PigsOffer existing = offerDAO.getOfferById(offerId);

            if (existing == null || existing.getSellerID() != sellerId) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Không thể chỉnh sửa chào bán này.", "UTF-8"));
                return;
            }

            int farmId = Integer.parseInt(request.getParameter("farmId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String pigBreed = request.getParameter("pigBreed");
            String description = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int minQuantity = Integer.parseInt(request.getParameter("minQuantity"));
            double minDeposit = Double.parseDouble(request.getParameter("minDeposit"));
            double retailPrice = Double.parseDouble(request.getParameter("retailPrice"));
            double totalOfferPrice = Double.parseDouble(request.getParameter("totalOfferPrice"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            String status = request.getParameter("status");

            if ("Banned".equalsIgnoreCase(existing.getStatus())) {
                response.sendRedirect("my-offers?msg=" + java.net.URLEncoder.encode("Chào bán đã bị cấm và không thể chỉnh sửa.", "UTF-8"));
                return;
            }

            // Validate
            String nameError = Validation.validateOfferName(name);
            String breedError = Validation.validatePigBreed(pigBreed);
            String quantityError = Validation.validateQuantity(quantity, minQuantity);
            String priceError = Validation.validatePrices(retailPrice, totalOfferPrice, minDeposit);
            String dateError = Validation.validateDates(startDate, endDate);
            String descriptionError = Validation.validateOfferDescription(description);

            if (nameError != null || breedError != null || quantityError != null
                    || priceError != null || dateError != null || descriptionError != null) {

                request.setAttribute("error_name", nameError);
                request.setAttribute("error_breed", breedError);
                request.setAttribute("error_quantity", quantityError);
                request.setAttribute("error_price", priceError);
                request.setAttribute("error_date", dateError);
                request.setAttribute("error_description", descriptionError);

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(offerId);
                offer.setName(name);
                offer.setCategoryID(categoryId);
                offer.setFarmID(farmId);
                offer.setSellerID(sellerId);
                offer.setPigBreed(pigBreed);
                offer.setDescription(description);
                offer.setQuantity(quantity);
                offer.setMinQuantity(minQuantity);
                offer.setMinDeposit(minDeposit);
                offer.setRetailPrice(retailPrice);
                offer.setTotalOfferPrice(totalOfferPrice);
                offer.setStartDate(startDate);
                offer.setEndDate(endDate);
                offer.setImageURL(existing.getImageURL());
                offer.setStatus(existing.getStatus());

                request.setAttribute("offer", offer);
                request.setAttribute("categories", new CategoryDAO().getAllCategories());
                request.setAttribute("myFarms", new FarmDAO().getActiveFarmsBySellerId(sellerId));

                // Trả lại các tham số lọc để dùng lại trên form
                request.setAttribute("page", pageParam);
                request.setAttribute("farmIdParam", farmIdParam);
                request.setAttribute("search", searchParam);
                request.setAttribute("status", statusParam);
                request.setAttribute("sort", sortParam);

                request.setAttribute("error", "Vui lòng kiểm tra lại thông tin.");
                request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
                return;
            }

            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            if ("Available".equalsIgnoreCase(status)) {
                if (startDate.after(today)) {
                    request.setAttribute("error", "Không thể đặt trạng thái 'Hoạt động' vì ngày bắt đầu vẫn chưa đến.");
                } else if (endDate.before(today)) {
                    request.setAttribute("error", "Không thể đặt trạng thái 'Hoạt động' vì chào bán đã kết thúc.");
                } else if (quantity <= 0) {
                    request.setAttribute("error", "Không thể đặt trạng thái 'Hoạt động' vì số lượng bằng 0.");
                } else if (minQuantity > quantity) {
                     request.setAttribute("error", "Số lượng tối thiểu không được lớn hơn tổng số lượng.");
                }
                if (request.getAttribute("error") != null) {
                    // Giữ lại dữ liệu đã nhập
                    PigsOffer offer = new PigsOffer();
                    offer.setOfferID(offerId);
                    offer.setName(name);
                    offer.setCategoryID(categoryId);
                    offer.setFarmID(farmId);
                    offer.setSellerID(sellerId);
                    offer.setPigBreed(pigBreed);
                    offer.setDescription(description);
                    offer.setQuantity(quantity);
                    offer.setMinQuantity(minQuantity);
                    offer.setMinDeposit(minDeposit);
                    offer.setRetailPrice(retailPrice);
                    offer.setTotalOfferPrice(totalOfferPrice);
                    offer.setStartDate(startDate);
                    offer.setEndDate(endDate);
                    offer.setImageURL(existing.getImageURL());
                    offer.setStatus(existing.getStatus());

                    request.setAttribute("offer", offer);
                    request.setAttribute("categories", new CategoryDAO().getAllCategories());
                    request.setAttribute("myFarms", new FarmDAO().getActiveFarmsBySellerId(sellerId));

                    request.setAttribute("page", request.getParameter("page"));
                    request.setAttribute("farmIdParam", request.getParameter("farmId"));
                    request.setAttribute("search", request.getParameter("search"));
                    request.setAttribute("status", request.getParameter("status"));
                    request.setAttribute("sort", request.getParameter("sort"));

                    request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
                    return;
                }
            }

            // Xử lý ảnh nếu có thay đổi
            Part imagePart = request.getPart("image");
            String imageURL = existing.getImageURL();
            if (imagePart != null && imagePart.getSize() > 0) {
                ImageService imageService = new ImageService();
                String uploadedURL = imageService.upload(imagePart);
                if (uploadedURL != null) {
                    imageURL = uploadedURL;
                }
            }

            PigsOffer updated = new PigsOffer();
            updated.setOfferID(offerId);
            updated.setName(name);
            updated.setCategoryID(categoryId);
            updated.setFarmID(farmId);
            updated.setSellerID(sellerId);
            updated.setPigBreed(pigBreed);
            updated.setDescription(description);
            updated.setQuantity(quantity);
            updated.setMinQuantity(minQuantity);
            updated.setMinDeposit(minDeposit);
            updated.setRetailPrice(retailPrice);
            updated.setTotalOfferPrice(totalOfferPrice);
            updated.setStartDate(startDate);
            updated.setEndDate(endDate);
            updated.setImageURL(imageURL);
            updated.setStatus(status);
            updated.setNote("Cập nhập ngày " + today.toString());

            boolean success = offerDAO.updatePigsOffer(updated);

            if (success) {
                String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
                String encodedMsg = java.net.URLEncoder.encode("Cập nhật thành công!", "UTF-8");

                String redirectURL = String.format("my-offers?page=%s&farmId=%s&search=%s&status=%s&sort=%s&msg=%s",
                        pageParam != null ? pageParam : "1",
                        farmIdParam != null ? farmIdParam : "",
                        encodedName,
                        statusParam != null ? statusParam : "",
                        sortParam != null ? sortParam : "",
                        encodedMsg
                );

                response.sendRedirect(redirectURL);
            } else {
                request.setAttribute("error", "Không thể cập nhật. Vui lòng thử lại sau.");
                request.setAttribute("offer", updated);
                request.setAttribute("categories", new CategoryDAO().getAllCategories());
                request.setAttribute("myFarms", new FarmDAO().getActiveFarmsBySellerId(sellerId));

                request.setAttribute("page", pageParam);
                request.setAttribute("farmIdParam", farmIdParam);
                request.setAttribute("search", searchParam);
                request.setAttribute("status", statusParam);
                request.setAttribute("sort", sortParam);

                request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
            request.getRequestDispatcher("updatepigsoffer.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Cập nhật chào bán";
    }
}
