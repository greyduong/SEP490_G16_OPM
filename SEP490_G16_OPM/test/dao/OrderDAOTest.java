package dao;

import java.util.List;
import model.Order;
import org.junit.Test;

public class OrderDAOTest {

    @Test
    public void testGetExpiredOrders() {
        var dao = new OrderDAO();
        var orders = dao.getExpiredOrders();
        assert !orders.isEmpty();
        orders.forEach(order -> {
            System.out.println(order.getOrderID());
        });
    }
    
    @Test
    public void testGetOverProcessedDateOrders() {
        var dao = new OrderDAO();
        var orders = dao.getOverProcessedDateOrders();
        assert !orders.isEmpty();
        orders.forEach(order -> {
            System.out.println(order.getOrderID());
        });
    }
    
    @Test
    public void testCancelOrders() {
        var dao = new OrderDAO();
        Order order = new Order();
        order.setOrderID(13);
        var orders = List.of(order);
        assert dao.cancelOrders(orders);
    }
}
