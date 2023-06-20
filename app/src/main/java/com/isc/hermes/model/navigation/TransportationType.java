package com.isc.hermes.model.navigation;

/**
 * Enum Class to represent the different transportation types of the navigation options
 */
public enum TransportationType {
    CAR(12.0), BIKE(13.0), WALK(5.0), MOTORCYCLE(16.0);

    private final double velocity;

    /**
     * Constructor method to set the velocity attribute
     * @param velocity transportation velocity
     */
    TransportationType(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Method to get the transportation's velocity
     * @return transportation's velocity
     */
    public double getVelocity() {
        return velocity;
    }
}
