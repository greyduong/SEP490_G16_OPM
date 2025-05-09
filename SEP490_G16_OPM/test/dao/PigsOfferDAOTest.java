package dao;

import org.junit.Test;

public class PigsOfferDAOTest {
    /**
     * Test of updateUpcomingOffers method, of class PigsOfferDAO.
     */
    @Test
    public void testUpdateUpcomingOffers() {
        PigsOfferDAO dao = new PigsOfferDAO();
        dao.updateUpcomingOffers();
    }

    /**
     * Test of updateExpiredOffers method, of class PigsOfferDAO.
     */
    @Test
    public void testUpdateExpiredOffers() {
        PigsOfferDAO dao = new PigsOfferDAO();
        dao.updateExpiredOffers();
    }
}
