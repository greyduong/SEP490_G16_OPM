package dao;

import org.junit.Test;

/**
 *
 * @author euhi3
 */
public class ValidationTest {
    
    @Test
    public void testIsValidPassword_Valid() {
        assert Validation.isValidPassword("Anh12345@") == true;
    }
    
}
