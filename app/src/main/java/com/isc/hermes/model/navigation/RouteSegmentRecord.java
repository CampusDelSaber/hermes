package com.isc.hermes.model.navigation;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class RouteSegmentRecord {
    private final LatLng start;
    private final LatLng end;

    public RouteSegmentRecord(LatLng start, LatLng end) {
        this.start = start;
        this.end = end;
    }

    public LatLng getStart() {
        return start;
    }

    public LatLng getEnd() {
        return end;
    }

}
