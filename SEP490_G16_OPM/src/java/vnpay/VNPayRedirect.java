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
import java.util.Optional;
import java.util.UUID;

@WebServlet("/wallet-topup")
public class VNPayRedirect extends HttpServlet {

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
        
        
        // VNPay require *100
        amount = amount * 100;

        // generate random txnRef
        String txnRef = UUID.randomUUID().toString().replaceAll("-", "");

        // Add to database
        WalletTopupHistory history = new WalletTopupHistory();
        history.setUserID(userID);
        history.setTxnRef(txnRef);
        history.setAmount(amount / 100);
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
