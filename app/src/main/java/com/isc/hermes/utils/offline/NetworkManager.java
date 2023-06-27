package com.isc.hermes.utils.offline;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

/**
 * This is utility class for checking network connectivity status.
 */
public class NetworkManager {

    /**
     * This checks if the device is currently connected to the internet.
     *
     * @param context The context of the application or activity.
     * @return {@code true} if the device is connected to the internet, {@code false} otherwise.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return hasTransport(capabilities, NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(capabilities, NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(capabilities, NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }

        return false;
    }

    /**
     * This checks if the given network capabilities have the specified transport type.
     *
     * @param capabilities The network capabilities to check.
     * @param transport    The transport type to check for.
     * @return {@code true} if the network capabilities have the specified transport type,
     * {@code false} otherwise.
     */
    private static boolean hasTransport(NetworkCapabilities capabilities, int transport) {
        return capabilities.hasTransport(transport);
    }
}
