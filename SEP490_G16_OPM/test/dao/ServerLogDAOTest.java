/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package dao;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author euhi3
 */
public class ServerLogDAOTest {
    
    public ServerLogDAOTest() {
    }

    @Test
    public void testCreateLogs() {
        new ServerLogDAO().createLogs(List.of("Đã hủy đơn 1", "Đã hủy đơn 2"));
    }
    
}
