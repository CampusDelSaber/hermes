package com.isc.hermes.utils;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is responsible for requesting the activation of Android services.
 */
public class AndroidRequestActivation {
    /**
     * Shows a dialog to enable internet.
     */
    public void showInternetRequest(AppCompatActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Internet Connection Needed")
                .setMessage("You need internet access for this function. Do you want to enable it?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    activity.startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }
}