package com.isc.hermes.model.navigation.exceptions;
import android.annotation.SuppressLint;

import com.isc.hermes.model.navigation.RouteSegmentRecord;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * This exception is thrown when a user is outside the boundaries of a route segment.
 */
public class UserOutsideTrackException extends Exception {
    /**
     * Constructs a new UserOutsideRouteException with the specified segment record and user location.
     *
     * @param segmentRecord the route segment record
     * @param userLocation  the user's location
     */
    @SuppressLint("DefaultLocale")
    public UserOutsideTrackException(RouteSegmentRecord segmentRecord, LatLng userLocation) {
        super(String.format("The user at [%s, %s] is outside the boundaries of the segment with:\n" +
                        "Start: [%s, %s]\n" +
                        "End: [%s, %s]",
                userLocation.getLatitude(), userLocation.getLongitude(),
                segmentRecord.getStart().getLatitude(), segmentRecord.getStart().getLongitude(),
                segmentRecord.getEnd().getLatitude(), segmentRecord.getEnd().getLongitude()));
    }
}
