package controller;

import model.Application;
import dao.ApplicationDAO;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import service.ImageService;

@WebServlet(name = "CreateApplication", urlPatterns = {"/CreateApplication"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class CreateApplication extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("createapplication.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("home?error=access-denied");
            return;
        }

        int userID = user.getUserID();
        String content = request.getParameter("content");
        String status = "Đang chờ xử lý";
        Date sentAt = new Date();
        Date processingDate = null;
        String filePath = null;

        // ✅ Validate content
        if (content == null || content.trim().length() < 10) {
            request.setAttribute("msg", "Nội dung đơn phải dài ít nhất 10 ký tự.");
            request.getRequestDispatcher("createapplication.jsp").forward(request, response);
            return;
        }

        // ✅ Handle image upload via ImgBB
        Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String contentType = imagePart.getContentType();

            if (!contentType.startsWith("image/")) {
                request.setAttribute("imageURLError", "Chỉ cho phép tệp hình ảnh (JPG, PNG).");
                request.getRequestDispatcher("createapplication.jsp").forward(request, response);
                return;
            }

            if (imagePart.getSize() > 10 * 1024 * 1024) {
                request.setAttribute("imageURLError", "Ảnh không được vượt quá 10MB.");
                request.getRequestDispatcher("createapplication.jsp").forward(request, response);
                return;
            }

            // 🟢 Upload lên ImgBB và lấy URL
            ImageService imageService = new ImageService();
            filePath = imageService.upload(imagePart); // URL ảnh online
        }

        // ✅ Create and save application
        Application newApp = new Application(userID, content.trim(), null, status, sentAt, processingDate, filePath);
        ApplicationDAO dao = new ApplicationDAO();
        boolean isCreated = dao.createApplication(newApp);

        if (isCreated) {
            request.getSession().setAttribute("success", "Đơn đã được gửi thành công!");
            response.sendRedirect("application");
        } else {
            request.setAttribute("msg", "Không thể gửi đơn. Vui lòng thử lại sau.");
            request.getRequestDispatcher("createapplication.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý tạo đơn đề nghị với ảnh";
    }
}
