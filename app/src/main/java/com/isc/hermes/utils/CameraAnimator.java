package com.isc.hermes.utils;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class for animating the position of the camera.
 */
public class CameraAnimator {
    private final MapboxMap mapboxMap;

    /**
     * Constructor to create a CameraAnimator object.
     * @param mapboxMap The MapboxMap object to animate the camera of.
     */
    public CameraAnimator(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    /**
     * Animates the camera to a new position.
     * @param latLng The new position to animate the camera to.
     * @param zoom The zoom level to animate the camera to.
     * @param durationMillis The duration of the animation in milliseconds.
     */
    public void animateCameraToNewPosition(LatLng latLng, double zoom, int durationMillis) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(zoom)
                        .build()), durationMillis);
    }
}
