/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package controller;

import com.google.gson.GsonBuilder;
import dal.DBContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import model.User;
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
public class DealerDashboardControllerTest {
    
    public DealerDashboardControllerTest() {
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
     * Test of getTotalSpend method, of class DealerDashboardController.
     */
    @Test
    public void testGetTotalSpend() {
        System.out.println("getTotalSpend");
        DBContext db = new DBContext();
        User user = new User();
        user.setUserID(5);
        LocalDate from = LocalDate.parse("2025-05-10");
        LocalDate to = LocalDate.parse("2025-05-21");
        DealerDashboardController instance = new DealerDashboardController();
        long result = instance.getTotalSpend(db, user, from, to);
        System.out.println("result = " + result);
    }
    
    @Test
    public void test() {
        User user = new User();
        user.setUserID(5);
        LocalDate from = LocalDate.parse("2025-05-10");
        LocalDate to = LocalDate.parse("2025-05-21");
        DealerDashboardController instance = new DealerDashboardController();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(instance.getOrderStat(user, from, to), System.out);
    }
}
