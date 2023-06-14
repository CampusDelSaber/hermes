package com.isc.hermes.model;

import com.isc.hermes.SearchViewActivity.MapboxMapListener;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

public class MapboxEventManager {
    private static MapboxEventManager instance;
    private MapboxMapListener mapboxMapListener;

    private MapboxEventManager() {}

    public static MapboxEventManager getInstance() {
        if (instance == null) {
            instance = new MapboxEventManager();
        }
        return instance;
    }

    public void setMapboxMapListener(MapboxMapListener listener) {
        mapboxMapListener = listener;
    }

    public void addMarker(MarkerOptions markerOptions) {
        if (mapboxMapListener != null) {
            mapboxMapListener.addMarker(markerOptions);
        }
    }
}
