package dal;

import java.util.List;
import model.Farm;
import org.junit.Test;

public class DBContextTest {

    @Test
    public void testConnection() {
        DBContext db = new DBContext();
        assert db.connection != null;
    }

    @Test
    public void testFetchAll() {
        DBContext db = new DBContext();
        List<Farm> farms = db.fetchAll(Farm.class, "SELECT * FROM Farm", new Object[0]);
        assert farms != null && !farms.isEmpty();
        farms.forEach(f -> System.out.println(f.getFarmName()));
    }

    @Test
    public void testFetchOne() {
        DBContext db = new DBContext();
        Farm farm = db.fetchOne(Farm.class, "SELECT * FROM Farm", new Object[0]);
        assert farm != null;
        System.out.println(farm.getFarmName());
    }
}
