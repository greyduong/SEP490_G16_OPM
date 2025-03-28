package dal;

import model.Farm;
import model.Page;
import org.junit.Test;

public class FarmDAOTest {
    @Test
    public void testGetAllFarm1() {
        FarmDAO dao = new FarmDAO();
        Page<Farm> farms = dao.getAllFarm(1, 5);
        assert farms != null;
        assert !farms.getData().isEmpty();
        farms.getData().forEach((f) -> {
            System.out.println(f.getFarmName());
        });
    }
    
    @Test
    public void testGetAllFarm2() {
        FarmDAO dao = new FarmDAO();
        Page<Farm> farms = dao.getAllFarm(-1, 5);
        assert farms != null;
        assert !farms.getData().isEmpty();
        farms.getData().forEach((f) -> {
            System.out.println(f.getFarmName());
        });
    }
    
    @Test
    public void testGetAllFarm3() {
        FarmDAO dao = new FarmDAO();
        assert dao.getAllFarm(9999, 9999).getData().isEmpty();
    }
    
    @Test
    public void testGetFarm() {
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getFarm(1);
        assert farm != null;
        System.out.println(farm.getFarmName());
    }
}
