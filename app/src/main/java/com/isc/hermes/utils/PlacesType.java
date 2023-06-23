package com.isc.hermes.utils;

import com.isc.hermes.R;

/**
 * Represents the types of places available in a location.
 */
public enum PlacesType {
    /** Represents a restaurant. */
    RESTAURANT("Restaurant", R.drawable.restaurant),

    /** Represents a hotel. */
    HOTEL("Hotel", R.drawable.hotel),

    /** Represents a park. */
    PARK("Park", R.drawable.park),

    /** Represents a museum. */
    MUSEUM("Museum", R.drawable.museum),

    /** Represents a shopping mall. */
    SHOPPING_MALL("Shopping Mall", R.drawable.shopping_mall),

    /** Represents a beach. */
    BEACH("Beach", R.drawable.beach),

    /** Represents a cafe. */
    CAFE("Cafe", R.drawable.cafe),

    /** Represents a bar. */
    BAR("Bar", R.drawable.bar),

    /** Represents a movie theater. */
    MOVIE_THEATER("Movie Theater", R.drawable.movie_theater),

    /** Represents a library. */
    LIBRARY("Library", R.drawable.library);

    private final String displayName;
    private final int imageResource;

    /**
     * Constructs a new PlacesType with the specified display name.
     *
     * @param displayName the display name of the place type
     * @param imageResource the image resource of the place type
     */
    PlacesType(String displayName, int imageResource) {
        this.displayName = displayName;
        this.imageResource = imageResource;
    }

    /**
     * Returns the display name of the place type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the image resource of the place type.
     *
     * @return the image resource
     */
    public int getImageResource() {
        return imageResource;
    }
}
