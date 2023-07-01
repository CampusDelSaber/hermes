package com.isc.hermes.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

public class AndroidServicesVerification {
    /**
     * Checks if the location is enabled on the device.
     * @return Returns true if the location is enabled, false otherwise.
     */
    public boolean isLocationEnabled(AppCompatActivity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return gps_enabled || network_enabled;
    }

    public boolean isInternetEnabled(AppCompatActivity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return false;
    }
}
