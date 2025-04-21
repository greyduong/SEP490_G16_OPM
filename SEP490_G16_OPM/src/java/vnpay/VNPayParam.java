package vnpay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class VNPayParam {
    private String key;
    private String value;

    public VNPayParam(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public void setValue(String value) {
        this.value = value;
    }
}