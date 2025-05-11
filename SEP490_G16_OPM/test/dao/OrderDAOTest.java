package dao;

import org.junit.Test;

public class OrderDAOTest {

    @Test
    public void testGetExpiredOrders() {
        var dao = new OrderDAO();
        var expired = dao.getExpiredOrders();
        assert !expired.isEmpty();
        expired.forEach(order -> {
            System.out.println(order.getOrderID());
        });
    }
}
