package vnpay;

import dao.UserDAO;
import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.WalletTopupHistory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import model.User;

@WebServlet("/vnpay")
public class VNPayReturn extends HttpServlet {

    private Map<String, String> getAllParams(HttpServletRequest req) {
        final Map<String, String> fields = new HashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(param -> fields.put(param, req.getParameter(param)));
        return fields;
    }

    /**
     * VNPay redirect back to app
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, String> params = getAllParams(req);
        String vnp_SecureHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String hashed = Config.hashAllFields(params);

        // invalid hash
        if (!hashed.equals(vnp_SecureHash)) {
            req.setAttribute("error", "Sai chữ ký!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }

        String vnp_TxnRef = req.getParameter("vnp_TxnRef");

        long amount = Long.parseLong(req.getParameter("vnp_Amount")) / 100;

        req.setAttribute("vnp_TxnRef", req.getParameter("vnp_TxnRef"));
        req.setAttribute("vnp_Amount", amount);
        req.setAttribute("vnp_PayDate", req.getParameter("vnp_PayDate"));
        req.setAttribute("vnp_BankCode", req.getParameter("vnp_BankCode"));
        req.setAttribute("vnp_TransactionNo", req.getParameter("vnp_TransactionNo"));
        req.setAttribute("vnp_ResponseCode", req.getParameter("vnp_ResponseCode"));
        req.setAttribute("vnp_OfferInfo", req.getParameter("vnp_OfferInfo"));

        boolean isSuccess = "00".equals(req.getParameter("vnp_TransactionStatus"));

        var db = new WalletTopupHistoryDAO();

        WalletTopupHistory history;
        try {
            history = db.getByTxnRef(vnp_TxnRef).orElseThrow();
        } catch (NoSuchElementException e) {
            req.setAttribute("error", "Giao dịch không tồn tại!");
            req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
            return;
        }

        if (isSuccess) {
            if (history.getStatus().equals("Pending")) {
                db.updateStatusByTxnRef(vnp_TxnRef, "Success");
                db.update("UPDATE UserAccount SET wallet = wallet + ? WHERE UserID = ?", history.getAmount(), history.getUserID());
                // force update session
                User user = (User) req.getSession().getAttribute("user");
                user = new UserDAO().getUserByUsername(user.getUsername());
                req.setAttribute("user", user);
                req.getSession().setAttribute("user", user);
            }
            req.setAttribute("success", true);
        } else {
            db.updateStatusByTxnRef(vnp_TxnRef, "Fail");
            req.setAttribute("fail", true);
        }
        req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
    }
}
