package com.isc.hermes.model;

/**
 * The LocationPermissionsModel class represents the model for location permissions.
 * It stores the granted status of location permissions.
 */
public class LocationPermissionsModel {
    boolean granted;

    /**
     * Sets the granted status of location permissions.
     *
     * @param granted true if location permissions are granted, false otherwise.
     */
    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    /**
     * Checks if location permissions are granted.
     *
     * @return true if location permissions are granted, false otherwise.
     */
    public boolean isGranted() {
        return granted;
    }
}
