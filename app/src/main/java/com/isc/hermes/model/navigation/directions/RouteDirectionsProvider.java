package com.isc.hermes.model.navigation.directions;

import com.isc.hermes.model.navigation.UserRouteTracker;
import com.isc.hermes.model.navigation.events.Subscriber;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;

public class RouteDirectionsProvider implements Subscriber {

    private final UserRouteTracker userRouteTracker;

    public RouteDirectionsProvider(UserRouteTracker userRouteTracker) {
        this.userRouteTracker = userRouteTracker;
        userRouteTracker.getRouteTrackerNotifier().subscribe(this);
    }

    @Override
    public void update() {
        RouteSegmentRecord currentSegment = userRouteTracker.getCurrentSegment();
        DirectionsRecord checkpoint = currentSegment.getDirections()[0];
        DirectionsRecord nextCheckpoint = currentSegment.getDirections()[1];
        // push update to view
    }
}
