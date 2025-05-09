package listener;

import dao.OrderDAO;

public class ProcessOrderTask implements Runnable {
    @Override
    public void run() {
        new OrderDAO().cancelExpiredOrders();
    }
}