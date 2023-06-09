package com.isc.hermes.utils;

import android.content.Context;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class creates waypoints which will be marked on the map.
 */
public class PlacesSearchedOnTheMap {
    private final MapboxMap mapboxMap;
    List<LatLng> pointList = new ArrayList<>();

    /**
     * This method is the class constructor.
     *
     * @param mapboxMap the MapboxMap object to be configured.
     * @param context Get global information about the environment of an application.
     */
    public PlacesSearchedOnTheMap(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        pointTest();
        displayPoint(pointList);
    }

    /**
     * This method creates and displays waypoints on the map.
     * @param pointList list of places that will be marked on the map.
     */
    public void displayPoint(List<LatLng> pointList) {
        for (int i = 0; i < pointList.size(); i++) {
            LatLng aux = pointList.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(aux);
            mapboxMap.addMarker(markerOptions);
        }
    }

    /**
     * This method is for testing due to lack of functionality in the program.
     *
     * TODO: This method must be removed.
     */
    private void pointTest(){
        LatLng latLng = new LatLng(-17.365546, -66.173602);
        LatLng latLng2 = new LatLng(-17.366484, -66.175582);
        LatLng latLng3 = new LatLng(-17.371483, -66.176134);
        pointList.add(latLng);
        pointList.add(latLng2);
        pointList.add(latLng3);
    }
}