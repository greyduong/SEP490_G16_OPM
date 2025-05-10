package controller;

import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import model.Page;
import model.User;
import model.WalletTopupHistory;

@WebServlet("/wallet")
public class WalletController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int userID = user.getUserID();
        int page;
        try {
            String pageStr = Optional.ofNullable(req.getAttribute("page")).map(String::valueOf).orElse("");
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }
        if (page < 1) page = 1;
        
        Page<WalletTopupHistory> topup = new WalletTopupHistoryDAO().getAll(userID, page);
        req.setAttribute("topup", topup);
        req.getRequestDispatcher("wallet.jsp").forward(req, resp);
    }
}
