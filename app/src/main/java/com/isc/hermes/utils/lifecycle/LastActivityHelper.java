package com.isc.hermes.utils.lifecycle;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This is a helper class for saving and retrieving the name of the last activity created using SharedPreferences.
 */
public class LastActivityHelper {
    private static final String PREFS_NAME = "LastActivityPrefs";
    private static final String KEY_LAST_ACTIVITY = "lastActivity";

    /**
     * This method saves the name of the last activity in SharedPreferences.
     *
     * @param context       The context of the calling activity.
     * @param activityName  The name of the last activity.
     */
    public static void saveLastActivity(Context context, String activityName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_ACTIVITY, activityName);
        editor.apply();
    }

    /**
     * This method retrieves the name of the last activity from SharedPreferences.
     *
     * @param context  The context of the calling activity.
     * @return The name of the last activity, or an empty string if not found.
     */
    public static String getLastActivity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_ACTIVITY, "");
    }
}
