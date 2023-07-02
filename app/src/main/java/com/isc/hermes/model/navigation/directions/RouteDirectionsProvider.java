package com.isc.hermes.model.navigation.directions;

import com.isc.hermes.controller.NavigationDirectionController;
import com.isc.hermes.model.navigation.UserRouteTracker;
import com.isc.hermes.model.navigation.events.Subscriber;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;

public class RouteDirectionsProvider implements Subscriber {

    private final UserRouteTracker userRouteTracker;
    private final NavigationDirectionController navigationDirectionController;

    public RouteDirectionsProvider(UserRouteTracker userRouteTracker, NavigationDirectionController navigationDirectionController) {
        this.userRouteTracker = userRouteTracker;
        this.navigationDirectionController = navigationDirectionController;
        userRouteTracker.getRouteTrackerNotifier().subscribe(this);
    }

    @Override
    public void update() {
        RouteSegmentRecord currentSegment = userRouteTracker.getCurrentSegment();
        DirectionsRecord checkpoint = currentSegment.getDirections()[0];
        DirectionsRecord nextCheckpoint = currentSegment.getDirections()[1];
        navigationDirectionController.update(checkpoint, nextCheckpoint);
    }
}
