package com.isc.hermes.model;

import com.google.gson.JsonObject;


/**
 * Class to represent a waypoint
 * Receiving the latitude, longitude, placename and properties of a carmenFeature
 *
 * @see JsonObject to see the json file of the waypoint properties
 */
public class WayPoint {
    private String placeName;
    private JsonObject properties;
    private double latitude;
    private double longitude;

    /**
     * Constructor method to initialize the attributes
     * @param placeName place's name
     * @param properties place's properties
     * @param latitude place's latitude
     * @param longitude place's longitude
     */
    public WayPoint(String placeName, JsonObject properties, double latitude, double longitude) {
        this.placeName = placeName;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    /**
     * Gets the place name.
     *
     * @return The place name.
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Sets the place name.
     *
     * @param placeName The place name to set.
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * Gets the properties.
     *
     * @return The properties.
     */
    public JsonObject getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties The properties to set.
     */
    public void setProperties(JsonObject properties) {
        this.properties = properties;
    }

    /**
     * Gets the latitude.
     *
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude The latitude to set.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude The longitude to set.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
