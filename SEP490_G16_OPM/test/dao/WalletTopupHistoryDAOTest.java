package dao;

import dal.Mapper;
import java.util.Optional;
import model.WalletTopupHistory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author euhi3
 */
public class WalletTopupHistoryDAOTest {
    
    public WalletTopupHistoryDAOTest() {
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
     * Test of getByTxnRef method, of class WalletTopupHistoryDAO.
     */
    @Test
    public void testGetByTxnRef() {
        new WalletTopupHistoryDAO().fetchAll(WalletTopupHistoryDAO.mapper(), "SELECT * FROM WalletTopupHistory", new Object[0]).forEach(e -> {
            System.out.println(e.getTxnRef());
        });
    }

    /**
     * Test of updateStatusByTxnRef method, of class WalletTopupHistoryDAO.
     */
    @Test
    public void testUpdateStatusByTxnRef() {
        System.out.println("updateStatusByTxnRef");
        String txnRef = "";
        String status = "";
        WalletTopupHistoryDAO instance = new WalletTopupHistoryDAO();
        instance.updateStatusByTxnRef(txnRef, status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class WalletTopupHistoryDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        WalletTopupHistory history = new WalletTopupHistory();
        history.setAmount(1000000);
        history.setTxnRef("testtest");
        history.setUserID(1);
        history.setStatus("Pending");
        WalletTopupHistoryDAO instance = new WalletTopupHistoryDAO();
        instance.create(history);
        assert instance.getByTxnRef("testtest").isPresent();
    }

    /**
     * Test of mapper method, of class WalletTopupHistoryDAO.
     */
    @Test
    public void testMapper() {
        System.out.println("mapper");
        Mapper<WalletTopupHistory> expResult = null;
        Mapper<WalletTopupHistory> result = WalletTopupHistoryDAO.mapper();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
