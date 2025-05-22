package controller;

import dao.CartDAO;
import dao.PigsOfferDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
@WebServlet(name = "AddToCartController", urlPatterns = {"/AddToCartController"})
public class AddToCartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int userId = user.getUserID();
            int offerId = Integer.parseInt(request.getParameter("offerId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            PigsOffer pigsOffer = getPigsOfferDAO().getOfferById(offerId);

            if (pigsOffer == null) {
                session.setAttribute("msg", "Chào bán đã ngưng bán hoặc không tồn tại!");
                response.sendRedirect("home");
                return;
            }

            String status = pigsOffer.getStatus();
            if ("Unavailable".equalsIgnoreCase(status) || "Upcoming".equalsIgnoreCase(status) || "Banned".equalsIgnoreCase(status)) {
                session.setAttribute("msg", "Chào bán này hiện không thể đặt hàng do đang ngưng bán hoặc bị cấm.");
                response.sendRedirect("home");
                return;
            }

            if (quantity < pigsOffer.getMinQuantity() || quantity > pigsOffer.getQuantity()) {
                session.setAttribute("msg", "Số lượng không phù hợp!");
                response.sendRedirect("home");
                return;
            }

            getCartDAO().addToCart(userId, offerId, quantity);

            // Lấy tên offer để search
            String offerName = pigsOffer.getName();

            // Redirect sang giỏ hàng với search theo tên offer vừa thêm
            String redirectUrl = "cart?search=" + java.net.URLEncoder.encode(offerName, "UTF-8");
            response.sendRedirect(redirectUrl);
        } catch (NumberFormatException e) {
            session.setAttribute("msg", "Dữ liệu nhập không hợp lệ.");
            response.sendRedirect("home");
        } catch (Exception e) {
            session.setAttribute("msg", "Có lỗi xảy ra trong quá trình xử lý.");
            response.sendRedirect("home");
        }
    }

    public PigsOfferDAO getPigsOfferDAO() {
        return new PigsOfferDAO();
    }

    public CartDAO getCartDAO() {
        return new CartDAO();
    }
}
