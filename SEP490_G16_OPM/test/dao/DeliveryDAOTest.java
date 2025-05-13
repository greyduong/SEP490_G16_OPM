package dao;

import java.util.List;
import model.Delivery;
import org.junit.Test;

public class DeliveryDAOTest {
    @Test
    public void testGetReadyDeliveries() {
		var res = new DeliveryDAO().getReadyDeliveries();
        assert !res.isEmpty();
        res.forEach(System.out::println);
    }
    
    @Test
    public void testUpdateDeliveriesStatus() {
        var delivery = new Delivery();
        delivery.setDeliveryID(4);
        var dao = new DeliveryDAO();
        List<Delivery> deliveries = List.of(delivery);
        dao.updateDeliveriesStatus(deliveries, "Confirmed");
        assert dao.getDeliveryStatusById(4).equals("Confirmed");
    }
    
}
