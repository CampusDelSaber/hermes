package com.isc.hermes.utils.lifecycle;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class ActivityStackManager {
    private static ActivityStackManager instance;
    private Context context;

    private ActivityStackManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized ActivityStackManager getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityStackManager(context);
        }
        return instance;
    }

    public Intent verifyTop() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        if (runningTasks != null && runningTasks.size() > 1) {
            ActivityManager.RunningTaskInfo topTask = runningTasks.get(1);

            if (topTask != null && topTask.topActivity != null) {
                Intent intent = new Intent();
                intent.setComponent(topTask.topActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                return intent;
            }
        }

        return null;
    }
}
