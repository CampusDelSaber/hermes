package com.isc.hermes.utils;

/**
 * This interface is responsible for listening to network changes.
 */
public interface OnNetworkChangeListener {
    /**
     * This method is called when the network changes.
     * @param isConnected Indicates if the device is connected to the internet.
     */
    void onNetworkChange(boolean isConnected);
}
