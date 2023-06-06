package com.isc.hermes.view;

import android.os.Bundle;

import com.isc.hermes.utils.MapConfigure;
import com.mapbox.mapboxsdk.maps.MapView;

public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;

    public MapDisplay(MapView mapView, MapConfigure mapConfigure) {
        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
    }

    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapConfigure::configure);
    }

    public void onStart() {
        mapView.onStart();
    }

    public void onResume() {
        mapView.onResume();
    }

    public void onPause() {
        mapView.onPause();
    }

    public void onStop() {
        mapView.onStop();
    }

    public void onLowMemory() {
        mapView.onLowMemory();
    }

    public void onDestroy() {
        mapView.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }
}
