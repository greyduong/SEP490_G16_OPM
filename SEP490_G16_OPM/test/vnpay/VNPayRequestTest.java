package vnpay;

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
public class VNPayRequestTest {
    
    public VNPayRequestTest() {
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
     * Test of addParam method, of class VNPayRequest.
     */
    @Test
    public void testAddParam() {
        System.out.println("addParam");
        String key = "";
        String value = "";
        VNPayParams instance = new VNPayParams();
        instance.add(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of build method, of class VNPayRequest.
     */
    @Test
    public void testBuild() {
        VNPayParams req = new VNPayParams();
        req.add("abcsg", "sdgsdg");
        req.add("cabdsdbs", "sdbsdb");
        req.add("basag", "agsas");
        System.out.println(req.build());
    }
    
}
