package com.isc.hermes.model.navigation;

import com.isc.hermes.controller.InfoRouteController;
import com.isc.hermes.model.location.LocationIntervals;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteEstimatesManager {
    private final UserRouteTracker userRouteTracker;
    private final InfoRouteController infoRouteController;
    private final TransportationType transportationType;

    private double totalEstimatedDistance;

    public RouteEstimatesManager(UserRouteTracker userRouteTracker, InfoRouteController infoRouteController, TransportationType transportationType) {
        this.userRouteTracker = userRouteTracker;
        this.infoRouteController = infoRouteController;
        this.transportationType = transportationType;
        setTimeDistanceEstimates(userRouteTracker.getRouteInformation());
    }

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

    private void setTimeDistanceEstimates(JSONObject geoJSON){
        try {
            totalEstimatedDistance = geoJSON.getDouble("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateEstimatedArrivalTime();
    }

    private void updateEstimatedArrivalTime(){
        int totalEstimatedArrivalTime = (int) Math.ceil(((totalEstimatedDistance / transportationType.getVelocity()) * 60));

        infoRouteController.setEstimatedTimeInfo(totalEstimatedArrivalTime);
    }

    private void updateEstimatedArrivalDistance(double traveledDistance){
        this.totalEstimatedDistance = totalEstimatedDistance - traveledDistance;
       infoRouteController.setDistanceInfo(totalEstimatedDistance);
    }
}
