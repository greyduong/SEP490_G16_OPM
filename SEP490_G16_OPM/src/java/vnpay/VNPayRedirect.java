package vnpay;

import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;
import model.User;
import model.WalletTopupHistory;

@WebServlet("/wallet")
public class VNPayRedirect extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Integer> logged = Optional.ofNullable(req.getSession().getAttribute("user")).map(obj -> (User) obj).map(u -> u.getUserID());
        if (logged.isEmpty()) {
            resp.sendRedirect("login-register.jsp");
            return;
        }
        req.getRequestDispatcher("wallet-topup.jsp").forward(req, resp);
    }
    

    /**
     * Create order and redirect to VNPay checkout
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Integer> logged = Optional.ofNullable(req.getSession().getAttribute("user")).map(obj -> (User) obj).map(u -> u.getUserID());
        if (logged.isEmpty()) {
            resp.sendRedirect("login-register.jsp");
            return;
        }
        int userID = logged.get();
        long amount = Integer.parseInt(req.getParameter("amount")) * 100;
        VNPayParams params = new VNPayParams();
        params.add("vnp_Version", "2.1.0");
        params.add("vnp_Command", "pay");
        String txnRef = Config.getRandomNumber(8);
        params.add("vnp_TxnRef", txnRef);
        params.add("vnp_IpAddr", "127.0.0.1");
        params.add("vnp_OrderType", "other");
        params.add("vnp_CurrCode", "VND");
        params.add("vnp_Amount", String.valueOf(amount));
        params.add("vnp_OrderInfo", "wallet");
        params.add("vnp_Locale", "vn");
        params.add("vnp_TmnCode", Config.vnp_TmnCode);
        params.add("vnp_ReturnUrl", req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + Config.vnp_ReturnUrl);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        params.add("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        params.add("vnp_ExpireDate", vnp_ExpireDate);
        String queryUrl = params.build();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, queryUrl);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        System.out.println(paymentUrl);
        
        WalletTopupHistory history = new WalletTopupHistory();
        history.setUserID(userID);
        history.setTxnRef(txnRef);
        history.setAmount(amount);
        history.setStatus("Pending");
        new WalletTopupHistoryDAO().create(history);
        
        resp.sendRedirect(paymentUrl);
    }
}
