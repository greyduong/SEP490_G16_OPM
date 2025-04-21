package dao;

import model.Farm;
import model.Page;
import org.junit.Test;

public class FarmDAOTest {

    @Test
    public void testGetFarm() {
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getById(1);
        assert farm != null;
        System.out.println(farm);
    }
    
    @Test
    public void testCreateFarm() {
        FarmDAO dao = new FarmDAO();
        Farm farm = new Farm();
        farm.setDescription("Example Farm Description");
        farm.setFarmName("Example Farm Name");
        farm.setLocation("Example Farm Location");
        farm.setStatus("Active");
        farm.setSellerID(2);
        int id = dao.create(farm);
        Farm created = dao.getById(id);
        assert created != null;
        System.out.println(created);
    }

    @Test
    public void testUpdateFarm() {
        FarmDAO dao = new FarmDAO();
        dao.delete(2);
        Farm deleted = dao.getById(2);
        assert deleted.getStatus().equals("Inactive");
        System.out.println(deleted);
    }
    
    @Test
    public void testSearchFarm() {
        FarmDAO dao = new FarmDAO();
        Page<Farm> page = dao.search("Example", 1, 5);
        page.getData().forEach(System.out::println);
    }
}
