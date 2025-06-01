package listener;

import dao.DeliveryDAO;
import dao.ServerLogDAO;
import java.util.logging.Logger;

public class ProcessDeliveryTask implements Runnable {

    @Override
    public void run() {
        try {
            var dao = new DeliveryDAO();
            var log = new ServerLogDAO();
            var ready = dao.getReadyDeliveries();
            dao.confirmDeliveries(ready);
            log.createLogs(ready.stream().map(delivery -> {
                return "Đã xác nhận đơn vận chuyển mã #%s".formatted(delivery.getDeliveryID());
            }).toList());
            Logger.getLogger(ProcessDeliveryTask.class.getName()).info("Đã xác nhận %s vận chuyển".formatted(ready.size()));
        } catch(Exception e) {

        }
    }

}
