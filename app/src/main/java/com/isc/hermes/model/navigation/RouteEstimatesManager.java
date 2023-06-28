package com.isc.hermes.model.navigation;

import com.isc.hermes.model.location.LocationIntervals;

public class RouteEstimatesManager {
    private final Route route;
    private final UserRouteTracker userRouteTracker;

    public RouteEstimatesManager(Route route, UserRouteTracker userRouteTracker) {
        this.route = route;
        this.userRouteTracker = userRouteTracker;
    }

    public void startEstimatesTrack() {
        while (!userRouteTracker.hasUserArrived()) {
            route.updateDistance(userRouteTracker.getPartialSegmentDistance());
            route.updateEstimatedArrivalTime();

            try {
                Thread.currentThread().wait((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
            } catch (InterruptedException e) {
                // TODO: Handle exception properly.
                e.printStackTrace();
            }
        }
    }
}
