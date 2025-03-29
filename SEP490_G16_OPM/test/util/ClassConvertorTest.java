package util;

import java.util.Map;
import model.Farm;
import org.junit.Test;

public class ClassConvertorTest {
    @Test
    public void test() {
        ClassConvertor<Farm> convertor = new ClassConvertor<>(Farm.class);
        Farm farm = new Farm();
        farm.setFarmName("test");
        Map<String, Object> values = convertor.getNotNullFields(farm);
        assert values.keySet().contains("farmname");
        values.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }
}
