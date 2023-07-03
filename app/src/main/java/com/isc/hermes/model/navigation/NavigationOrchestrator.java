package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.location.LocationIntervals;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

public class NavigationOrchestrator implements Runnable {
    private final AtomicBoolean caRun;
    private final RoutesRepository routesRepository;
    private final HashMap<String, UserRouteTracker> availableRoutes;
    private final String defaultKey;
    private final InfoRouteController infoRouteController;

    private UserRouteTrackerNotifier userRouteTrackerNotifier;
    private UserRouteTracker userRouteTracker;

    public NavigationOrchestrator(String defaultKey, InfoRouteController infoRouteController) {
        this.defaultKey = defaultKey;
        this.infoRouteController = infoRouteController;
        caRun = new AtomicBoolean(true);
        routesRepository = RoutesRepository.getInstance();
        availableRoutes = new HashMap<>();
    }

    public void startNavigationMode(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (availableRoutes.isEmpty()){
            setRoute(defaultKey);
        }

        caRun.set(true);
        Thread thread = new Thread(this, "Navigation Thread");
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();
    }

    public void stopLiveUpdates() {
        caRun.set(false);
        availableRoutes.clear();
        routesRepository.clean();
    }

    @Override
    public void run() {
        Timber.d("%s has started", Thread.currentThread().getName());
        while (caRun.get()) {
            if (userRouteTracker.hasUserArrived()){
                infoRouteController.performUserHasArrivedProtocol();
                caRun.set(false);
                continue;

            }else {
                userRouteTracker.update();
                userRouteTrackerNotifier.doNotify();
            }

            if (Thread.currentThread().isInterrupted()) {
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

        if (!caRun.get()) {
            Timber.d("[%s] has finished", Thread.currentThread().getName());
            stopLiveUpdates();
        }
    }

    public void setRoute(String key) {
        long start = System.nanoTime();
        userRouteTracker = availableRoutes.get(key);
        Timber.d("UserRouteTracker is: %s", userRouteTracker);
        Timber.d("Changed to: %s", key);
        if (userRouteTracker == null) {
            userRouteTracker = new UserRouteTracker(routesRepository.getRouteInformation(key));
            userRouteTracker.parseRoute();
            availableRoutes.put(key, userRouteTracker);
        }
        userRouteTrackerNotifier = userRouteTracker.getRouteTrackerNotifier();
        long finish = System.nanoTime();
        Timber.d("Change route execution time : %s ms", ((finish - start)/1_000_000));

    }

    public UserRouteTracker getUserRouteTracker() {
        if (availableRoutes.isEmpty()){
            setRoute(defaultKey);
        }

        return userRouteTracker;
    }
}
