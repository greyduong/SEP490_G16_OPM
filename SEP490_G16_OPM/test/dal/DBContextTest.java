package dal;

import java.util.List;
import java.util.UUID;
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
        List<Farm> farms = db.fetchAll(Farm.class, "SELECT * FROM Farm");
        assert farms != null && !farms.isEmpty();
        farms.forEach(f -> System.out.println(f));
    }

    @Test
    public void testFetchOne() {
        DBContext db = new DBContext();
        Farm farm = db.fetchOne(Farm.class, "SELECT * FROM Farm");
        assert farm != null;
        System.out.println(farm);
    }
    
    @Test
    public void testInsert() {
        DBContext db = new DBContext();
        Farm farm = new Farm();
        farm.setFarmName("Test");
        int id = db.insert(Farm.class, farm);
        assert db.fetchOne(Farm.class, "SELECT * FROM Farm WHERE FarmID = ?", id).getFarmName().equals("Test");
    }
    
    @Test
    public void testUpdate() {
        DBContext db = new DBContext();
        Farm farm = new Farm();
        farm.setFarmID(2);
        String newName = UUID.randomUUID().toString();
        farm.setFarmName(newName);
        db.update(Farm.class, farm);
        Farm newData = db.fetchOne(Farm.class, "SELECT * FROM Farm WHERE FarmID = ?", 2);
        System.out.println(newData);
        assert newData.getFarmName().equals(newName);
    }
        
    @Test
    public void testCount() {
        DBContext db = new DBContext();
        int count = db.count("SELECT COUNT(*) FROM Farm");
        System.out.println(count);
    }
}
