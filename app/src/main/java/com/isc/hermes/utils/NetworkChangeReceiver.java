package com.isc.hermes.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is responsible for listening to network changes.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private final OnNetworkChangeListener onNetworkChangeListener;

    /**
     * This constructor is responsible for initializing the listener.
     * @param onNetworkChangeListener The listener.
     */
    public NetworkChangeReceiver(OnNetworkChangeListener onNetworkChangeListener) {
        this.onNetworkChangeListener = onNetworkChangeListener;
    }

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        onNetworkChangeListener.onNetworkChange(checkInternet(context));
    }

    /**
     * This method is responsible for checking if the device is connected to the internet.
     * @param context The context.
     * @return Returns true if the device is connected to the internet, false otherwise.
     */
    boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}

