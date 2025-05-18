package controller;

import dao.WalletTopupHistoryDAO;
import dao.WalletUseHistoryDAO;
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
import model.WalletUseHistory;

@WebServlet("/wallet")
public class WalletController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int userID = user.getUserID();
        int topupPage;
        int usePage;
        try {
            String topupPageStr = Optional.ofNullable(req.getAttribute("topupPage")).map(String::valueOf).orElse("");
            String usePageStr = Optional.ofNullable(req.getAttribute("usePage")).map(String::valueOf).orElse("");
            topupPage = Integer.parseInt(topupPageStr);
            usePage = Integer.parseInt(usePageStr);
        } catch (NumberFormatException e) {
            topupPage = 1;
			usePage = 1;
        }
        if (topupPage < 1) topupPage = 1;
        if (usePage < 1) usePage = 1;
        
        Page<WalletTopupHistory> topup = new WalletTopupHistoryDAO().getAll(userID, topupPage);
        Page<WalletUseHistory> use = new WalletUseHistoryDAO().getAll(userID, topupPage);
        req.setAttribute("topup", topup);
        req.setAttribute("use", use);
        req.getRequestDispatcher("wallet.jsp").forward(req, resp);
    }
}
