package com.isc.hermes.model.navigation;

import android.annotation.SuppressLint;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class UserOutsideRouteException extends RuntimeException{
    @SuppressLint("DefaultLocale")
    public UserOutsideRouteException(RouteSegmentRecord segmentRecord, LatLng userLocation) {
        super(String.format("The user at [%s, %s] is outside the boundaries of the segment with: \n Start: [%s, %s]\n End: [%s. %s]",
                userLocation.getLatitude(), userLocation.getLongitude(),
                segmentRecord.getStart().getLatitude(), segmentRecord.getStart().getLongitude(),
                segmentRecord.getEnd().getLongitude(), segmentRecord.getEnd().getLongitude()));
    }
}
