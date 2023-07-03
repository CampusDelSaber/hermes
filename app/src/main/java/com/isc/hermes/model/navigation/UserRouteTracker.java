package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;
import static com.isc.hermes.model.navigation.NavigationTrackerTools.isNearPoint;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.navigation.exceptions.RouteOutOfTracksException;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public void update() {
        LatLng userLocation = currentLocation.getLatLng();
        if (isUserTrackLost) {
            recoverTrack(true);
        }

        if (isUserOnTrack(userLocation)) {
            Timber.i(String.format("User on track #%d\n", routeSegmentIndex));
        }else {
            nextTrack(userLocation);
        }
    }

    /**
     * Parses the route information and initializes the necessary objects for tracking the user's route.
     *
     * @throws RouteOutOfTracksException If the route is empty and navigation cannot be started.
     */

    public void parseRoute() throws RouteOutOfTracksException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Void> routeParsingFuture = executorService.submit(() -> {
            CompletableFuture<List<RouteSegmentRecord>> futureRouteSegments = navigationRouteParser.makeRouteSegments(routeInformation);

            futureRouteSegments.thenAccept(routeSegments -> {
                this.routeSegments = routeSegments;

                if (routeSegments.isEmpty()) {
                    throw new RouteOutOfTracksException("Could not start the navigation, route is empty");
                }

                usersDestination = routeSegments.get(routeSegments.size() - 1).getEnd();
                trackRecoveryHandler = new TrackRecoveryHandler(routeSegments);
                routeDistanceHelper = new RouteDistanceHelper(routeSegments);
                Timber.i(String.format("Starting route with: %s Tracks\n", routeSegments.get(0).getDirections()[0].getStreetName()));
                nextTrack(currentLocation.getLatLng());
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                throw new RouteOutOfTracksException("Failed to retrieve route segments");
            });

            return null;
        });

        // Wait for the route parsing to complete
        try {
            routeParsingFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RouteOutOfTracksException("Route parsing failed");
        }

        // Shutdown the executor service
        executorService.shutdown();
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
                routeDistanceHelper.updateTrackIndex(routeSegmentIndex);
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
        boolean pointReached = isNearPoint(usersDestination, currentLocation.getLatLng());
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
        boolean isInsideSegment = isPointInsideSegment(currentSegment, CurrentLocationModel.getInstance().getLatLng());
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

    public UserRouteTrackerNotifier getRouteTrackerNotifier() {
        return routeTrackerNotifier;
    }

    public RouteSegmentRecord getCurrentSegment() {
        return currentSegment;
    }
}
