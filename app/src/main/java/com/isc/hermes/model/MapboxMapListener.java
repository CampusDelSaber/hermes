package com.isc.hermes.model;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;

public interface MapboxMapListener {
    void addMarker(MarkerOptions markerOptions);
}