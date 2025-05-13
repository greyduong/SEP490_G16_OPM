package listener;

import dao.DeliveryDAO;

public class ProcessDeliveryTask implements Runnable {

    @Override
    public void run() {
		var dao = new DeliveryDAO();
		var ready = dao.getReadyDeliveries();
		dao.updateDeliveriesStatus(ready, "Confirmed");
    }
    
}
