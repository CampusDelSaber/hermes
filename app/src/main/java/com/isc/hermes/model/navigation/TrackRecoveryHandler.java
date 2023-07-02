package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;

import com.isc.hermes.model.CurrentLocationModel;

import java.util.List;

import timber.log.Timber;

public class TrackRecoveryHandler {
    private final List<RouteSegmentRecord> routeSegments;
    private final CurrentLocationModel currentLocation;
    private RouteSegmentRecord foundedTrack;

    public TrackRecoveryHandler(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        currentLocation = CurrentLocationModel.getInstance();
        foundedTrack = null;
    }

    public boolean isAttemptSuccessful() {
        return (foundedTrack != null);
    }

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

    public RouteSegmentRecord getTrack() {
        RouteSegmentRecord tmp = foundedTrack;
        foundedTrack = null;
        return tmp;
    }

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

    private void simpleStrategy() {
        RouteSegmentRecord candidate = routeSegments.get(0);
        if (isPointInsideSegment(candidate, currentLocation.getLatLng())) {
            foundedTrack = candidate;
        }
    }

    private void doubleWayStrategy(int routeSegmentIndex) {
        int nexTrackIndex = (routeSegmentIndex == 1) ? 0 : 1;
        RouteSegmentRecord candidate = routeSegments.get(nexTrackIndex);
        RouteSegmentRecord current = routeSegments.get(routeSegmentIndex);

        foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{candidate, current});
    }

    private void threeWayStrategy(int routeSegmentIndex) {
        RouteSegmentRecord[] closestTracks = getClosestTracks(routeSegmentIndex);
        RouteSegmentRecord leftTrack = closestTracks[0];
        RouteSegmentRecord rightTrack = closestTracks[1];
        RouteSegmentRecord middle = routeSegments.get(routeSegmentIndex);

        foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{rightTrack, middle, leftTrack});
    }
}
