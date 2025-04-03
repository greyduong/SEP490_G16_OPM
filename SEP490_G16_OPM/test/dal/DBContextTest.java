package dal;

import org.junit.Test;

public class DBContextTest {

    /**
     * Test of getConnection method, of class DBContext.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DBContext instance = new DBContext();
        assert instance.getConnection() != null;
    }

    /**
     * Test of prepareStatement method, of class DBContext.
     */
    @Test
    public void testPrepareStatement() throws Exception {
        
    }

    /**
     * Test of prepareStatementReturnKeys method, of class DBContext.
     */
    @Test
    public void testPrepareStatementReturnKeys() throws Exception {
        
    }

    /**
     * Test of fetchAll method, of class DBContext.
     */
    @Test
    public void testFetchAll() {
        
    }

    /**
     * Test of fetchOne method, of class DBContext.
     */
    @Test
    public void testFetchOne() {
        
    }

    /**
     * Test of count method, of class DBContext.
     */
    @Test
    public void testCount() {
        
    }

    /**
     * Test of insert method, of class DBContext.
     */
    @Test
    public void testInsert() {
        
    }

    /**
     * Test of update method, of class DBContext.
     */
    @Test
    public void testUpdate() {
        
    }
    
}
