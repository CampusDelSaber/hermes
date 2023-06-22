package com.isc.hermes.model.location;

public enum LocationIntervals {
    UPDATE_INTERVAL_MS(1000),
    SMALLEST_DISPLACEMENT_METERS(50f),
    ACCURACY_ALPHA(0.1f);

    private final float value;

    LocationIntervals(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
