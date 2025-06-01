package listener;

import dao.PigsOfferDAO;
import dao.ServerLogDAO;
import java.util.logging.Logger;

public class ProcessOfferTask implements Runnable {

    @Override
    public void run() {
        try {
            var db = new PigsOfferDAO();
            var log = new ServerLogDAO();
            var upcoming = db.getAvailableUpcomingOffers();
            db.updateOffersStatus(upcoming, "Available");
            Logger.getLogger(ProcessOfferTask.class.getName()).info("%s chào bán đã sẵn sàng".formatted(upcoming.size()));
            log.createLogs(upcoming.stream().map(offer -> {
                return "Chào bán mã #%s đã sẵn sàng".formatted(offer.getOfferID());
            }).toList());
            var expired = db.getExpiredOffers();
            db.updateOffersStatus(expired, "Unavailable", "Hết hạn");
            log.createLogs(expired.stream().map(offer -> {
                return "Chào bán mã #%s đã hết hạn".formatted(offer.getOfferID());
            }).toList());
            Logger.getLogger(ProcessOfferTask.class.getName()).info("Đã hủy %s chào bán hết hạn".formatted(expired.size()));
        } catch(Exception e) {

        }
    }
}
