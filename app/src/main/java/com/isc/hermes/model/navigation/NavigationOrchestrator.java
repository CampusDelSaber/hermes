package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.location.LocationIntervals;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * The NavigationOrchestrator class is responsible for managing navigation and route tracking.
 * It implements the Runnable interface to run navigation in a separate thread.
 */
public class NavigationOrchestrator implements Runnable {
    private final AtomicBoolean caRun;
    private final RoutesRepository routesRepository;
    private final HashMap<String, UserRouteTracker> availableRoutes;
    private final String defaultKey;
    private final InfoRouteController infoRouteController;

    private UserRouteTrackerNotifier userRouteTrackerNotifier;
    private UserRouteTracker userRouteTracker;

    /**
     * Constructs a new NavigationOrchestrator object with the specified default key and InfoRouteController.
     *
     * @param defaultKey           the default key for the route
     * @param infoRouteController  the InfoRouteController instance for handling route information
     */
    public NavigationOrchestrator(String defaultKey, InfoRouteController infoRouteController) {
        this.defaultKey = defaultKey;
        this.infoRouteController = infoRouteController;
        caRun = new AtomicBoolean(true);
        routesRepository = RoutesRepository.getInstance();
        availableRoutes = new HashMap<>();
    }

    /**
     * Starts the navigation mode in a separate thread with the specified UncaughtExceptionHandler.
     *
     * @param uncaughtExceptionHandler the UncaughtExceptionHandler for handling uncaught exceptions in the thread
     */
    public void startNavigationMode(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (availableRoutes.isEmpty()) {
            setRoute(defaultKey);
        }

        caRun.set(true);
        Thread thread = new Thread(this, "Navigation Thread");
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();
    }

    /**
     * Stops the live updates and clears the available routes.
     */
    public void stopLiveUpdates() {
        caRun.set(false);
        availableRoutes.clear();
        routesRepository.clean();
    }

    /**
     * Runs the navigation logic in a separate thread.
     */
    @Override
    public void run() {
        Timber.d("%s has started", Thread.currentThread().getName());
        while (caRun.get()) {
            if (userRouteTracker.hasUserArrived()) {
                infoRouteController.performUserHasArrivedProtocol();
                caRun.set(false);
                continue;
            } else {
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

    /**
     * Sets the route with the specified key.
     *
     * @param key the key of the route
     */
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
        Timber.d("Change route execution time: %s ms", ((finish - start) / 1_000_000));
    }

    /**
     * Gets the UserRouteTracker instance.
     * If no routes are available, it sets the default route before returning the instance.
     *
     * @return the UserRouteTracker instance
     */
    public UserRouteTracker getUserRouteTracker() {
        if (availableRoutes.isEmpty()) {
            setRoute(defaultKey);
        }

        return userRouteTracker;
    }
}
