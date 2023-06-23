package com.isc.hermes.model;

/**
 * This class has the responsibility to store radium values on geodetic units.
 */
public enum Radium {

    FIVE_METERS(0.000045),
    TEN_METERS(0.00009),
    TWENTY_FIVE_METERS(0.000225),
    FIFTY_METERS(0.00045),
    ONE_HUNDRED_METERS(0.0009),
    FIVE_HUNDRED_METERS(0.0045),
    ONE_KILOMETER(0.009),
    FIVE_KILOMETERS(0.045),
    TEN_KILOMETERS(0.09);

    private final double value;

    /**
     * This is the constructor method to build the radium value.
     *
     * @param value to build the radium.
     */
    Radium(double value) {
        this.value = value;
    }

    /**
     * This is a getter method to get radium value built.
     *
     * @return radium value.
     */
    public double getValue() {
        return value;
    }
}
