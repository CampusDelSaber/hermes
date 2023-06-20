package com.isc.hermes.utils;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class CameraAnimator {
    private final MapboxMap mapboxMap;

    public CameraAnimator(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void animateCameraToNewPosition(LatLng latLng, double zoom, int durationMillis) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(zoom)
                        .build()), durationMillis);
    }
}
