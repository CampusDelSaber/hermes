package com.isc.hermes.utils;

import android.content.Context;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class PlacesSearchedOnTheMap {
    private final MapboxMap mapboxMap;

    public PlacesSearchedOnTheMap(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;

    }

    public void displayPoint(List<LatLng> pointList) {
        for (int i = 0; i < pointList.size(); i++) {
            LatLng aux = pointList.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(aux);
            mapboxMap.addMarker(markerOptions);
        }
    }
}