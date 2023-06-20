package com.isc.hermes.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * The CurrentLocationModel class represents the model for the current location.
 * It stores the latitude and longitude coordinates of the current location.
 */
public class CurrentLocationModel {
    private double latitude;
    private double longitude;
    private static CurrentLocationModel currentLocationModel;

    /**
     * Single ton to obtain the current location from the user
     * @return user's current location
     */
    public static CurrentLocationModel getCurrentLocationModel() {
        if (currentLocationModel == null) currentLocationModel = new CurrentLocationModel();
        return currentLocationModel;
    }

    /**
     * Returns the latitude coordinate of the current location.
     *
     * @return The latitude coordinate.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude coordinate of the current location.
     *
     * @return The longitude coordinate.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the latitude and longitude coordinates as a LatLng object.
     *
     * @return The LatLng object representing the current location.
     */
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    /**
     * Sets the latitude coordinate of the current location.
     *
     * @param latitude The latitude coordinate to set.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude coordinate of the current location.
     *
     * @param longitude The longitude coordinate to set.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

