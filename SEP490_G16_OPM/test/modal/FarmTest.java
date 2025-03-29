package modal;

import java.sql.Timestamp;
import model.Farm;
import org.junit.Test;

public class FarmTest {
    @Test
    public void testToString() {
        Farm farm = new Farm();
        farm.setFarmName("wtf");
        farm.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        farm.setDescription("wtf");
        System.out.println(farm.toString());
    }
}
