package com.isc.hermes.model.navigation;

import com.isc.hermes.model.location.LocationIntervals;

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

public class NavigationOrchestrator implements Runnable {
    private final AtomicBoolean caRun;
    private final UserRouteTrackerNotifier userRouteTrackerNotifier;
    private final UserRouteTracker userRouteTracker;

    public NavigationOrchestrator(UserRouteTracker userRouteTracker) {
        this.userRouteTracker = userRouteTracker;
        caRun = new AtomicBoolean(true);
        userRouteTrackerNotifier = userRouteTracker.getRouteTrackerNotifier();
    }

    public void startNavigationMode(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        Thread thread = new Thread(this, "Navigation Thread");
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();
    }

    public void stopNavigationMode() {
        caRun.set(false);
    }

    @Override
    public void run() {
        while (caRun.get()) {
            userRouteTracker.update();
            userRouteTrackerNotifier.doNotify();

            if (Thread.currentThread().isInterrupted()){
                Timber.e("[%s] Has been interrupted", Thread.currentThread().getName());
                caRun.set(false);
                continue;
            }

            try {
                Thread.sleep((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
            } catch (InterruptedException e) {
                Timber.e("[%s] Finished unexpectedly", Thread.currentThread().getName());
                caRun.set(false);
            }
        }

        if (!caRun.get()){
            Timber.d("[%s] has finished", Thread.currentThread().getName());
        }
    }
}
