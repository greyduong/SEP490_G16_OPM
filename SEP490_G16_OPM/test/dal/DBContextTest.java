package dal;

import java.util.Arrays;
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
    
    @Test
    public void testInsert() {
        DBContext db = new DBContext();
        Farm farm = new Farm();
        farm.setFarmName("Test");
        int id = db.insert(Farm.class, "Farm", "FarmID", farm, "FarmName");
        assert db.fetchOne(Farm.class, "SELECT * FROM Farm WHERE FarmID = ?", id).getFarmName().equals("Test");
    }
}
