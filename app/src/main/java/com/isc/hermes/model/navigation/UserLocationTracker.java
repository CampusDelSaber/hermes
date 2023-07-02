package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The UserLocationTracker class represents a tracker for monitoring user location changes.
 */
public class UserLocationTracker {
    private LatLng latestLocation;
    private final CurrentLocationModel currentLocation;

    public UserLocationTracker() {
        currentLocation = CurrentLocationModel.getInstance();
        latestLocation = currentLocation.getLatLng();
    }

    /**
     * Checks if the user has moved based on their current location compared to the last known location.
     *
     * @return true if the user has moved, false otherwise.
     */
    public boolean hasUserMoved() {
        LatLng userLocation = currentLocation.getLatLng();
        boolean hasUserMoved = NavigationTrackerTools.hasUserMoved(userLocation, latestLocation);
        if (hasUserMoved) {
            latestLocation = userLocation;
        }

        return hasUserMoved;
    }
}
