package org.spicord;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class SpicordScheduler {

    private static ScheduledExecutorService scheduler;

    public synchronized static ScheduledExecutorService getDefaultScheduler() {
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(4, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
            ((ThreadPoolExecutor) scheduler).setRejectedExecutionHandler((r,e)->{});
        }
        return scheduler;
    }

    private SpicordScheduler() {}
}
