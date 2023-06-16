package com.isc.hermes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MarkerManager {
    private Marker currentMarker;
    private SharedPreferences sharedPreferences;
    private static MarkerManager instance;

    public static MarkerManager getInstance(Context context){
        if(instance == null) instance = new MarkerManager(context);
        return instance;
    }

    public MarkerManager(Context context) {
        sharedPreferences = context.getSharedPreferences("com.isc.hermes", Context.MODE_PRIVATE);
    }

    public void addMarkerToMap(MapboxMap mapboxMap, String placeName, double latitude, double longitude) {
        if (currentMarker != null) {
            mapboxMap.removeMarker(currentMarker);
            removeSavedMarker();
        }
        if(placeName != null && mapboxMap!=null){
            MarkerOptions options = new MarkerOptions();
            options.setTitle(placeName);
            options.position(new LatLng(latitude, longitude));
            currentMarker = mapboxMap.addMarker(options);
        }
    }

    private void removeSavedMarker() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("placeName");
        editor.remove("latitude");
        editor.remove("longitude");
        editor.apply();
    }
}
