package controller;

import dao.CartDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author duong
 */
@WebServlet(name = "RemoveCartController", urlPatterns = {"/remove-cart"})
public class RemoveCartController extends HttpServlet {

    private CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int cartId = Integer.parseInt(request.getParameter("id"));
            cartDAO.removeCartById(cartId);
        } catch (NumberFormatException | NullPointerException e) {
        }
        response.sendRedirect("cart");
    }
}
