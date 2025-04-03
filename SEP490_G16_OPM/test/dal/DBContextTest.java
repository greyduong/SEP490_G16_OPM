package dal;

import java.util.List;
import java.util.stream.Collectors;
import model.Farm;
import org.junit.Test;

public class DBContextTest {

    /**
     * Test of getConnection method
     */
    @Test
    public void testGetConnection() {
        DBContext db = new DBContext();
        assert db.getConnection() != null;
    }

    /**
     * Test of fetchAll method
     */
    @Test
    public void testFetchAll() {
        DBContext db = new DBContext();
        List<Farm> farms = db.fetchAll((rs) -> {
            Farm farm = new Farm();
            farm.setFarmName(rs.getString("FarmName"));
            return farm;
        }, "SELECT * FROM Farm");
        assert !farms.isEmpty();
        farms.forEach(System.out::println);
    }
}
