package com.isc.hermes.generators;

public enum Radium {

    FIFTY_METERS(0.00045),
    ONE_HUNDRED_METERS(0.0009),
    FIVE_HUNDRED_METERS(0.0045),
    ONE_KILOMETER(0.009),
    FIVE_KILOMETERS(0.045),
    TEN_KILOMETERS(0.09);

    private final double value;

    Radium(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
