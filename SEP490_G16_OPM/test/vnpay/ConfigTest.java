package vnpay;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
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
public class ConfigTest {
    
    public ConfigTest() {
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
     * Test of hashAllFields method, of class Config.
     */
    @Test
    public void testHashAllFields() {
        System.out.println("hashAllFields");
        Map fields = null;
        String expResult = "";
        String result = Config.hashAllFields(fields);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hmacSHA512 method, of class Config.
     */
    @Test
    public void testHmacSHA512() {
        
    }

    /**
     * Test of getIpAddress method, of class Config.
     */
    @Test
    public void testGetIpAddress() {
        System.out.println("getIpAddress");
        HttpServletRequest request = null;
        String expResult = "";
        String result = Config.getIpAddress(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRandomNumber method, of class Config.
     */
    @Test
    public void testGetRandomNumber() {
        System.out.println("getRandomNumber");
        int len = 0;
        String expResult = "";
        String result = Config.getRandomNumber(len);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
