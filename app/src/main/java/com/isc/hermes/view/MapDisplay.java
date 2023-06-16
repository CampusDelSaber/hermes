package com.isc.hermes.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

<<<<<<< HEAD
import com.isc.hermes.controller.IncidentsGetterController;
=======
import androidx.annotation.NonNull;

import com.isc.hermes.model.Utils.PolylineManager;
import com.isc.hermes.utils.MapClickEventsManager;
>>>>>>> 2821b69c7396e94cc7b4fa3720daf472b2fe84a5
import com.isc.hermes.utils.MapConfigure;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;
    private MapboxMap mapboxMap;
    private IncidentsGetterController incidentsGetterController;
    private Context context;

    /**
     * Constructor to create a MapDisplay object.
     *
     * @param mapView       the MapView object to display the map
     * @param mapConfigure  the MapConfigure object to configure the map
     */
    public MapDisplay(Context context, MapView mapView, MapConfigure mapConfigure) {
        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
        this.context = context;
        mapConfigure.setContext(context);
        incidentsGetterController = IncidentsGetterController.getInstance();
    }

<<<<<<< HEAD
=======



>>>>>>> 2821b69c7396e94cc7b4fa3720daf472b2fe84a5
    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            mapConfigure.configure(mapboxMap);

            mapboxMap.addOnCameraIdleListener(
                    () -> incidentsGetterController.getNearIncidentsWithinRadius(mapboxMap, context)
            );
        });
    }

    /**
     * Method for starting the MapView object instance.
     */
    public void onStart() {
        mapView.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    public void onResume() {
        mapView.onResume();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    public void onPause() {
        mapView.onPause();
    }

    /**
     * Method for stopping the MapView object instance.
     */
    public void onStop() {
        mapView.onStop();
    }

    /**
     * Method for handling low memory situations.
     */
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    public void onDestroy() {
        mapView.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance to save
     */
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    /** Getter for the MapboxMap object.
     *
     * @return the MapboxMap object
     */
    public MapboxMap getMapboxMap() {
        return mapboxMap;
    }

    /**
     * Method to set the map style to satellite, dark and default.
     *
     * @param mapStyle is the style for the map.
     */
    public void setMapStyle(String mapStyle) {
        if (mapView != null && mapStyle != null) {
            mapView.getMapAsync(mapboxMap -> {
                switch (mapStyle) {
                    case "Satellite" -> mapboxMap.setStyle(Style.SATELLITE_STREETS);
                    case "Dark" -> mapboxMap.setStyle(Style.DARK);
                    case "Default" -> mapboxMap.setStyle(Style.MAPBOX_STREETS);
                }
            });
        }
    }
}
