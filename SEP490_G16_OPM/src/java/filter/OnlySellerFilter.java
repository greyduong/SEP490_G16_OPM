package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;

@WebFilter(urlPatterns = {
    "/seller",
    "/createFarm",
    "/my-farms",
    "/updateFarm",
    "/deactivateFarm",
    "/my-offers",
    "/createOffer",
    "/updateOffer",
    "/updateOfferStatus",
    "/orders-request",
    "/confirm-order",
    "/reject-order",
    "/customer-orders",
    "/customer-order-details",
    "/create-delivery"
})
public class OnlySellerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getRoleID());
        if (user.getRoleID() != 4) {
            response.sendRedirect(request.getContextPath() + "/home?error=403");
            return;
        }
        chain.doFilter(request, response);
    }
}
