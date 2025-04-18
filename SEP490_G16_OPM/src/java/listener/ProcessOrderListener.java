package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@WebListener
public class ProcessOrderListener implements ServletContextListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger.getLogger(ProcessOrderListener.class.getName()).info("Start Process Order Schedule:");
        scheduler.scheduleAtFixedRate(new ProcessOrderTask(), 0, 2, TimeUnit.MINUTES);
    }
}