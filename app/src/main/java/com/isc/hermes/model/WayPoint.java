package com.isc.hermes.model;

import com.google.gson.JsonObject;

public class WayPoint {
    private String placeName;
    private JsonObject properties;
    private double latitude;
    private double longitude;

    public WayPoint(String placeName, JsonObject properties, double latitude, double longitude) {
        this.placeName = placeName;
        this.properties = properties;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public JsonObject getProperties() {
        return properties;
    }

    public void setProperties(JsonObject properties) {
        this.properties = properties;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
