package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;

import java.util.List;

import timber.log.Timber;

/**
 * The TrackRecoveryHandler class provides methods for recovering a track based on route segments and current location.
 */
public class TrackRecoveryHandler {
    private final List<RouteSegmentRecord> routeSegments;
    private final CurrentLocationModel currentLocation;
    private RouteSegmentRecord foundedTrack;

    /**
     * Constructs a new TrackRecoveryHandler object.
     *
     * @param routeSegments The list of route segments.
     */
    public TrackRecoveryHandler(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        currentLocation = CurrentLocationModel.getInstance();
        foundedTrack = null;
    }

    /**
     * Checks if the attempt to recover the track was successful.
     *
     * @return true if the track was successfully recovered, false otherwise.
     */
    public boolean isAttemptSuccessful() {
        return (foundedTrack != null);
    }

    /**
     * Attempts a simple track recovery strategy based on the given route segment index.
     *
     * @param routeSegmentIndex The index of the current route segment.
     */
    public void attemptSimpleRecovery(int routeSegmentIndex) {
        if (routeSegments.size() == 1) {
            simpleStrategy();
        } else if (routeSegments.size() == 2) {
            doubleWayStrategy(routeSegmentIndex);
        } else {
            threeWayStrategy(routeSegmentIndex);
        }

        Timber.d("Simple recovery is done");
    }

    /**
     * Attempts a deep track recovery strategy by iterating through all route segments.
     */
    public void attemptDeepRecovery() {
        for (int index = 0; index < routeSegments.size(); index++) {
            Timber.d("ATTEMPT RECOVERY ON TRACK #%d\n", index);
            attemptSimpleRecovery(index);
            if (isAttemptSuccessful()) {
                break;
            } else {
                Timber.d("ATTEMPT FAILED");
            }
        }
    }

    /**
     * Gets the recovered track segment and resets the found track.
     *
     * @return The recovered track segment.
     */
    public RouteSegmentRecord getTrack() {
        RouteSegmentRecord tmp = foundedTrack;
        foundedTrack = null;
        return tmp;
    }

    /**
     * Extracts a segment from the given tracks that contains the user's current location.
     *
     * @param tracks The tracks to search for the user's location.
     * @return The segment containing the user's location, or null if not found.
     */
    private RouteSegmentRecord extractSegmentWithUser(RouteSegmentRecord[] tracks) {
        RouteSegmentRecord foundSegment = null;
        for (RouteSegmentRecord track : tracks) {
            if (isPointInsideSegment(track, currentLocation.getLatLng())) {
                foundSegment = track;
                Timber.d("The user's track has been found");
                break;
            }
        }
        return foundSegment;
    }

    /**
     * Gets the closest tracks to the given route index.
     *
     * @param routeIndex The index of the current route segment.
     * @return An array of closest tracks, left track at index 0 and right track at index 1.
     */
    private RouteSegmentRecord[] getClosestTracks(int routeIndex) {
        RouteSegmentRecord leftTrack;
        RouteSegmentRecord rightTrack;

        if (routeIndex > 0) {
            leftTrack = routeSegments.get(routeIndex - 1);
        } else {
            leftTrack = routeSegments.get(routeIndex + 2);
        }

        if (routeIndex < routeSegments.size() - 1) {
            rightTrack = routeSegments.get(routeIndex + 1);
        } else {
            rightTrack = routeSegments.get(routeIndex - 2);
        }

        return new RouteSegmentRecord[]{leftTrack, rightTrack};
    }

    /**
     * Applies a simple strategy to recover the track based on a single route segment.
     */
    private void simpleStrategy() {
        RouteSegmentRecord candidate = routeSegments.get(0);
        if (isPointInsideSegment(candidate, currentLocation.getLatLng())) {
            foundedTrack = candidate;
        }
    }

    /**
     * Applies a double-way strategy to recover the track based on two route segments.
     *
     * @param routeSegmentIndex The index of the current route segment.
     */
    private void doubleWayStrategy(int routeSegmentIndex) {
        int nexTrackIndex = (routeSegmentIndex == 1) ? 0 : 1;
        RouteSegmentRecord candidate = routeSegments.get(nexTrackIndex);
        RouteSegmentRecord current = routeSegments.get(routeSegmentIndex);

        foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{candidate, current});
    }

    /**
     * Applies a three-way strategy to recover the track based on three route segments.
     *
     * @param routeSegmentIndex The index of the current route segment.
     */
    private void threeWayStrategy(int routeSegmentIndex) {
        RouteSegmentRecord[] closestTracks = getClosestTracks(routeSegmentIndex);
        RouteSegmentRecord leftTrack = closestTracks[0];
        RouteSegmentRecord rightTrack = closestTracks[1];
        RouteSegmentRecord middle = routeSegments.get(routeSegmentIndex);

        foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{rightTrack, middle, leftTrack});
    }
}
