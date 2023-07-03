package com.isc.hermes.model.navigation.route_segments;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class RouteSegmentRecordBuilder {
    private LatLng start;
    private LatLng end;

    public RouteSegmentRecordBuilder setStart(LatLng start) {
        this.start = start;
        return this;
    }

    public RouteSegmentRecordBuilder setEnd(LatLng end) {
        this.end = end;
        return this;
    }

    public RouteSegmentRecord createRouteSegmentRecord() {
        return new RouteSegmentRecord(start, end);
    }
}