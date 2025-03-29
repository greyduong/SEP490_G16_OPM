package util;

import java.util.Map;
import model.Farm;
import org.junit.Test;

public class DatabaseMapperTest {
    @Test
    public void testGetNotNullFields() {
        DatabaseMapper<Farm> convertor = new DatabaseMapper<>(Farm.class);
        Farm farm = new Farm();
        farm.setFarmName("test");
        Map<String, Object> values = convertor.getFields(farm);
        assert values.keySet().contains("FarmName");
        values.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }
    @Test
    public void testGetIdField() {
        DatabaseMapper<Farm> convertor = new DatabaseMapper<>(Farm.class);
        assert convertor.getPrimaryKey().equals("FarmID");
    }
}
