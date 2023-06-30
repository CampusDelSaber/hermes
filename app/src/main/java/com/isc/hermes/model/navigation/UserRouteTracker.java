package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import timber.log.Timber;

/**
 * The UserRouteTracker class tracks the user's route and provides information about the user's progress.
 */
public class UserRouteTracker {
    public static final int GEO_JSON_LATITUDE = 1;
    private static final int GEO_JSON_LONGITUDE = 0;
    private final CoordinatesDistanceCalculator distanceCalculator;
    private final CurrentLocationModel currentLocation;
    private final ListIterator<RouteSegmentRecord> availableSegments;
    private LatLng destination;
    private RouteSegmentRecord currentRouteSegmentRecord;
    private JSONObject routeInformation;
    private LatLng lastLocation;

    /**
     * Constructs a UserRouteTracker object with the provided GeoJSON route information.
     *
     * @param geoJSON The GeoJSON route information.
     */
    public UserRouteTracker(String geoJSON) {
        try {
            routeInformation = new JSONObject(geoJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentRouteSegmentRecord = null;
        distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        currentLocation = CurrentLocationModel.getInstance();
        availableSegments = makeRouteSegments().listIterator();
        lastLocation = currentLocation.getLatLng();
        try {
            updateCurrentRouteSegment(currentLocation.getLatLng());
        } catch (UserOutsideRouteException e) {
            Timber.e(e.getMessage());
        }
    }

    /**
     * Gets the distance remaining in the current route segment until reaching the end point.
     *
     * @return The distance remaining in the current route segment.
     */
    public double getTraveledDistance() {
        LatLng userLocation = currentLocation.getLatLng();
        if (!isUserInSegment(userLocation)) {
            try {
                updateCurrentRouteSegment(userLocation);
            } catch (UserOutsideRouteException e) {
                Timber.e(e.getMessage());
            }
        }
        double calculateDistance = distanceCalculator.calculateDistance(
                userLocation,
                currentRouteSegmentRecord.getEnd()
        );
        return calculateDistance;
    }

    /**
     * Updates the current route segment based on the user's current location.
     * If there are available segments, the next segment is assigned as the current segment.
     * If the user is outside the current segment, a UserOutsideRouteException is thrown.
     *
     * @param userLocation The user's current location.
     * @throws UserOutsideRouteException If the user is outside the current route segment.
     */
    private void updateCurrentRouteSegment(LatLng userLocation) throws UserOutsideRouteException {
        if (availableSegments.hasNext()) {
            currentRouteSegmentRecord = availableSegments.next();
            if (!NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation)) {
                throw new UserOutsideRouteException(currentRouteSegmentRecord, userLocation);
            }
        }
    }

    /**
     * Creates a list of route segments from the route information.
     *
     * @return The list of route segments.
     */
    private List<RouteSegmentRecord> makeRouteSegments() {
        List<RouteSegmentRecord> routeSegments = new ArrayList<>();
        try {
            JSONArray route = routeInformation.getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < 10000; i++) {
                if (route.optJSONArray(i) != null) {
                    if (route.optJSONArray(i + 1) != null) {
                        JSONArray start = route.getJSONArray(i);
                        JSONArray end = route.getJSONArray(i + 1);
                        routeSegments.add(new RouteSegmentRecord(
                                new LatLng(start.getDouble(GEO_JSON_LATITUDE), start.getDouble(GEO_JSON_LONGITUDE)),
                                new LatLng(end.getDouble(GEO_JSON_LATITUDE), end.getDouble(GEO_JSON_LONGITUDE))
                        ));
                    } else {
                        JSONArray dest = route.getJSONArray(i);
                        destination = new LatLng(dest.getDouble(GEO_JSON_LATITUDE), dest.getDouble(GEO_JSON_LONGITUDE));
                        break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeSegments;
    }

    /**
     * Checks if the user has arrived at the destination point.
     *
     * @return true if the user has arrived, false otherwise.
     */
    public boolean hasUserArrived() {
        return NavigationTrackerTools.isPointReached(destination, currentLocation.getLatLng());
    }

    /**
     * Checks if the user is inside the current route segment.
     *
     * @param userLocation The user's current location.
     * @return true if the user is inside the current route segment, false otherwise.
     */
    public boolean isUserInSegment(LatLng userLocation) {
        boolean isInsideSegment = NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation);
        boolean isEndReached = NavigationTrackerTools.isPointReached(currentRouteSegmentRecord.getEnd(), userLocation);

        return isInsideSegment && !isEndReached;
    }

    /**
     * Gets the route information in the form of a JSON object.
     *
     * @return The route information JSON object.
     */
    public JSONObject getRouteInformation() {
        return routeInformation;
    }

    /**
     * Checks if the user has moved based on their current location compared to the last known location.
     *
     * @return true if the user has moved, false otherwise.
     */
    public boolean hasUserMoved() {
        LatLng userLocation = currentLocation.getLatLng();
        boolean hasUserMoved = NavigationTrackerTools.hasUserMoved(userLocation, lastLocation);
        if (hasUserMoved) {
            lastLocation = userLocation;
        }

        return hasUserMoved;
    }

}
