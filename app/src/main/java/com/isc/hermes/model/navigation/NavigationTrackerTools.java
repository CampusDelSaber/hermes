package com.isc.hermes.model.navigation;

import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The NavigationTrackerTools class provides utility methods for navigation tracking.
 */
public class NavigationTrackerTools {
    public static double DESTINATION_REACHED_CRITERIA = 0.005;
    public static double USER_MOVED_CRITERIA = 0.001;
    public static double USER_ON_TRACK_CRITERIA = 0.001;
    public static double USER_IN_RANGE_PRECISION = 0.0001;

    /**
     * Checks if a point has been reached based on the target and the current point.
     *
     * @param target The target point to reach.
     * @param point  The current point.
     * @return true if the point has been reached, false otherwise.
     */
    public static boolean isNearPoint(LatLng target, LatLng point) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        double distance = distanceCalculator.calculateDistance(point, target);
        int comparative = Double.compare(distance, DESTINATION_REACHED_CRITERIA);
        return comparative <= 0;
    }

    /**
     * Checks if a point is inside a given route segment.
     *
     * @param segment The route segment.
     * @param point   The point to check.
     * @return true if the point is inside the segment, false otherwise.
     */
    public static boolean isPointInsideSegment(RouteSegmentRecord segment, LatLng point) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        LatLng lineStart = segment.getStart();
        LatLng lineEnd = segment.getEnd();

        boolean isInsideHorizontalBounds  = isInRange(lineStart, lineEnd, point, USER_IN_RANGE_PRECISION);
        double pointToLineDistance = distanceCalculator.pointToLineDistance(point, lineStart, lineEnd);
        boolean isInsideVerticalBounds = (Double.compare(pointToLineDistance, USER_ON_TRACK_CRITERIA) <= 0);

        return isInsideHorizontalBounds && isInsideVerticalBounds;
    }

    /**
     * Checks if a point is within a range defined by a floor and ceil value with a given precision.
     *
     * @param floor     The floor value.
     * @param ceil      The ceil value.
     * @param point     The point to check.
     * @param precision The precision for defining the range.
     * @return true if the point is within the range, false otherwise.
     */
    public static boolean isInRange(LatLng floor, LatLng ceil, LatLng point, double precision) {
        double minLat = Math.min(floor.getLatitude(), ceil.getLatitude());
        double maxLat = Math.max(floor.getLatitude(), ceil.getLatitude());
        double minLng = Math.min(floor.getLongitude(), ceil.getLongitude());
        double maxLng = Math.max(floor.getLongitude(), ceil.getLongitude());

        return (point.getLatitude() >= minLat - precision) &&
                (point.getLatitude() <= maxLat + precision) &&
                (point.getLongitude() >= minLng - precision) &&
                (point.getLongitude() <= maxLng + precision);
    }

    /**
     * Determines if a user has moved based on the distance between their current location
     * and their last known location.
     *
     * @param userLocation The current location of the user.
     * @param lastLocation The last known location of the user.
     * @return true if the user has moved beyond the defined criteria, false otherwise.
     */
    public static boolean hasUserMoved(LatLng userLocation, LatLng lastLocation) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        double movement = distanceCalculator.calculateDistance(userLocation, lastLocation);
        return Double.compare(movement, USER_MOVED_CRITERIA) > 0;
    }
}
