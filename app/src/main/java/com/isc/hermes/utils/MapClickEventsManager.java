package com.isc.hermes.utils;


import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to manage two types of click on mapBox map
 */
public class MapClickEventsManager {

    MapClickEventListener mapClickEventListener;

    MapLongClickEventListener mapLongClickEventListener;

    public MapClickEventsManager(MapboxMap mapboxMap){
        mapClickEventListener = new MapClickEventListener(mapboxMap);
        mapLongClickEventListener = new MapLongClickEventListener(mapboxMap);
    }
}
