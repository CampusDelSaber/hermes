package com.isc.hermes.utils;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * This class manages the waypoints on the mapBox map.
 */
public class PlacesSearchedManager {

    PlacesSearchedOnTheMap placesSearchedOnTheMap;

    /**
     * This method is the constructor of the class in which we will instantiate the waypoints.
     *
     * @param mapboxMap the MapboxMap object to be configured.
     * @param context Get global information about the environment of an application.
     */
    public PlacesSearchedManager(MapboxMap mapboxMap, Context context){
        placesSearchedOnTheMap = new PlacesSearchedOnTheMap(mapboxMap, context);
    }
}
