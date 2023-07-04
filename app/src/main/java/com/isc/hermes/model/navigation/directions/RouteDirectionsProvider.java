package com.isc.hermes.model.navigation.directions;

import com.isc.hermes.controller.NavigationDirectionController;
import com.isc.hermes.model.navigation.UserRouteTracker;
import com.isc.hermes.model.navigation.events.Subscriber;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;

/**
 * The RouteDirectionsProvider class is responsible for providing route directions updates to the navigation UI.
 * It subscribes to the UserRouteTracker's route tracker notifier and updates the navigation direction controller
 * with the current and next checkpoints.
 */
public class RouteDirectionsProvider implements Subscriber {

    private final UserRouteTracker userRouteTracker;
    private final NavigationDirectionController navigationDirectionController;

    /**
     * Constructs a RouteDirectionsProvider object with the specified UserRouteTracker and NavigationDirectionController.
     * It subscribes to the UserRouteTracker's route tracker notifier to receive route updates.
     *
     * @param userRouteTracker            The UserRouteTracker object.
     * @param navigationDirectionController The NavigationDirectionController object.
     */
    public RouteDirectionsProvider(UserRouteTracker userRouteTracker, NavigationDirectionController navigationDirectionController) {
        this.userRouteTracker = userRouteTracker;
        this.navigationDirectionController = navigationDirectionController;
        userRouteTracker.getRouteTrackerNotifier().subscribe(this);
    }

    /**
     * Updates the navigation direction controller with the current and next checkpoints.
     * This method is called when the UserRouteTracker notifies a route update.
     */
    @Override
    public void update() {
        RouteSegmentRecord currentSegment = userRouteTracker.getCurrentSegment();
        navigationDirectionController.update(currentSegment.getStart(), currentSegment.getEnd());
    }
}
