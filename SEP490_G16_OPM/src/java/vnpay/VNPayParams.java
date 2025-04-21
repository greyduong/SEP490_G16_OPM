package vnpay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VNPayParams {
    private final List<VNPayParam> params = new ArrayList<>();
    public void add(String key, String value) {
        params.add(new VNPayParam(key, value));
    }
    public String build() {
        return params.stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .map(p -> p.getKey() + "=" + URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
