package dao;

import model.Farm;
import model.Page;
import org.junit.Test;

public class FarmDAOTest {

    @Test
    public void testGetAllFarm() {
        FarmDAO dao = new FarmDAO();
        Page<Farm> farms = dao.getAllFarm(1, 3);
        assert farms != null;
        assert !farms.getData().isEmpty();
        System.out.println("PageNumber = " + farms.getPageNumber());
        System.out.println("TotalPage = " + farms.getTotalPage());
        farms.getData().forEach((f) -> {
            System.out.println(f);
        });
    }

    @Test
    public void testGetFarm() {
        FarmDAO dao = new FarmDAO();
        Farm farm = dao.getFarm(1);
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
        int id = dao.createFarm(farm);
        Farm created = dao.getFarm(id);
        assert created != null;
        System.out.println(created);
    }

    @Test
    public void testUpdateFarm() {
        FarmDAO dao = new FarmDAO();
        dao.deleteFarm(2);
        Farm deleted = dao.getFarm(2);
        assert deleted.getStatus().equals("Inactive");
        System.out.println(deleted);
    }
    
    @Test
    public void testSearchFarm() {
        FarmDAO dao = new FarmDAO();
        Page<Farm> page = dao.searchFarm("Example", 1, 5);
        page.getData().forEach(System.out::println);
    }
}
