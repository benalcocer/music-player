package com.example.util;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

public class CustomThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    /**
     * boolean indicating if the shutDownAll method has been invoked.
     */
    private static boolean isShuttingAllDown = false;

    /**
     * Dummy object used as the value in the Map.
     */
    private static final Object PRESENT = new Object();

    /**
     * All the instantiated executors that have not been shutdown.
     */
    private static final IdentityHashMap<CustomThreadPoolExecutor, Object> executors = new IdentityHashMap<>();

    public CustomThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
        executors.put(this, PRESENT);
    }

    /**
     * Shut down all the executors that are being tracked during the application's life cycle.
     */
    public static void shutAllDown() {
        isShuttingAllDown = true;
        executors.keySet().forEach(executor -> {
            try {
                executor.shutdown();
            } catch (Exception e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        });
        executors.clear();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (!isShuttingAllDown) {
            executors.remove(this);
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        if (!isShuttingAllDown) {
            executors.remove(this);
        }
        return super.shutdownNow();
    }
}
