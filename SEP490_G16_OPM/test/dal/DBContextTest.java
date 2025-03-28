package dal;

import org.junit.Test;

public class DBContextTest {

    @Test
    public void testConnection() {
        DBContext db = new DBContext();
        assert db.connection != null;
    }
}
