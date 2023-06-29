package com.isc.hermes.model.location;

/**
 * The LocationIntervals enum represents the intervals used for location updates.
 */
public enum LocationIntervals {
    /**
     * The update interval in milliseconds for location updates.
     */
    UPDATE_INTERVAL_MS(1000),

    /**
     * The smallest displacement in meters to trigger location updates.
     */
    SMALLEST_DISPLACEMENT_METERS(50f),

    /**
     * The alpha value for the accuracy circle of the user's location on the map.
     */
    ACCURACY_ALPHA(0.1f);

    private final float value;

    /**
     * Constructs a new LocationIntervals enum constant with the specified value.
     *
     * @param value The value associated with the enum constant.
     */
    LocationIntervals(float value) {
        this.value = value;
    }

    /**
     * Returns the value associated with the enum constant.
     *
     * @return The value of the enum constant.
     */
    public float getValue() {
        return value;
    }
}
