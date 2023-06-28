package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.isc.hermes.utils.MapCoordsRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class UserRouteTracker {
    private final CoordinatesDistanceCalculator distanceCalculator;
    private final CurrentLocationModel currentLocation;
    private final Route route;
    private RouteSegmentRecord currentRouteSegmentRecord;
    private final ListIterator<RouteSegmentRecord> availableSegments;

    private final MapCoordsRecord destination;


    public UserRouteTracker(Route route) {
        this.route = route;
        currentRouteSegmentRecord = null;
        distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        currentLocation = CurrentLocationModel.getInstance();
        availableSegments = makeRouteSegments().listIterator();
        Node lastNode = route.getCurrentNode();
        destination = new MapCoordsRecord(lastNode.getLatitude(), lastNode.getLongitude());
    }

    public double getPartialSegmentDistance() {
        MapCoordsRecord userLocation = new MapCoordsRecord(currentLocation.getLatitude(), currentLocation.getLongitude());
        
        if (!isUserInSegment(userLocation)) {
            updateCurrentRouteSegment(userLocation);
        }
        return distanceCalculator.calculateDistance(
                new MapCoordsRecord(userLocation.getLatitude(), userLocation.getLongitude()),
                currentRouteSegmentRecord.getEnd()
        );
    }

    private void updateCurrentRouteSegment(MapCoordsRecord userLocation) {
        if (availableSegments.hasNext()) {
            currentRouteSegmentRecord = availableSegments.next();
            if (!NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation)){
                // throw exception
                System.out.println("segment error");
            }
        }
    }

    private List<RouteSegmentRecord> makeRouteSegments() {
        List<RouteSegmentRecord> routeSegments = new ArrayList<>();
        List<Node> path = route.getPath();
        for (int index = 0; index < path.size() - 1; index++) {
            Node source = path.get(index);
            Node target = path.get(index + 1);

            MapCoordsRecord sourceCoords = new MapCoordsRecord(source.getLatitude(), source.getLongitude());
            MapCoordsRecord targetCoords = new MapCoordsRecord(target.getLatitude(), target.getLongitude());

            routeSegments.add(new RouteSegmentRecord(sourceCoords, targetCoords));
        }

        return routeSegments;
    }

    public boolean hasUserArrived() {
        return NavigationTrackerTools.isPointReached(destination, new MapCoordsRecord(currentLocation.getLatitude(), currentLocation.getLatitude()));
    }

    public boolean isUserInSegment(MapCoordsRecord userLocation){
        boolean isInsideSegment = NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation);
        boolean isEndReached = NavigationTrackerTools.isPointReached(currentRouteSegmentRecord.getEnd(), userLocation);

        return isInsideSegment && !isEndReached;
    }
}
