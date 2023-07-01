package com.isc.hermes.model.navigation;

import static com.isc.hermes.model.navigation.NavigationTrackerTools.isPointInsideSegment;

import com.isc.hermes.model.CurrentLocationModel;

import java.util.List;

public class TrackRecoveryProtocol {
    private final List<RouteSegmentRecord> routeSegments;
    private final CurrentLocationModel currentLocation;
    private RouteSegmentRecord foundedTrack;

    public TrackRecoveryProtocol(List<RouteSegmentRecord> routeSegments) {
        this.routeSegments = routeSegments;
        currentLocation = CurrentLocationModel.getInstance();
        foundedTrack = null;
    }

    public boolean isAttemptSuccessful() {
        return (foundedTrack != null);
    }

    public void attemptSimpleRecovery(int routeSegmentIndex) {
        if (routeSegments.size() == 1){
            simpleStrategy();
        }else if (routeSegments.size() == 2){
            doubleWayStrategy(routeSegmentIndex);
        }else {
            threeWayStrategy(routeSegmentIndex);
        }
    }

    public void attemptDeepRecovery() {
        for (int index = 0; index < routeSegments.size(); index++) {
            System.out.printf("ATTEMPT RECOVERY ON TRACK #%d\n", index);
            attemptSimpleRecovery(index);
            if (isAttemptSuccessful()){
                break;
            }else {
                System.out.println("ATTEMPT FAILED");
            }
        }
    }

    public RouteSegmentRecord getTrack() {
        RouteSegmentRecord tmp = foundedTrack;
        foundedTrack = null;
        return tmp;
    }

    private RouteSegmentRecord extractSegmentWithUser(RouteSegmentRecord[] tracks){
        RouteSegmentRecord foundSegment = null;
        for (RouteSegmentRecord track : tracks) {
            if (isPointInsideSegment(track, currentLocation.getLatLng(), true)) {
                foundSegment = track;
                System.out.println("The user's track has been found");
                break;
            }
        }
        return foundSegment;
    }

    private RouteSegmentRecord[] getClosestTracks(int routeIndex) {
        RouteSegmentRecord firstCandidate;
        RouteSegmentRecord secondCandidate;

        if (routeIndex > 0) {
            firstCandidate = routeSegments.get(routeIndex - 1);
        } else {
            firstCandidate = routeSegments.get(routeIndex + 2);
        }

        if (routeIndex < routeSegments.size() - 1) {
            secondCandidate = routeSegments.get(routeIndex + 1);
        } else {
            secondCandidate = routeSegments.get(routeIndex - 2);
        }

        return new RouteSegmentRecord[]{firstCandidate, secondCandidate};
    }

    private void simpleStrategy(){
        RouteSegmentRecord candidate = routeSegments.get(0);
        if (isPointInsideSegment(candidate, currentLocation.getLatLng(), true)){
            foundedTrack = candidate;
        }
    }

    private void doubleWayStrategy(int routeSegmentIndex){
        int upper = routeSegmentIndex + 1;
        int lower = routeSegmentIndex - 1;
        RouteSegmentRecord candidate;
        RouteSegmentRecord current = routeSegments.get(routeSegmentIndex);

        if (upper < routeSegments.size()){
            candidate = routeSegments.get(upper);
        }else {
            candidate = routeSegments.get(lower);
        }

        RouteSegmentRecord twoTrack = new RouteSegmentRecord(current.getStart(), candidate.getEnd());
        boolean isUserInside = isPointInsideSegment(twoTrack, currentLocation.getLatLng(), true);
        if (isUserInside){
            foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{candidate, current});
        }
    }

    private void threeWayStrategy(int routeSegmentIndex){
        RouteSegmentRecord[] closestTracks = getClosestTracks(routeSegmentIndex);
        RouteSegmentRecord a = closestTracks[0];
        RouteSegmentRecord b = closestTracks[1];
        RouteSegmentRecord middle = routeSegments.get(routeSegmentIndex);

        RouteSegmentRecord triple = new RouteSegmentRecord(a.getStart(), b.getEnd());
        boolean isUserInside = isPointInsideSegment(triple, currentLocation.getLatLng(), true);
        if (isUserInside){
            foundedTrack = extractSegmentWithUser(new RouteSegmentRecord[]{a, b, middle});
        }
    }
}
