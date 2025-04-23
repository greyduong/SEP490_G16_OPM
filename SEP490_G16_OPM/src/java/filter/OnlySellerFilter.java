package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;

@WebFilter()
public class OnlySellerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        new NeedLoginFilter().doFilter(request, response, chain);
        if (response.isCommitted()) return;
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRoleID() != 4) {
            response.sendRedirect(request.getContextPath() + "/home?error=403");
        }
        chain.doFilter(request, response);
    }
}
