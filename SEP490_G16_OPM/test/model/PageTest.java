package model;

import java.util.List;
import org.junit.Test;

public class PageTest {

    /**
     * Test of toString method
     */
    @Test
    public void testToString() {
        Page<Farm> p = new Page();
        Farm farm = new Farm();
        farm.setFarmName("Test");
        p.setPageNumber(1);
        p.setPageSize(12);
        p.setTotalPage(12);
        p.setData(List.of(farm));
        System.out.println(p.toString());
    }
}
