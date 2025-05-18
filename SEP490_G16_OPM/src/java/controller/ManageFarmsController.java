package controller;

import dao.FarmDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Farm;
import model.Page;

import java.io.IOException;

@WebServlet(name = "ManageFarmsController", urlPatterns = {"/manage-farms"})
public class ManageFarmsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String msg = request.getParameter("msg");
        if (msg != null && !msg.isEmpty()) {
            request.setAttribute("msg", msg);
        }

        int pageNumber = 1;
        int pageSize = 5;

        String sort = request.getParameter("sort");
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                pageNumber = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            pageNumber = 1;
        }

        try {
            FarmDAO farmDAO = new FarmDAO();
            Page<Farm> pagedFarms = farmDAO.getAllFarmsWithFilter(search, status, pageNumber, pageSize, sort);

            request.setAttribute("pagedFarms", pagedFarms);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("sort", sort);
            request.getRequestDispatcher("manage-farms.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi khi tải danh sách trang trại.");
        }
    }

    @Override
    public String getServletInfo() {
        return "Quản lý toàn bộ trang trại cho Manager";
    }
}
