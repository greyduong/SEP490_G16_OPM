package dao;

import org.junit.Test;

public class UserDAOTest {
    @Test
    public void testCheckExistsEmail() {
        var db = new UserDAO();
        assert db.checkExistsEmail("admin@gmail.com");
    }
    
    @Test
    public void testCheckExistsEmail2() {
        var db = new UserDAO();
        assert !db.checkExistsEmail("admin2@gmail.com");
    }
    
    @Test
    public void testCheckExistsEmail3() {
        var db = new UserDAO();
        assert !db.checkExistsEmail("");
    }
}
