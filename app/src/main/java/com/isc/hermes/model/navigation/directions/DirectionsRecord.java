package com.isc.hermes.model.navigation.directions;

public class DirectionsRecord {
    private final String streetName;
    private final DirectionEnum direction;

    public DirectionsRecord(String streetName, DirectionEnum direction) {
        this.streetName = streetName;
        this.direction = direction;
    }

    public String getStreetName() {
        return streetName;
    }

    public DirectionEnum getDirection() {
        return direction;
    }
}
