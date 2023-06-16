package com.isc.hermes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The MarkerManager class is responsible for managing markers on a MapView.
 */
public class MarkerManager {
    private Marker currentMarker;
    private SharedPreferences sharedPreferences;
    private static MarkerManager instance;

    /**
     * Constructs a new MarkerManager instance.
     *
     * @param context The context used to access SharedPreferences.
     */
    public MarkerManager(Context context) {
        sharedPreferences = context.getSharedPreferences("com.isc.hermes", Context.MODE_PRIVATE);
    }

    public static MarkerManager getInstance(Context context) {
        if(instance == null) instance = new MarkerManager(context);
        return instance;
    }

    /**
     * Adds a marker to the map at the specified location.
     *
     * @param mapView    The MapView instance.
     * @param placeName  The name of the place associated with the marker.
     * @param latitude   The latitude of the marker's position.
     * @param longitude  The longitude of the marker's position.
     */
    public boolean addMarkerToMap(MapView mapView, String placeName, double latitude, double longitude) {
        AtomicBoolean added = new AtomicBoolean(false);
        mapView.getMapAsync(mapboxMap -> {
            if (currentMarker != null) {
                mapboxMap.removeMarker(currentMarker);
                removeSavedMarker();
            }

            if (placeName != null) {
                MarkerOptions options = new MarkerOptions();
                options.setTitle(placeName);
                options.position(new LatLng(latitude, longitude));
                currentMarker = mapboxMap.addMarker(options);
                added.set(true);
            }
        });
        return added.get();
    }

    /**
     * Sets the camera position to the specified latitude and longitude.
     *
     * @param mapboxMap   The MapboxMap instance.
     * @param latitude  The latitude of the camera's position.
     * @param longitude The longitude of the camera's position.
     */
    public void setCameraPosition(MapboxMap mapboxMap, double latitude, double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(10)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Removes the saved marker from SharedPreferences.
     */
    public void removeSavedMarker() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("placeName");
        editor.remove("latitude");
        editor.remove("longitude");
        editor.apply();
    }

    public Marker getCurrentMarker() {
        return currentMarker;
    }


}
