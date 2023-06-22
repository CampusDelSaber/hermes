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
    private IncidentsManager incidentsManager;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }
}