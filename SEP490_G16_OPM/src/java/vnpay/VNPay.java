package vnpay;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * VNPay utilities
 * @author hieu
 */
public class VNPay {
    private final static String PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final static String TMNCODE = "3G1HOHIQ";
    private final static String HASH_SECRET = "48GA7QRMIC7ES1V171PDHXIRVUD513GC";
    
    /**
     * Tạo url thanh toán
     * @param txnRef Mã hóa đơn ngẫu nhiên
     * @param amount Số tiền
     * @param returnUrl Url VNPay sẽ chuyển hướng về khi thanh toán xong
     * @return Url thanh toán
     */
    public String createPaymentUrl(long amount, String txnRef, String returnUrl) {
        amount = amount * 100;
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_OrderType", "other");
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_OrderInfo", "wallet");
        params.put("vnp_Locale", "vn");
        params.put("vnp_TmnCode", TMNCODE);
        params.put("vnp_ReturnUrl", returnUrl);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+7"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(now);
        params.put("vnp_CreateDate", vnp_CreateDate);
        ZonedDateTime expire = now.plusMinutes(15);
        String vnp_ExpireDate = formatter.format(expire);
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        String encodedParams = encodeParams(params);
        String vnp_SecureHash = hashParams(encodedParams);
        return PAY_URL + "?" + encodedParams + "&vnp_SecureHash=" + vnp_SecureHash;
    }
    
    /**
     * Lấy tất cả parameter trong http request
     * @param req http request
     * @return danh sách các parameter
     */
    public Map<String, String> getAllParams(HttpServletRequest req) {
        final Map<String, String> fields = new HashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(param -> fields.put(param, req.getParameter(param)));
        return fields;
    }
    
    public ResponseStatus handleReturn(HttpServletRequest req) {
        final Map<String, String> params = getAllParams(req);
        String vnp_SecureHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        String encodedParams = encodeParams(params);
        String hashed = hashParams(encodedParams);
        if (!hashed.equals(vnp_SecureHash)) return ResponseStatus.INVALID_SIGNATURE;
        if("00".equals(req.getParameter("vnp_TransactionStatus"))) return ResponseStatus.SUCCESS;
        return ResponseStatus.CANCELLED;
    }
    
    /**
     * Thuật toán mã hóa HMACSHA512
     * @param key
     * @param data
     * @return Dữ liệu đã bị mã hóa
     */
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (IllegalStateException | NullPointerException | InvalidKeyException | NoSuchAlgorithmException ex) {
            return "";
        }
    }
    
    public String encodeParams(Map<String, String> params) {
        return params.keySet().stream()
                .sorted((a, b) -> a.compareTo(b))
                .map(p -> p + "=" + URLEncoder.encode(params.get(p), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
    
    public String hashParams(String encodedParams) {
        return hmacSHA512(HASH_SECRET, encodedParams);
    }
    
    public static enum ResponseStatus {
        INVALID_SIGNATURE, CANCELLED, SUCCESS
    }
}
