package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.location.LocationIntervals;

public class RouteEstimatesManager {
    private final Route route;
    private final UserRouteTracker userRouteTracker;
    private final InfoRouteController infoRouteController;

    public RouteEstimatesManager(Route route, UserRouteTracker userRouteTracker, InfoRouteController infoRouteController) {
        this.route = route;
        this.userRouteTracker = userRouteTracker;
        this.infoRouteController = infoRouteController;
    }

    public void startEstimatesTrack() {
        while (!userRouteTracker.hasUserArrived()) {
            route.updateDistance(userRouteTracker.getPartialSegmentDistance());
            route.updateEstimatedArrivalTime();

            infoRouteController.setDistanceInfo(route.getTotalEstimatedDistance());
            infoRouteController.setEstimatedTimeInfo(route.getTotalEstimatedArrivalTime());

            try {
                Thread.sleep((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
            } catch (InterruptedException e) {
                // TODO: Handle exception properly.
                e.printStackTrace();
            }
        }
    }
}
