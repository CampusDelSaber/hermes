package com.isc.hermes.utils;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

public class PlacesSearchedManager {

    PlacesSearchedOnTheMap placesSearchedOnTheMap;

    public PlacesSearchedManager(MapboxMap mapboxMap, Context context){
        placesSearchedOnTheMap = new PlacesSearchedOnTheMap(mapboxMap, context);
    }
}
