package com.isc.hermes.model.navigation.route_segments;

import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class RouteSegmentRecordBuilder {
    private LatLng start;
    private LatLng end;
    private DirectionsRecord[] directions;

    public RouteSegmentRecordBuilder setStart(LatLng start) {
        this.start = start;
        return this;
    }

    public RouteSegmentRecordBuilder setEnd(LatLng end) {
        this.end = end;
        return this;
    }

    public RouteSegmentRecordBuilder setDirections(DirectionsRecord[] directions) {
        this.directions = directions;
        return this;
    }

    public RouteSegmentRecord createRouteSegmentRecord() {
        return new RouteSegmentRecord(start, end, directions);
    }
}