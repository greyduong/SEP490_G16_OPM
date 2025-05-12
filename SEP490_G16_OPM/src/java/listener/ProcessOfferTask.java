package listener;

import dao.PigsOfferDAO;

public class ProcessOfferTask implements Runnable {

    @Override
    public void run() {
        var db = new PigsOfferDAO();
        var upcoming = db.getAvailableUpcomingOffers();
        db.updateOffersStatus(upcoming, "Available");
        var expired = db.getExpiredOffers();
        db.updateOffersStatus(expired, "Unavailable");
    }
}
