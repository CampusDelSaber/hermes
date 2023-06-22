package com.isc.hermes.utils;

/**
 * Represents the types of places available in a location.
 */
public enum PlacesType {
    /** Represents a restaurant. */
    RESTAURANT("Restaurant"),

    /** Represents a hotel. */
    HOTEL("Hotel"),

    /** Represents a park. */
    PARK("Park"),

    /** Represents a museum. */
    MUSEUM("Museum"),

    /** Represents a shopping mall. */
    SHOPPING_MALL("Shopping Mall"),

    /** Represents a beach. */
    BEACH("Beach"),

    /** Represents a cafe. */
    CAFE("Cafe"),

    /** Represents a bar. */
    BAR("Bar"),

    /** Represents a movie theater. */
    MOVIE_THEATER("Movie Theater"),

    /** Represents a library. */
    LIBRARY("Library");

    private final String displayName;

    /**
     * Constructs a new PlacesType with the specified display name.
     *
     * @param displayName the display name of the place type
     */
    PlacesType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the place type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
