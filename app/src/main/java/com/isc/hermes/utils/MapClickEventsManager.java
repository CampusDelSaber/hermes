package com.isc.hermes.utils;


import android.content.Context;

import com.isc.hermes.controller.MapController;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to manage click on mapBox map
 */
public class MapClickEventsManager {

    /**
     * Class to set click manager to mapbox map
     * @param mapboxMap Is map set
     * @param context Is mproject context
     */
    public MapClickEventsManager(MapboxMap mapboxMap, Context context){
        new MapController(mapboxMap, context);
    }

}
