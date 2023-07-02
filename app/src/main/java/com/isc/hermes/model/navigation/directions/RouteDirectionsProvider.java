package com.isc.hermes.model.navigation.directions;

import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.model.navigation.events.Publisher;
import com.isc.hermes.model.navigation.events.Subscriber;

import java.util.List;

public class RouteDirectionsProvider implements Subscriber {
    private List<RouteSegmentRecord> routeSegments;
    private int routeSegmentIndex;

    public RouteDirectionsProvider(List<RouteSegmentRecord> routeSegments, Publisher publisher) {
        this.routeSegments = routeSegments;
        publisher.subscribe(this);
    }

    public DirectionsRecord getTrackDirections(){
        return routeSegments.get(routeSegmentIndex).getDirections();
    }

    @Override
    public void update(int routeSegmentIndex){
        this.routeSegmentIndex = routeSegmentIndex;
    }
}
