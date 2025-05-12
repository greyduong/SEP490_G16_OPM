package controller;

import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.WalletTopupHistory;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import vnpay.VNPay;

@WebServlet("/wallet-topup")
public class WalletTopupController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int userID = user.getUserID();

        String method = Optional.ofNullable(req.getParameter("method")).orElse("picker");
        req.setAttribute("method", method);
        req.setAttribute("amount", req.getParameter("amount"));
        req.setAttribute("amountInput", req.getParameter("amountInput"));
        
        long amount;
        if (method.equals("picker")) {
            try {
                amount = Long.parseLong(req.getParameter("amount"));
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Mệnh giá không hợp lệ");
                req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
                return;
            }
        } else {
            try {
                amount = Long.parseLong(req.getParameter("amountInput"));
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Mệnh giá không hợp lệ");
                req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
                return;
            }
        }

        // validate amount
        if (amount % 10000 != 0) {
            req.setAttribute("error", "Mệnh giá phải là bội số của 10,000");
            req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
            return;
        }
        if (amount < 10000 || amount > 100000000) {
            req.setAttribute("error", "Mệnh giá không hợp lệ");
            req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
            return;
        }
        
        // generate random txnRef
        String txnRef = UUID.randomUUID().toString().replaceAll("-", "");

        // Add to database
        WalletTopupHistory history = new WalletTopupHistory();
        history.setUserID(userID);
        history.setTxnRef(txnRef);
        history.setAmount(amount);
        history.setStatus("Pending");
        new WalletTopupHistoryDAO().create(history);
        
        String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/wallet-topup-result";
        
        String paymentUrl = new VNPay().createPaymentUrl(amount, txnRef, returnUrl);
        
        resp.sendRedirect(paymentUrl);
    }
}
