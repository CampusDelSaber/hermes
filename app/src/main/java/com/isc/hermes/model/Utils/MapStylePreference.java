package com.isc.hermes.model.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MapStylePreference {
    private static final String PREFERENCE_NAME = "MapStylePreference";
    private static final String KEY_MAP_STYLE = "MapStyle";

    public static void setMapStyle(Context context, String mapStyle) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_MAP_STYLE, mapStyle);
        editor.apply();
    }

    public static String getMapStyle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_MAP_STYLE, null);
    }
}