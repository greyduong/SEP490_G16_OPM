package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@WebListener
public class TaskScheduler implements ServletContextListener {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger.getLogger(TaskScheduler.class.getName()).info("Bắt đầu chạy task");
        scheduler.scheduleAtFixedRate(new ProcessOrderTask(), 0, 2, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new ProcessOfferTask(), 0, 2, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new ProcessDeliveryTask(), 0, 2, TimeUnit.MINUTES);
    }
}