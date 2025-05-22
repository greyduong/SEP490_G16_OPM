package controller;

import dao.CartDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PigsOffer;

/**
 *
 * @author duong
 */
@WebServlet(name = "UpdateCartController", urlPatterns = {"/update-cart"})
public class UpdateCartController extends HttpServlet {

    public CartDAO getCartDAO() {
        return new CartDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int page = Integer.parseInt(request.getParameter("page"));

        CartDAO cartDAO = getCartDAO();
        PigsOffer offer = cartDAO.getPigsOfferByCartId(cartId);

        if (quantity < offer.getMinQuantity() || quantity > offer.getQuantity()) {
            response.sendRedirect("cart?page=" + page + "&error=Số lượng không hợp lệ");
            return;
        }

        cartDAO.updateCartQuantity(cartId, quantity);
        response.sendRedirect("cart?page=" + page);

    }
}
