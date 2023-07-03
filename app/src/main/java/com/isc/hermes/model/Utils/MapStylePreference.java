package com.isc.hermes.model.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to handle the map style according to user preferences.
 * <p>
 *     Allows to recover the map style between executions and when user sign-in many times
 *     to his Hermes account.
 * </p>
 */
public class MapStylePreference {
    private static final String PREFERENCE_NAME = "MapStylePreference";
    private static final String KEY_MAP_STYLE = "MapStyle";

    /**
     * Method to set the received map style into shared preferences.
     *
     * @param context is the application context.
     * @param mapStyle is the map style to be stored into shared preferences.
     */
    public static void setMapStyle(Context context, String mapStyle) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_MAP_STYLE, mapStyle);
        editor.apply();
    }

    /**
     * Method to recover the map style stored at shared preferences.
     *
     * @param context is the application context.
     * @return the stored map style.
     */
    public static String getMapStyle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_MAP_STYLE, null);
    }
}