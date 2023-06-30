package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.controller.TrafficAutomaticFormController;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.model.location.LocationIntervals;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The RouteEstimatesManager class manages the estimates for route time and distance during navigation.
 */
public class RouteEstimatesManager {
    private final UserRouteTracker userRouteTracker;
    private final InfoRouteController infoRouteController;
    private final TransportationType transportationType;
    private double totalEstimatedDistance;

    /**
     * Constructs a new RouteEstimatesManager object.
     *
     * @param userRouteTracker     The UserRouteTracker instance for tracking the user's route.
     * @param infoRouteController  The InfoRouteController instance for updating route information.
     * @param transportationType   The transportation type used for estimating arrival time.
     */
    public RouteEstimatesManager(UserRouteTracker userRouteTracker, InfoRouteController infoRouteController, TransportationType transportationType) {
        this.userRouteTracker = userRouteTracker;
        this.infoRouteController = infoRouteController;
        this.transportationType = transportationType;
        setTimeDistanceEstimates(userRouteTracker.getRouteInformation());
    }



    /**
     * Starts updating the estimates for arrival time and distance.
     */
    public void startUpdatingEstimations() {
        while (!userRouteTracker.hasUserArrived()) {
            updateEstimatedArrivalDistance(userRouteTracker.getPartialSegmentDistance());
            updateEstimatedArrivalTime();
            try {
                Thread.sleep((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
            } catch (InterruptedException e) {
                // TODO: Handle exception properly.
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the time and distance estimates based on the provided geoJSON object.
     *
     * @param geoJSON The JSON object containing route information.
     */
    private void setTimeDistanceEstimates(JSONObject geoJSON){
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
    private void updateEstimatedArrivalTime(){
        int totalEstimatedArrivalTime = (int) Math.ceil(((totalEstimatedDistance / transportationType.getVelocity()) * 60));

        infoRouteController.setEstimatedTimeInfo(totalEstimatedArrivalTime);
        infoRouteController.setTimeEstimate(totalEstimatedArrivalTime);
    }


    /**
     * Updates the estimated arrival distance based on the traveled distance.
     *
     * @param traveledDistance The distance traveled by the user.
     */
    private void updateEstimatedArrivalDistance(double traveledDistance){
        this.totalEstimatedDistance = totalEstimatedDistance - traveledDistance;
        infoRouteController.setDistanceInfo(totalEstimatedDistance);
    }

}
