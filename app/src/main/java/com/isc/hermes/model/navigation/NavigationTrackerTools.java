package com.isc.hermes.model.navigation;

import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The NavigationTrackerTools class provides utility methods for navigation tracking.
 */
public class NavigationTrackerTools {
    public static double USER_REACHED_DESTINATION_CRITERIA = 0.005;
    public static double USER_MOVED_CRITERIA = 0.001;
    public static double USER_ON_TRACK_CRITERIA = 0.08;

    /**
     * Checks if a point has been reached based on the target and the current point.
     *
     * @param target The target point to reach.
     * @param point  The current point.
     * @return true if the point has been reached, false otherwise.
     */
    public static boolean isPointReached(LatLng target, LatLng point) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        double distance = distanceCalculator.calculateDistance(point, target);
        int comparative = Double.compare(distance, USER_REACHED_DESTINATION_CRITERIA);
        return comparative <= 0;
    }

    /**
     * Checks if a point is inside a given route segment.
     *
     * @param segment The route segment.
     * @param point   The point to check.
     * @return true if the point is inside the segment, false otherwise.
     */
    public static boolean isPointInsideSegment(RouteSegmentRecord segment, LatLng point, boolean isLog) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        LatLng lineStart = segment.getStart();
        LatLng lineEnd = segment.getEnd();

        int floorComparative = Double.compare(lineStart.getLatitude(), point.getLatitude());
        int ceilComparative = Double.compare(lineEnd.getLatitude(), point.getLatitude());
        boolean isInsideHorizontalBounds  = (floorComparative >= 0) && (ceilComparative <= 0);
        if (isLog){
            System.err.println(String.format("INSIDE HORIZONTAL BOUNDARIES STATUS: %s", isInsideHorizontalBounds));
        }

        double pointToLineDistance = distanceCalculator.pointToLineDistance(point, lineStart, lineEnd);
        if (isLog){
            System.err.println(String.format("User distance to point: %s", pointToLineDistance));
        }

        boolean isInsideVerticalBounds = (Double.compare(pointToLineDistance, USER_ON_TRACK_CRITERIA) <= 0);
        if (isLog){
            System.err.println(String.format("INSIDE VERTICAL BOUNDARIES STATUS: %s", isInsideHorizontalBounds));
        }

        return isInsideHorizontalBounds && isInsideVerticalBounds;
    }

    /**
     * Determines if a user has moved based on the distance between their current location
     * and their last known location.
     *
     * @param userLocation  The current location of the user.
     * @param lastLocation  The last known location of the user.
     * @return              {@code true} if the user has moved beyond the defined criteria,
     *                      {@code false} otherwise.
     */
    public static boolean hasUserMoved(LatLng userLocation, LatLng lastLocation) {
        CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        double movement = distanceCalculator.calculateDistance(userLocation, lastLocation);
        return Double.compare(movement, USER_MOVED_CRITERIA) > 0;
    }
}
