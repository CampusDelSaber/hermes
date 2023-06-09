package com.isc.hermes.utils;

import android.content.Context;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

public class PlacesSearchedOnTheMap {
    private final MapboxMap mapboxMap;
    List<LatLng> pointList = new ArrayList<>();

    public PlacesSearchedOnTheMap(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        pointTest();
        displayPoint(pointList);
    }

    public void displayPoint(List<LatLng> pointList) {
        for (int i = 0; i < pointList.size(); i++) {
            LatLng aux = pointList.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(aux);
            mapboxMap.addMarker(markerOptions);
        }
    }

    private void pointTest(){
        LatLng latLng = new LatLng(-17.365546, -66.173602);
        LatLng latLng2 = new LatLng(-17.366484, -66.175582);
        LatLng latLng3 = new LatLng(-17.371483, -66.176134);
        pointList.add(latLng);
        pointList.add(latLng2);
        pointList.add(latLng3);
    }
}