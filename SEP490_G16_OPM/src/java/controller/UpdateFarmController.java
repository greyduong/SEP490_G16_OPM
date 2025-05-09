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
import java.net.URLEncoder;
import model.Farm;
import model.User;
import service.ImageService;

/**
 *
 * @author duong
 */
@WebServlet(name = "UpdateFarmController", urlPatterns = {"/updateFarm"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class UpdateFarmController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

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
            String msg = URLEncoder.encode("Thiếu ID trang trại", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg + "&" + queryParams);
            return;
        }

        int id = Integer.parseInt(idParam);
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getFarmById(id);

        if (farm == null || farm.getSellerID() != user.getUserID()) {
            String msg = URLEncoder.encode("Không tìm thấy hoặc không có quyền sửa trang trại này", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg + "&" + queryParams);
            return;
        }

        request.setAttribute("farm", farm);
        request.setAttribute("page", page);
        request.setAttribute("sort", sort);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.getRequestDispatcher("updatefarm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int farmId = Integer.parseInt(request.getParameter("farmId"));
        String farmName = request.getParameter("farmName");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String statusOption = request.getParameter("statusOption");

        String nameError = Validation.validateFarmName(farmName);
        String locationError = Validation.validateFarmLocation(location);
        String descriptionError = Validation.validateFarmDescription(description);

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
            farm.setStatus(statusOption);

            request.setAttribute("farm", farm);
            request.setAttribute("nameError", nameError);
            request.setAttribute("locationError", locationError);
            request.setAttribute("descriptionError", descriptionError);
            request.setAttribute("page", page);
            request.setAttribute("sort", sort);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.getRequestDispatcher("updatefarm.jsp").forward(request, response);
            return;
        }

        farmName = Validation.formatToTitleCase(farmName);
        location = Validation.formatToTitleCase(location);

        FarmDAO dao = new FarmDAO();
        Farm oldFarm = dao.getFarmById(farmId);

        // Upload ảnh nếu người dùng chọn file mới
        Part imagePart = request.getPart("image");
        String imageURL = oldFarm.getImageURL(); // giữ nguyên nếu không chọn ảnh mới
        if (imagePart != null && imagePart.getSize() > 0) {
            ImageService imageService = new ImageService();
            String uploaded = imageService.upload(imagePart);
            if (uploaded != null) {
                imageURL = uploaded;
            }
        }

        Farm farm = new Farm();
        farm.setFarmID(farmId);
        farm.setFarmName(farmName);
        farm.setLocation(location);
        farm.setDescription(description);
        farm.setImageURL(imageURL);
        farm.setStatus(statusOption);

        boolean noChange = oldFarm.getFarmName().equals(farmName)
                && oldFarm.getLocation().equals(location)
                && oldFarm.getDescription().equals(description)
                && ((oldFarm.getImageURL() == null && imageURL == null)
                || (oldFarm.getImageURL() != null && oldFarm.getImageURL().equals(imageURL)))
                && oldFarm.getStatus().equals(statusOption);

        if (noChange) {
            String msg = URLEncoder.encode("Thông tin cập nhật giống như cũ, không cần thay đổi!", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg + "&" + queryParams);
            return;
        }

        if ("Inactive".equalsIgnoreCase(statusOption) && !dao.canDeactivateFarm(farmId)) {
            String msg = URLEncoder.encode("Không thể dừng hoạt động vì vẫn còn offer đang mở hoặc đơn hàng chưa hoàn thành.", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg + "&" + queryParams);
            return;
        }

        boolean success = dao.updateOldFarm(farm);
        if (success) {
            String msg = URLEncoder.encode("Cập nhật trang trại thành công", "UTF-8");
            response.sendRedirect("my-farms?msg=" + msg + "&" + queryParams);
        } else {
            request.setAttribute("farm", farm);
            request.setAttribute("msg", "Cập nhật không thành công");
            request.setAttribute("page", page);
            request.setAttribute("sort", sort);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.getRequestDispatcher("updatefarm.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
