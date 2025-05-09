package dao;

import org.junit.Test;

public class OrderDAOTest {
    /**
     * Test of cancelExpiredOrders method, of class OrderDAO.
     */
    @Test
    public void testCancelExpiredOrders() {
        OrderDAO dao = new OrderDAO();
        dao.cancelExpiredOrders();
    }
}
