package com.isc.hermes.utils;

public class MapCoordsRecord {
    private final double latitude;
    private final double longitude;

    public MapCoordsRecord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitudeAsRadians() {
        return Math.toRadians(latitude);
    }

    public double getLongitudeAsRadians() {
        return Math.toRadians(longitude);
    }
}
