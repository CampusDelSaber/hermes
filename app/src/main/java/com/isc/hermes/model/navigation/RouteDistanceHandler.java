package com.isc.hermes.model.navigation;

import java.util.List;

public class RouteDistanceHandler {
    private final List<RouteSegmentRecord> routeSegments;
    private double lastMeasurement;
    private boolean lastSegment;

    public RouteDistanceHandler(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        lastMeasurement = 0.0;
        lastSegment = false;
    }

    public double getDistance(){
        double distance = lastMeasurement;
        if (lastSegment){
            return 0.0;
        }
        return distance;
    }

    public void update(int routeSegmentIndex){
        double totalDistance = 0.0;
        for (int index = routeSegmentIndex; index < routeSegments.size(); index++) {
            totalDistance += routeSegments.get(index).getDistance();
        }
        lastMeasurement = totalDistance;

        lastSegment = (routeSegments.size() - routeSegmentIndex) == 1;
    }
}
