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
    "/manager-home",
    "/manage-farms",
    "/pending-farms",
    "/process-farm",
    "/manage-offers",
    "/process-offer",
    "/manage-orders",
    "/manage-application",
    "/StaffViewApplication"
})

public class OnlyManagerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getRoleID());
        if (user.getRoleID() != 2) {
            response.sendRedirect(request.getContextPath() + "/home?error=403");
            return;
        }
        chain.doFilter(request, response);
    }
}
