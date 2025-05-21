package controller;

import dao.CartDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Cart;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "ShoppingCartController", urlPatterns = {"/cart"})
public class ShoppingCartController extends HttpServlet {

    private CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        int userId = user.getUserID();

        // Lấy tham số phân trang, tìm kiếm, sắp xếp
        String indexParam = request.getParameter("page");
        int pageIndex = (indexParam != null) ? Integer.parseInt(indexParam) : 1;
        int pageSize = 3;

        String search = request.getParameter("search");
        String sort = request.getParameter("sort");

        // Lấy danh sách giỏ hàng có phân trang, search, sort
        List<Cart> cartList = cartDAO.getCartByUserIdWithFilter(userId, pageIndex, pageSize, search, sort);

        // Tổng số mục trong giỏ (phục vụ phân trang)
        int totalItems = cartDAO.countCartItemsByUserWithFilter(userId, search);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        request.setAttribute("cartList", cartList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("param", request.getParameterMap()); // giữ lại filter khi phân trang

        request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
    }
}
