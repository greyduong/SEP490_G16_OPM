package listener;

import dao.DeliveryDAO;
import java.util.logging.Logger;

public class ProcessDeliveryTask implements Runnable {

    @Override
    public void run() {
        try {
            var dao = new DeliveryDAO();
            var ready = dao.getReadyDeliveries();
            dao.updateDeliveriesStatus(ready, "Confirmed");
            Logger.getLogger(ProcessDeliveryTask.class.getName()).info("Đã xác nhận %s vận chuyển".formatted(ready.size()));
        } catch(Exception e) {

        }
    }

}
