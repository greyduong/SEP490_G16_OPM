package controller;

import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import model.User;
import org.junit.Test;

public class SellerHomeControllerTest {
    @Test
    public void testDoPost() throws Exception {
        User user = new User();
        user.setUserID(4);
        var from = LocalDate.parse("2025-05-13");
        var to = LocalDate.parse("2025-05-20");
        var res = new SellerHomeController().getOfferStat(user, from, to);
        new GsonBuilder().setPrettyPrinting().create().toJson(res, System.out);
    }
    
}
