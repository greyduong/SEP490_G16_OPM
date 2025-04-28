package vnpay;

import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.WalletTopupHistory;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@WebServlet("/wallet")
public class VNPayRedirect extends HttpServlet {

    /**
     * Show wallet topup page
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
    }

    /**
     * Create order and redirect to VNPay checkout
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int userID = user.getUserID();
        long amount;
        // validate amount
        try {
            amount = Long.parseLong(req.getParameter("amount")) * 100;
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid amount");
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

        // build payment url
        VNPayParams params = new VNPayParams();
        params.add("vnp_Version", "2.1.0");
        params.add("vnp_Command", "pay");
        params.add("vnp_TxnRef", txnRef);
        params.add("vnp_IpAddr", "127.0.0.1");
        params.add("vnp_OrderType", "other");
        params.add("vnp_CurrCode", "VND");
        params.add("vnp_Amount", String.valueOf(amount));
        params.add("vnp_OrderInfo", "wallet");
        params.add("vnp_Locale", "vn");
        params.add("vnp_TmnCode", Config.vnp_TmnCode);
        params.add("vnp_ReturnUrl", req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + Config.vnp_ReturnUrl);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+7"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(now);
        params.add("vnp_CreateDate", vnp_CreateDate);
        ZonedDateTime expire = now.plusMinutes(15);
        String vnp_ExpireDate = formatter.format(expire);
        params.add("vnp_ExpireDate", vnp_ExpireDate);
        String queryUrl = params.build();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, queryUrl);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        resp.sendRedirect(paymentUrl);
    }
}
