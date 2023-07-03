package com.isc.hermes.model.navigation;

import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;

import java.util.List;

/**
 * The RouteDistanceHandler class calculates the total distance of a route based on its segments.
 */
public class RouteDistanceHelper {
    private final List<RouteSegmentRecord> routeSegments;
    private double lastMeasurement;
    private boolean lastSegment;

    /**
     * Constructs a new RouteDistanceHandler object.
     *
     * @param routeSegments The list of route segments.
     */
    public RouteDistanceHelper(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        lastMeasurement = 0.0;
        lastSegment = false;
    }

    /**
     * Gets the total distance of the route.
     *
     * @return The total distance of the route.
     */
    public double getDistance() {
        double distance = lastMeasurement;
        if (lastSegment) {
            return 0.0;
        }
        return distance;
    }

    /**
     * Updates the route distance based on the current route segment index.
     *
     * @param routeSegmentIndex The current route segment index.
     */
    public void updateTrackIndex(int routeSegmentIndex) {
        double totalDistance = 0.0;
        for (int index = routeSegmentIndex; index < routeSegments.size(); index++) {
            totalDistance += routeSegments.get(index).getDistance();
        }
        lastMeasurement = totalDistance;

        lastSegment = (routeSegments.size() - routeSegmentIndex) == 1;
    }
}
