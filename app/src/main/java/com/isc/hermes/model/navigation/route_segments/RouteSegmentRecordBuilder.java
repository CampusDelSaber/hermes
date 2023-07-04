package com.isc.hermes.model.navigation.route_segments;

import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The RouteSegmentRecordBuilder class is used to construct RouteSegmentRecord objects
 * by setting the start and end points of a route segment.
 */
public class RouteSegmentRecordBuilder {
    private LatLng start;
    private LatLng end;
    private DirectionsRecord[] directions;

    /**
     * Sets the start point of the route segment.
     *
     * @param start the start point of the route segment
     * @return the RouteSegmentRecordBuilder instance
     */
    public RouteSegmentRecordBuilder setStart(LatLng start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the end point of the route segment.
     *
     * @param end the end point of the route segment
     * @return the RouteSegmentRecordBuilder instance
     */
    public RouteSegmentRecordBuilder setEnd(LatLng end) {
        this.end = end;
        return this;
    }

    public RouteSegmentRecordBuilder setDirections(DirectionsRecord[] directions) {
        this.directions = directions;
        return this;
    }

    /**
     * Creates a RouteSegmentRecord object using the provided start and end points.
     *
     * @return a new RouteSegmentRecord instance
     */
    public RouteSegmentRecord createRouteSegmentRecord() {
        return new RouteSegmentRecord(start, end, directions);
    }
}
