/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package dao;

import java.util.List;
import model.Order;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author duong
 */
public class OrderDAOTest {
    
    public OrderDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insertOrder method, of class OrderDAO.
     */
    @Test
    public void testInsertOrder() {
        System.out.println("insertOrder");
        int dealerId = 0;
        int sellerId = 0;
        int offerId = 0;
        int quantity = 0;
        double totalPrice = 0.0;
        OrderDAO instance = new OrderDAO();
        instance.insertOrder(dealerId, sellerId, offerId, quantity, totalPrice);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOrdersByBuyerId method, of class OrderDAO.
     */
    @Test
    public void testGetOrdersByBuyerId() {
        System.out.println("getOrdersByBuyerId");
        int dealerId = 0;
        OrderDAO instance = new OrderDAO();
        List<Order> expResult = null;
        List<Order> result = instance.getOrdersByBuyerId(dealerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPendingOrdersBySellerId method, of class OrderDAO.
     */
    @Test
    public void testGetPendingOrdersBySellerId() {
        System.out.println("getPendingOrdersBySellerId");
        int sellerId = 0;
        OrderDAO instance = new OrderDAO();
        List<Order> expResult = null;
        List<Order> result = instance.getPendingOrdersBySellerId(sellerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOrdersExcludingPending method, of class OrderDAO.
     */
    @Test
    public void testGetOrdersExcludingPending() {
        System.out.println("getOrdersExcludingPending");
        int sellerID = 0;
        OrderDAO instance = new OrderDAO();
        List<Order> expResult = null;
        List<Order> result = instance.getOrdersExcludingPending(sellerID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOrderById method, of class OrderDAO.
     */
    @Test
    public void testGetOrderById() {
        System.out.println("getOrderById");
        int orderId = 0;
        OrderDAO instance = new OrderDAO();
        Order expResult = null;
        Order result = instance.getOrderById(orderId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of confirmOrder method, of class OrderDAO.
     */
    @Test
    public void testConfirmOrder() {
        System.out.println("confirmOrder");
        int orderID = 0;
        OrderDAO instance = new OrderDAO();
        boolean expResult = false;
        boolean result = instance.confirmOrder(orderID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cancelExpiredOrders method, of class OrderDAO.
     */
    @Test
    public void testCancelExpiredOrders() {
        System.out.println("cancelExpiredOrders");
        OrderDAO instance = new OrderDAO();
        instance.cancelExpiredOrders();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
