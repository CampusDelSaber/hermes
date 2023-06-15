package com.isc.hermes.utils;

import com.isc.hermes.controller.incidents.IncidentPointVisualizationController;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import android.content.Context;

public class IncidentsManager {

    private MapboxMap mapboxMap;
    private Context context;

    public IncidentsManager(MapboxMap mapboxMap, Context context) {
        new IncidentPointVisualizationController(mapboxMap, context);
    }
}
