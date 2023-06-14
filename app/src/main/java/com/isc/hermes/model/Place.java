package com.isc.hermes.model;

public class Place {
    private String placeName;
    private String placeLocation;

    // Constructor
    public Place(String placeName, String placeLocation) {
        this.placeName = placeName;
        this.placeLocation = placeLocation;
    }

    // Getters
    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }
}

