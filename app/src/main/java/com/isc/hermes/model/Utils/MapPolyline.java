package com.isc.hermes.model.Utils;


import android.graphics.Color;

import com.isc.hermes.controller.MapPolylineManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

public class MapPolyline {
    private final MapPolylineManager polylineManager;

    public MapPolyline() {
        polylineManager = new MapPolylineManager();
    }

    public void displaySavedCoordinates(List<String> geoJson, List<Integer> colors) {
        polylineManager.displaySavedCoordinates(geoJson, colors);
    }

    public void displayPolyline(LatLng point1, LatLng point2) {
        polylineManager.displayPolyline(point1, point2);
    }
}
