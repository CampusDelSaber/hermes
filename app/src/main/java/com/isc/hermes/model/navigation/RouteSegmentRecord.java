package com.isc.hermes.model.navigation;

import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The RouteSegmentRecord class represents a segment of a route defined by its start and end coordinates.
 */
public class RouteSegmentRecord {
    private final LatLng start;
    private final LatLng end;
    private final double distance;

    /**
     * Constructs a new RouteSegmentRecord object.
     *
     * @param start The start coordinates of the route segment.
     * @param end   The end coordinates of the route segment.
     */
    public RouteSegmentRecord(LatLng start, LatLng end) {
        this.start = start;
        this.end = end;
        this.distance = CoordinatesDistanceCalculator.getInstance().calculateDistance(start, end);
    }

    /**
     * Returns the start coordinates of the route segment.
     *
     * @return The start coordinates.
     */
    public LatLng getStart() {
        return start;
    }

    /**
     * Returns the end coordinates of the route segment.
     *
     * @return The end coordinates.
     */
    public LatLng getEnd() {
        return end;
    }

    public double getDistance() {
        return distance;
    }
}
