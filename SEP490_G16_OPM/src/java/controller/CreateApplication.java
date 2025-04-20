package controller;

import model.Application;
import dao.ApplicationDAO;
import model.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "CreateApplication", urlPatterns = {"/CreateApplication"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class CreateApplication extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        request.getRequestDispatcher("createapplication.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login-register.jsp");
            return;
        }

        int userID = user.getUserID();
        String content = request.getParameter("content");
        String status = "Pending";
        Date sentAt = new Date();
        Date processingDate = null;
        String filePath = null;

        // ✅ Validate content
        if (content == null || content.trim().length() < 10) {
            request.setAttribute("msg", "Nội dung đơn phải dài ít nhất 10 ký tự.");
            request.getRequestDispatcher("createapplication.jsp").forward(request, response);
            return;
        }

        // ✅ Handle file upload
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String contentType = filePart.getContentType();

            // ✅ MIME type validation
            if (!contentType.matches("application/pdf|application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document|image/png|image/jpeg")) {
                request.setAttribute("msg", "Chỉ cho phép file PDF, Word, PNG hoặc JPG.");
                request.getRequestDispatcher("createapplication.jsp").forward(request, response);
                return;
            }

            if (filePart.getSize() > 10 * 1024 * 1024) {
                request.setAttribute("msg", "File không được vượt quá 10MB.");
                request.getRequestDispatcher("createapplication.jsp").forward(request, response);
                return;
            }

            // ✅ Save to img/applications/
            String uploadDir = getServletContext().getRealPath("/") + "img" + File.separator + "applications";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fullPath = uploadDir + File.separator + fileName;
            filePart.write(fullPath);
            filePath = "img/applications/" + fileName;
        }

        // ✅ Create application object
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
        return "Xử lý tạo đơn đề nghị";
    }
}
