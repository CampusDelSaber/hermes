package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.navigation.events.Subscriber;

/**
 * The LiveRouteEstimationsWorker class manages the estimates for route time and distance during navigation.
 */
public class LiveRouteEstimationsWorker implements Subscriber {
    private final UserRouteTracker userRouteTracker;
    private final InfoRouteController infoRouteController;
    private final TransportationType transportationType;

    /**
     * Constructs a new LiveRouteEstimationsWorker object.
     *
     * @param userRouteTracker    The UserRouteTracker instance for tracking the user's route.
     * @param infoRouteController The InfoRouteController instance for updating route information.
     * @param transportationType  The transportation type used for estimating arrival time.
     */
    public LiveRouteEstimationsWorker(UserRouteTracker userRouteTracker, InfoRouteController infoRouteController, TransportationType transportationType) {
        this.userRouteTracker = userRouteTracker;
        this.infoRouteController = infoRouteController;
        this.transportationType = transportationType;
        userRouteTracker.getRouteTrackerNotifier().subscribe(this);
    }

    /**
     * Updates the estimated arrival time based on the total estimated distance and transportation type.
     *
     * @param distance The total estimated distance.
     */
    private void updateEstimatedArrivalTime(double distance) {
        int totalEstimatedArrivalTime = (int) Math.ceil(((distance / transportationType.getVelocity()) * 60));
        infoRouteController.setEstimatedTimeInfo(totalEstimatedArrivalTime);
        infoRouteController.setTimeEstimate(totalEstimatedArrivalTime);
    }

    /**
     * Updates the estimated arrival distance based on the traveled distance.
     *
     * @param traveledDistance The distance traveled by the user.
     */
    private void updateEstimatedArrivalDistance(double traveledDistance) {
        infoRouteController.setDistanceInfo(traveledDistance);
    }

    @Override
    public void update() {
        if (userRouteTracker.hasUserMoved()) {
            double distanceLeft = userRouteTracker.getTraveledDistance() + userRouteTracker.getUnvisitedRouteSize();
            updateEstimatedArrivalDistance(distanceLeft);
            updateEstimatedArrivalTime(distanceLeft);
        }
    }
}