package com.isc.hermes.model.navigation;

/**
 * Enum Class to represent the different transportation types of the navigation options
 */
public enum TransportationType {
    CAR("CAR",20.0), BIKE("BIKE",12.0),
    WALK("WALK",5.0), MOTORCYCLE("MOTORCYCLE",25.0);

    private final double velocity;
    private final String name;

    /**
     * Constructor method to set the velocity attribute
     * @param velocity transportation velocity
     */
    TransportationType(String name, double velocity) {
        this.name = name;
        this.velocity = velocity;
    }

    /**
     * Method to get the transportation's velocity
     * @return transportation's velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Method to get the transportation's name
     * @return transportation's name
     */
    public String getName() {
        return name;
    }
}
