package com.isc.hermes.model;

public class DirectionInstruction {
    private String direction;
    private String icon;
    private String streetName;

    public DirectionInstruction(String direction, String icon, String streetName) {
        this.direction = direction;
        this.icon = icon;
        this.streetName = streetName;
    }

    public String getDirection() {
        return direction;
    }

    public String getIcon() {
        return icon;
    }

    public String getStreetName() {
        return streetName;
    }
}
