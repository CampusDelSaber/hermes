package com.isc.hermes.model.navigation;

import java.util.List;

public class RouteDistanceHandler {
    private final List<RouteSegmentRecord> routeSegments;
    private double lastMeasurement;

    public RouteDistanceHandler(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        lastMeasurement = 0.0;
    }

    public double getDistance(){
        return lastMeasurement;
    }

    public void update(int routeSegmentIndex){
        double totalDistance = 0.0;
        for (int index = routeSegmentIndex; index < routeSegments.size(); index++) {
            totalDistance += routeSegments.get(index).getDistance();
        }
        lastMeasurement = totalDistance;
    }
}
