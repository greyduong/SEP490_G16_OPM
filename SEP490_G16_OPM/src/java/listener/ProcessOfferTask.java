package listener;

import dao.PigsOfferDAO;
import java.util.logging.Logger;

public class ProcessOfferTask implements Runnable {

    @Override
    public void run() {
        var db = new PigsOfferDAO();
        var upcoming = db.getAvailableUpcomingOffers();
        db.updateOffersStatus(upcoming, "Available");
        Logger.getLogger(ProcessOfferTask.class.getName()).info("%s chào bán đã sẵn sàng".formatted(upcoming.size()));
        var expired = db.getExpiredOffers();
        db.updateOffersStatus(expired, "Unavailable", "Hết hạn");
        Logger.getLogger(ProcessOfferTask.class.getName()).info("Đã hủy %s chào bán hết hạn".formatted(expired.size()));
    }
}
