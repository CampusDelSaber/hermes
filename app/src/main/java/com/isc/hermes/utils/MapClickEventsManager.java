package com.isc.hermes.utils;


import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to manage two types of click on mapBox map
 */
public class MapClickEventsManager {

    public static boolean isOnReportIncidentMode = false;

    public MapClickEventsManager(MapboxMap mapboxMap, Context context){
        new MapReportIncidentClickEventListener(mapboxMap, context);
        new MapReportIncidentLongClickEventListener(mapboxMap);
    }

}
