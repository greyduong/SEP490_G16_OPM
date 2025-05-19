package controller;

import dao.UserDAO;
import dao.Validation;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Lấy dữ liệu từ form
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // ✅ Kiểm tra rỗng
        if (Validation.isEmpty(fullname) || Validation.isEmpty(email)
                || Validation.isEmpty(phone) || Validation.isEmpty(address)) {
            session.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            response.sendRedirect("profile");
            return;
        }

        // ✅ Kiểm tra định dạng email
        if (!Validation.isValidEmail(email)) {
            session.setAttribute("error", "Email không hợp lệ.");
            response.sendRedirect("profile");
            return;
        }

        // ✅ Kiểm tra định dạng fullname
        if (!Validation.isValidFullName(fullname)) {
            session.setAttribute("error", "Họ tên chỉ được chứa chữ cái và ít nhất 2 ký tự.");
            response.sendRedirect("profile");
            return;
        }

        // Gán giá trị mới
        currentUser.setFullName(fullname);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);

        // Cập nhật DB
        UserDAO userDAO = new UserDAO();
        boolean updateSuccess = userDAO.updateUser(currentUser);

        if (updateSuccess) {
            session.setAttribute("user", currentUser); // Cập nhật lại session
            session.setAttribute("success", "Cập nhật thông tin thành công.");
        } else {
            session.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
        }

        response.sendRedirect("profile");
    }
}
