package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.location.LocationIntervals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * The RouteEstimatesManager class manages the estimates for route time and distance during navigation.
 */
public class RouteEstimatesManager {
    private UserRouteTracker currentRoute;
    private final InfoRouteController infoRouteController;
    private final TransportationType transportationType;

    private double totalEstimatedDistance;
    private Thread liveUpdateThread;

    private HashMap<String, UserRouteTracker> availableRoutes;

    /**
     * Constructs a new RouteEstimatesManager object.
     *
     * @param infoRouteController The InfoRouteController instance for updating route information.
     * @param transportationType  The transportation type used for estimating arrival time.
     * @param availableRoutes
     */
    public RouteEstimatesManager(InfoRouteController infoRouteController, TransportationType transportationType, HashMap<String, UserRouteTracker> availableRoutes) {
        this.availableRoutes = availableRoutes;
        this.infoRouteController = infoRouteController;
        this.transportationType = transportationType;
        setTimeDistanceEstimates(currentRoute.getRouteInformation());
    }

    /**
     * Starts updating the estimates for arrival time and distance.
     */
    public void startLiveUpdate() {
        liveUpdateThread = new Thread(() -> {
            while (!currentRoute.hasUserArrived()) {
                updateEstimatedArrivalDistance(currentRoute.getPartialSegmentDistance());
                updateEstimatedArrivalTime();
                try {
                    Thread.sleep((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
                } catch (InterruptedException e) {
                    // TODO: Handle exception properly.
                    e.printStackTrace();
                }
            }
        }, "Live estimations");
    }

    /**
     * Sets the time and distance estimates based on the provided geoJSON object.
     *
     * @param geoJSON The JSON object containing route information.
     */
    private void setTimeDistanceEstimates(JSONObject geoJSON) {
        try {
            totalEstimatedDistance = geoJSON.getDouble("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateEstimatedArrivalTime();
    }

    /**
     * Updates the estimated arrival time based on the total estimated distance and transportation type.
     */
    private void updateEstimatedArrivalTime() {
        int totalEstimatedArrivalTime = (int) Math.ceil(((totalEstimatedDistance / transportationType.getVelocity()) * 60));

        infoRouteController.setEstimatedTimeInfo(totalEstimatedArrivalTime);
    }

    /**
     * Updates the estimated arrival distance based on the traveled distance.
     *
     * @param traveledDistance The distance traveled by the user.
     */
    private void updateEstimatedArrivalDistance(double traveledDistance) {
        this.totalEstimatedDistance = totalEstimatedDistance - traveledDistance;
        infoRouteController.setDistanceInfo(totalEstimatedDistance);
    }

    public void stopLiveUpdate() {
        liveUpdateThread.interrupt();
    }

    public void switchRoute(String routeName){
        currentRoute = availableRoutes.get(routeName);
    }
}
