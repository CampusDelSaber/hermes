package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isNearPoint;
import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.navigation.exceptions.RouteOutOfTracksException;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
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
    private final UserRouteTrackerNotifier routeTrackerNotifier;
    private final NavigationRouteParser navigationRouteParser;

    private RouteDistanceHelper routeDistanceHelper;
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
        routeTrackerNotifier = new UserRouteTrackerNotifier();
        navigationRouteParser = new NavigationRouteParser();
        routeSegmentIndex = 0;
        isUserTrackLost = false;
    }

    /**
     * Updates the user's location and determines the next action based on the current state.
     * If the user's track is lost, it attempts to recover the track.
     * If the user is on the current track segment, it logs the track index.
     * If the user is off the current track segment, it proceeds to the next track segment.
     *
     * @throws RouteOutOfTracksException if the current route has no more unvisited tracks
     */
    public void update() {
        LatLng userLocation = currentLocation.getLatLng();

        if (isUserTrackLost) {
            recoverTrack(true);
        }

        if (routeSegmentIndex >= routeSegments.size()) {
            throw new RouteOutOfTracksException("The current route does not have any more unvisited tracks");
        }

        if (isUserOnTrack(userLocation)) {
            Timber.i("User on track #%d\n", routeSegmentIndex);
        } else {
            recoverTrack(false);
            Timber.d("Track has been switched");
        }
    }

    /**
     * Parses the route information and initializes the necessary objects for tracking the user's route.
     *
     * @throws RouteOutOfTracksException If the route is empty and navigation cannot be started.
     */
    public void parseRoute() throws RouteOutOfTracksException {
        routeSegments = navigationRouteParser.makeRouteSegments(routeInformation);
        if (routeSegments.isEmpty()) {
            throw new RouteOutOfTracksException("Could not start the navigation, route is empty");
        }

        usersDestination = routeSegments.get(routeSegments.size() - 1).getEnd();
        trackRecoveryHandler = new TrackRecoveryHandler(routeSegments);
        routeDistanceHelper = new RouteDistanceHelper(routeSegments);
        Timber.d(String.format("Starting route with: %s Tracks\n", routeSegments.size()));
        currentSegment = routeSegments.get(routeSegmentIndex);
    }

    /**
     * Gets the distance remaining in the current route segment until reaching the end point.
     *
     * @return The distance remaining in the current route segment.
     */
    public double getTraveledDistance() {
        return distanceCalculator.calculateDistance(
                currentLocation.getLatLng(),
                currentSegment.getEnd()
        );
    }

    /**
     * Checks if the user has arrived at the destination point.
     *
     * @return true if the user has arrived, false otherwise.
     */
    public boolean hasUserArrived() {
        return isNearPoint(usersDestination, currentLocation.getLatLng());
    }

    /**
     * Checks if the user is inside the current route segment.
     *
     * @param userLocation The user's current location.
     * @return true if the user is inside the current route segment, false otherwise.
     */
    public boolean isUserOnTrack(LatLng userLocation) {
        boolean isInsideSegment = isPointInsideSegment(currentSegment, userLocation);
        boolean isEndReached = isNearPoint(currentSegment.getEnd(), userLocation);
        return isInsideSegment && !isEndReached;
    }

    /**
     * Gets the distance remaining in the unvisited portion of the route.
     *
     * @return The distance remaining in the unvisited portion of the route.
     */
    public double getUnvisitedRouteSize() {
        return routeDistanceHelper.getDistance();
    }

    /**
     * Checks if the user has moved from the last recorded location.
     *
     * @return true if the user has moved, false otherwise.
     */
    public boolean hasUserMoved() {
        return userLocationTracker.hasUserMoved();
    }

    /**
     * Recovers the track based on the specified deep attempt flag.
     *
     * @param deepAttempt Determines whether to perform a deep recovery attempt.
     */
    private void recoverTrack(boolean deepAttempt) {
        if (deepAttempt) {
            trackRecoveryHandler.attemptDeepRecovery();
        } else {
            trackRecoveryHandler.attemptSimpleRecovery(routeSegmentIndex);
        }

        if (trackRecoveryHandler.isAttemptSuccessful()) {
            currentSegment = trackRecoveryHandler.getTrack();
            routeSegmentIndex = routeSegments.indexOf(currentSegment);
            routeDistanceHelper.updateTrackIndex(routeSegmentIndex);
            Timber.d("User on track #%d\n", routeSegmentIndex);
            isUserTrackLost = false;

        } else if (deepAttempt) {
            Timber.d("Deep recovery has failed");
        } else {
            isUserTrackLost = true;
            Timber.e("Track of the user has been lost");
        }
    }

    /**
     * Gets the UserRouteTrackerNotifier instance associated with this UserRouteTracker.
     *
     * @return the UserRouteTrackerNotifier instance
     */
    public UserRouteTrackerNotifier getRouteTrackerNotifier() {
        return routeTrackerNotifier;
    }
}
