package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;
import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointReached;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.navigation.exceptions.RouteOutOfTracksException;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import timber.log.Timber;

/**
 * The UserRouteTracker class tracks the user's route and provides information about the user's progress.
 */
public class UserRouteTracker {

    private final CoordinatesDistanceCalculator distanceCalculator;
    private final CurrentLocationModel currentLocation;
    private final UserLocationTracker userLocationTracker;
    private RouteDistanceHandler routeDistanceHandler;
    private TrackRecoveryHandler trackRecoveryHandler;

    private List<RouteSegmentRecord> routeSegments;
    private RouteSegmentRecord currentSegment;
    private int routeSegmentIndex;
    private JSONObject routeInformation;
    private LatLng usersDestination;
    private boolean isUserTrackLost;

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

        distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        currentLocation = CurrentLocationModel.getInstance();
        userLocationTracker = new UserLocationTracker();
        routeSegmentIndex = 0;
        isUserTrackLost = false;
    }

    public void parseRoute() {
        routeSegments = NavigationRouteParser.makeRouteSegments(routeInformation);
        if (routeSegments.isEmpty()) {
            throw new RouteOutOfTracksException("Could not start the navigation, route is empty");
        }
        usersDestination = routeSegments.get(routeSegments.size() - 1).getEnd();
        routeSegments.get(0).setStart(currentLocation.getLatLng());
        trackRecoveryHandler = new TrackRecoveryHandler(routeSegments);
        routeDistanceHandler = new RouteDistanceHandler(routeSegments);
        Timber.i(String.format("Starting route with: %s Tracks\n", routeSegments.size()));
        nextTrack(currentLocation.getLatLng());
    }

    /**
     * Gets the distance remaining in the current route segment until reaching the end point.
     *
     * @return The distance remaining in the current route segment.
     */
    public double getTraveledDistance() {
        LatLng userLocation = currentLocation.getLatLng();
        if (isUserTrackLost) {
            recoverTrack(true);
        }

        if (isUserOnTrack(userLocation)) {
            Timber.i(String.format("User on track #%d\n", routeSegmentIndex));
            return distanceCalculator.calculateDistance(
                    userLocation,
                    currentSegment.getEnd()
            );
        } else {
            nextTrack(userLocation);
        }

        return -0.0;
    }

    /**
     * Updates the current route segment based on the user's current location.
     * If there are available segments, the next segment is assigned as the current segment.
     * If the user is outside the current segment, a UserOutsideRouteException is thrown.
     *
     * @param userLocation The user's current location.
     */
    private void nextTrack(LatLng userLocation) {
        if (!routeSegments.isEmpty() && routeSegmentIndex < routeSegments.size()) {
            currentSegment = routeSegments.get(routeSegmentIndex);
            if (isPointInsideSegment(currentSegment, userLocation)) {
                routeSegmentIndex++;
                routeDistanceHandler.update(routeSegmentIndex);
            } else {
                recoverTrack(false);
            }

        } else {
            throw new RouteOutOfTracksException("The current route does not have any more unvisited tracks");
        }
    }

    /**
     * Checks if the user has arrived at the destination point.
     *
     * @return true if the user has arrived, false otherwise.
     */
    public boolean hasUserArrived() {
        boolean pointReached = isPointReached(usersDestination, currentLocation.getLatLng());
        if (pointReached) {
            Timber.i("USER HAS ARRIVED LOCATION");
        }
        return pointReached;
    }

    /**
     * Checks if the user is inside the current route segment.
     *
     * @param userLocation The user's current location.
     * @return true if the user is inside the current route segment, false otherwise.
     */
    public boolean isUserOnTrack(LatLng userLocation) {
        boolean isInsideSegment = isPointInsideSegment(currentSegment, userLocation);
        boolean isEndReached = isPointReached(currentSegment.getEnd(), userLocation);
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

    public double getUnvisitedRouteSize() {
        return routeDistanceHandler.getDistance();
    }

    public boolean hasUserMoved() {
        return userLocationTracker.hasUserMoved();
    }

    private void recoverTrack(boolean deepAttempt) {
        if (deepAttempt) {
            trackRecoveryHandler.attemptDeepRecovery();
        } else {
            trackRecoveryHandler.attemptSimpleRecovery(routeSegmentIndex);
        }

        if (trackRecoveryHandler.isAttemptSuccessful()) {
            currentSegment = trackRecoveryHandler.getTrack();
            routeSegmentIndex = routeSegments.indexOf(currentSegment);
            routeDistanceHandler.update(routeSegmentIndex);
            Timber.d("User on track #%d\n", routeSegmentIndex);
            isUserTrackLost = false;

        } else if (deepAttempt) {
            Timber.e("Deep recovery has failed");
        } else {
            isUserTrackLost = true;
            Timber.e("Track of the user has been lost");
        }
    }
}
