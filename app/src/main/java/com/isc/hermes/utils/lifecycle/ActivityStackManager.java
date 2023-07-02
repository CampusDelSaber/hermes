package com.isc.hermes.utils.lifecycle;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;
/**
 * This class manages the activity stack and provides utility methods to handle activities.
 */
public class ActivityStackManager {
    private static ActivityStackManager instance;
    private Context context;

    private static final int TOP_STACK = 1;
    /**
     * This method constructs a new instance of ActivityStackManager.
     *
     * @param context The application context.
     */
    private ActivityStackManager(Context context) {
        this.context = context.getApplicationContext();
    }
    /**
     * This method returns the singleton instance of ActivityStackManager.
     *
     * @param context The application context.
     * @return The singleton instance of ActivityStackManager.
     */
    public static synchronized ActivityStackManager getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityStackManager(context);
        }
        return instance;
    }
    /**
     *This method  verifies the top activity in the stack.
     *
     * @return The intent representing the top activity, or null if there is no valid top activity.
     */
    public Intent getForeground() {
        List<ActivityManager.RunningTaskInfo> runningTasks = getRunningTasks();

        if (runningTasks != null && runningTasks.size() > TOP_STACK) {
            ActivityManager.RunningTaskInfo topTask = runningTasks.get(TOP_STACK);

            if (topTask != null && topTask.topActivity != null) {
                return createIntentFromActivity(topTask.topActivity);
            }
        }

        return null;
    }

    /**
     * This method retrieves the running tasks from the activity manager.
     *
     * @return The list of running tasks.
     */
    private List<ActivityManager.RunningTaskInfo> getRunningTasks() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            return activityManager.getRunningTasks(Integer.MAX_VALUE);
        }
        return null;
    }

    /**
     * This method creates an intent from the given activity component.
     *
     * @param activityComponent The component representing the activity.
     * @return The intent created from the activity component.
     */
    private Intent createIntentFromActivity(ComponentName activityComponent) {
        Intent intent = new Intent();
        intent.setComponent(activityComponent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

}
