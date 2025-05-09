package controller;

import dao.FarmDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Farm;
import model.User;

@WebServlet(name = "DealerFarmsController", urlPatterns = {"/farms"})
public class DealerFarmsController extends HttpServlet {

    private static final int PAGE_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        

        // Lấy tham số trang
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        // Lấy từ khóa tìm kiếm
        String search = request.getParameter("search");
        if (search == null) {
            search = "";
        }

        // DAO xử lý
        FarmDAO farmDAO = new FarmDAO();
        int totalFarms = farmDAO.countSearchAllFarms(search);
        int totalPages = (int) Math.ceil((double) totalFarms / PAGE_SIZE);
        List<Farm> farmList = farmDAO.searchAllFarmsWithPagination(search, (page - 1) * PAGE_SIZE, PAGE_SIZE);

        // Gửi dữ liệu qua JSP
        request.setAttribute("farmList", farmList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", search); // để giữ lại keyword trên input
        request.getRequestDispatcher("farms.jsp").forward(request, response);
    }
}
