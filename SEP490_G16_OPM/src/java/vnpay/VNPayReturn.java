package vnpay;

import dal.DBContext;
import dao.WalletTopupHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import model.WalletTopupHistory;

@WebServlet("/vnpay")
public class VNPayReturn extends HttpServlet {

    /**
     * VNPay redirect back to app
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map fields = new HashMap();
        for (Enumeration params = req.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        String vnp_SecureHash = req.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = Config.hashAllFields(fields);
        String vnp_TxnRef = req.getParameter("vnp_TxnRef");
        req.setAttribute("vnp_TxnRef", req.getParameter("vnp_TxnRef"));
        req.setAttribute("vnp_Amount", req.getParameter("vnp_Amount"));
        req.setAttribute("vnp_PayDate", req.getParameter("vnp_PayDate"));
        req.setAttribute("vnp_BankCode", req.getParameter("vnp_BankCode"));
        req.setAttribute("vnp_TransactionNo", req.getParameter("vnp_TransactionNo"));
        req.setAttribute("vnp_ResponseCode", req.getParameter("vnp_ResponseCode"));
        req.setAttribute("vnp_OfferInfo", req.getParameter("vnp_OfferInfo"));

        if (signValue.equals(vnp_SecureHash)) {
            boolean result = "00".equals(req.getParameter("vnp_TransactionStatus"));
            if (result) {
                try {
                    WalletTopupHistoryDAO dao = new WalletTopupHistoryDAO();
                    WalletTopupHistory history = dao.getByTxnRef(vnp_TxnRef).orElseThrow();
                    dao.updateStatusByTxnRef(vnp_TxnRef, "Success");
                    new DBContext().update("UPDATE UserAccount SET wallet = wallet + ? WHERE UserID = ?",
                            history.getAmount(),
                            history.getUserID());
                    req.setAttribute("success", "Success");
                } catch (NoSuchElementException e) {
                    req.setAttribute("error", "TxnRef not exist!");
                }
            }
        } else {
            req.setAttribute("error", "Invalid signature");
        }
        req.getRequestDispatcher("wallet-result.jsp").forward(req, resp);
    }
}
