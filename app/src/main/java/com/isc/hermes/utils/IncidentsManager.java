package com.isc.hermes.utils;

import com.isc.hermes.controller.incidents.IncidentPointVisualizationController;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import android.content.Context;

/**
 * The manager class for handling incidents.
 */
public class IncidentsManager {

    /**
     * Constructor for creating an instance of the IncidentsManager.
     * Initializes the IncidentPointVisualizationController.
     * @param mapboxMap the MapboxMap object representing the map
     * @param context the context of the application
     */
    public IncidentsManager(MapboxMap mapboxMap, Context context) {
        IncidentPointVisualizationController.getInstance(mapboxMap, context);
    }
}