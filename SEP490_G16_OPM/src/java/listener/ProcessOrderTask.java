package listener;

import dao.OrderDAO;
import java.util.logging.Logger;

public class ProcessOrderTask implements Runnable {
    @Override
    public void run() {
        Logger.getLogger(ProcessOrderTask.class.getName()).info("Running Process Order Task:");
        new OrderDAO().cancelExpiredOrders();
    }
}