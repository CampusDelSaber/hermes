package com.isc.hermes.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for managing shared preferences related to searchers.
 */
public class SharedSearcherPreferencesManager {
    private final SharedPreferences sharedPreferences;
    private static final String KEY_PLACE_NAME = "placeName";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    /**
     * Constructs a new SharedSearcherPreferencesManager instance.
     *
     * @param context the context used for accessing shared preferences
     */
    public SharedSearcherPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("com.isc.hermes", Context.MODE_PRIVATE);
    }

    /**
     * Retrieves the saved place name from shared preferences.
     *
     * @return the place name, or null if not found
     */
    public String getPlaceName() {
        return sharedPreferences.getString(KEY_PLACE_NAME, null);
    }

    /**
     * Retrieves the saved latitude from shared preferences.
     *
     * @return the latitude value, or 0 if not found
     */
    public double getLatitude() {
        return sharedPreferences.getFloat(KEY_LATITUDE, 0);
    }

    /**
     * Retrieves the saved longitude from shared preferences.
     *
     * @return the longitude value, or 0 if not found
     */
    public double getLongitude() {
        return sharedPreferences.getFloat(KEY_LONGITUDE, 0);
    }
}
