package controller;

import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import model.WalletTopupHistory;
import vnpay.VNPay;

@WebServlet("/wallet-topup-result")
public class WalletTopupResultController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var result = new VNPay().handleReturn(req);
        
        // Sai chữ ký
        if (result == VNPay.ResponseStatus.INVALID_SIGNATURE) {
            req.setAttribute("error", "Sai chữ ký!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }
        
        String txnRef = req.getParameter("vnp_TxnRef");
        
        var db = new WalletTopupHistoryDAO();
        WalletTopupHistory history = db.getByTxnRef(txnRef).orElse(null);
        
        if (history == null) {
            req.setAttribute("error", "Giao dịch không tồn tại!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }
        
        // Người dùng hủy thanh toán
        if (result == VNPay.ResponseStatus.CANCELLED) {
            db.updateStatusByTxnRef(txnRef, "Cancelled");
            req.setAttribute("fail", "Bạn đã hủy giao dịch!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }
        
        // Người dùng reload trang
        if (!history.getStatus().equals("Pending")) {
            req.setAttribute("error", "Bạn đã từng xác nhận giao dịch này!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }
        
        long amount = Long.parseLong((String) req.getParameter("vnp_Amount")) / 100;
        req.setAttribute("success", "Bạn đã nạp thành công %s VND vào tài khoản".formatted(amount));
        db.updateStatusByTxnRef(txnRef, "Success");
        req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
    }
    
}
