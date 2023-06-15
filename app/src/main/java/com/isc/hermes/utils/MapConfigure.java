package com.isc.hermes.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONException;

/**
 * Class for configuring a MapboxMap object.
 */
public class MapConfigure {

    private MapClickEventsManager manager;

    private PlacesSearchedManager placesSearchedManager;
    private IncidentsManager incidentsManager;
    private Context context;
    /**
     * Configures a MapboxMap object with the MAPBOX_STREETS style.
     *
     * @param mapboxMap the MapboxMap object to be configured
     */
    public void configure(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        manager = new MapClickEventsManager(mapboxMap,context);
        placesSearchedManager = new PlacesSearchedManager(mapboxMap,context);
        incidentsManager = new IncidentsManager(mapboxMap,context);
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
