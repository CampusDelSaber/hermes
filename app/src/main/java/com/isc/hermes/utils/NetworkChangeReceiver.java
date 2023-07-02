package com.isc.hermes.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class NetworkChangeReceiver extends BroadcastReceiver {

    private final OnNetworkChangeListener onNetworkChangeListener;

    public NetworkChangeReceiver(OnNetworkChangeListener onNetworkChangeListener) {
        this.onNetworkChangeListener = onNetworkChangeListener;
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        onNetworkChangeListener.onNetworkChange(checkInternet(context));
    }

    boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}

