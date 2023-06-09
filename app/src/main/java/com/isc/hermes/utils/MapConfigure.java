package com.isc.hermes.utils;


import androidx.annotation.NonNull;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;


/**
 * Class for configuring a MapboxMap object.
 */
public class MapConfigure {
    public static MapboxMap mapboxMapStatic = null;

    /**
     * Configures a MapboxMap object with the MAPBOX_STREETS style.
     *
     * @param mapboxMap the MapboxMap object to be configured
     */
    public void configure(@NonNull MapboxMap mapboxMap) {
        mapboxMapStatic = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
    }
}
