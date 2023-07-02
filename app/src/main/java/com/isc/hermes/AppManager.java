package com.isc.hermes;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
/**
 * Custom Application class to manage the last activity used in the app.
 */
public class AppManager extends Application {
    private Activity lastActivity;
    /**
     * Returns the intent representing the last activity used in the app.
     *
     * @return The intent representing the last activity, or null if there is no last activity.
     */
    public Intent getLastActivity() {
        Intent intent = null;
        if (lastActivity != null) {
            intent = new Intent(this, lastActivity.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return intent;
    }
    /**
     * Sets the last activity used in the app.
     *
     * @param activity The last activity used.
     */
    public void setLastActivity(Activity activity) {
        lastActivity = activity;
    }
}
