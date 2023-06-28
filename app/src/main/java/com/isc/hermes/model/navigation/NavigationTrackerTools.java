package com.isc.hermes.model.navigation;

import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class NavigationTrackerTools {
    public static double CLOSE_ENOUGH = 10;

    public static boolean isPointReached(LatLng target, LatLng point) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        double distance = distanceCalculator.calculateDistance(point, target);
        return distance <= CLOSE_ENOUGH;
    }

    public static boolean isInsideSegment(RouteSegmentRecord segment, LatLng point) {
        boolean isHigherThan = (point.getLatitude() >= segment.getStart().getLatitude());
        isHigherThan &= (point.getLongitude() >= segment.getStart().getLongitude());

        boolean isLowerThan = (point.getLatitude() <= segment.getEnd().getLatitude());
        isLowerThan &= (point.getLongitude() <= segment.getEnd().getLongitude());

        return isHigherThan && isLowerThan;
    }
}
