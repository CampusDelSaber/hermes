package com.isc.hermes.utils;


import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to manage two types of click on mapBox map
 */
public class MapClickEventsManager {

    MapClickEventListener mapClickEventListener;

    MapLongClickEventListener mapLongClickEventListener;

    public MapClickEventsManager(MapboxMap mapboxMap, Context context){
        mapClickEventListener = new MapClickEventListener(mapboxMap,context);
        mapLongClickEventListener = new MapLongClickEventListener(mapboxMap);
    }

}
