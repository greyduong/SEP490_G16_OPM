package controller;

import dao.FarmDAO;
import dao.OrderDAO;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Farm;
import model.Order;

@WebServlet(name = "ManageOrdersController", urlPatterns = {"/manage-orders"})
public class ManageOrdersController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final FarmDAO farmDAO = new FarmDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String farmIdRaw = request.getParameter("farmId");

        int farmId = -1;
        if (farmIdRaw != null && !farmIdRaw.isEmpty()) {
            try {
                farmId = Integer.parseInt(farmIdRaw);
            } catch (NumberFormatException e) {
                farmId = -1;
            }
        }

        int page = 1;
        int pageSize = 10;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        // Lấy danh sách farm với try-catch
        List<Farm> farmList;
        try {
            farmList = farmDAO.getAllFarms();
        } catch (Exception e) {
            e.printStackTrace();
            farmList = Collections.emptyList(); // fallback nếu lỗi
        }
        request.setAttribute("farmList", farmList);

        // Lấy dữ liệu đơn hàng
        List<Order> orders = orderDAO.getAllOrdersWithFilterAndPaging(search, status, farmId, sort, page, pageSize);
        int totalData = orderDAO.countAllOrdersWithFilter(search, status, farmId);
        int totalPage = (int) Math.ceil((double) totalData / pageSize);

        request.setAttribute("orders", orders);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalData", totalData);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("farmId", farmIdRaw);

        request.getRequestDispatcher("manage-orders.jsp").forward(request, response);
    }
}
