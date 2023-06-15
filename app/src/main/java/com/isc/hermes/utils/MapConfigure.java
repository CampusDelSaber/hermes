package com.isc.hermes.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.isc.hermes.controller.MapWayPointController;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

/**
 * Class for configuring a MapboxMap object.
 */
public class MapConfigure {
    private PlacesSearchedManager placesSearchedManager;
    private Context context;
    /**
     * Configures a MapboxMap object with the MAPBOX_STREETS style.
     *
     * @param mapboxMap the MapboxMap object to be configured
     */
    public void configure(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        MapClickEventsManager.getInstance().setMapboxMap(mapboxMap);
        MapClickEventsManager.getInstance().setContext(context);
        MapClickEventsManager.getInstance().setMapClickConfiguration(new MapWayPointController(MapClickEventsManager.getInstance().getMapboxMap(),context));
        placesSearchedManager = new PlacesSearchedManager(mapboxMap,context);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
