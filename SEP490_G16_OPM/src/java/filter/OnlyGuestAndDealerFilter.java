package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.User;

@WebFilter(urlPatterns = {
    "/farms",
    "/farm-detail"
})
public class OnlyGuestAndDealerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getSession().getAttribute("user") == null) {
            chain.doFilter(request, response);
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/home?error=403");
            return;
        }
        chain.doFilter(request, response);
    }
}
