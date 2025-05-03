package controller;

import dao.FarmDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Farm;
import model.User;

@WebServlet(name = "DealerFarmsController", urlPatterns = {"/dealer-farms"})
public class DealerFarmsController extends HttpServlet {

    private static final int PAGE_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin người dùng
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRoleID() != 5) { // Chỉ Dealer mới được truy cập
            response.sendRedirect("login-register.jsp?error=access-denied");
            return;
        }

        int userId = user.getUserID();
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }

        FarmDAO farmDAO = new FarmDAO();
        int totalFarms = farmDAO.countAllFarms();
        int totalPages = (int) Math.ceil((double) totalFarms / PAGE_SIZE);
        List<Farm> farmList = farmDAO.getAllFarmsWithPagination((page - 1) * PAGE_SIZE, PAGE_SIZE);

        request.setAttribute("farmList", farmList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("dealer-farms.jsp").forward(request, response);
    }
}
