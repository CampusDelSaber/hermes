package com.isc.hermes.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.isc.hermes.model.Utils.PolylineManager;
import com.isc.hermes.utils.MapClickEventsManager;
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
    private final Context context;
    private MapboxMap mapboxMap;
    private static MapDisplay instance;

    /**
     * Constructor to create a MapDisplay object.
     *
     * @param mapView      the MapView object to display the map
     * @param mapConfigure the MapConfigure object to configure the map
     */
    public MapDisplay(Context context, MapView mapView, MapConfigure mapConfigure) {
        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
        this.context = context;
        mapConfigure.setContext(context);
    }

    public static MapDisplay getInstance(Context context, MapView mapView, MapConfigure mapConfigure) {
        if (instance == null) instance = new MapDisplay(context, mapView, mapConfigure);
        return instance;
    }



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

    /**
     * Getter for the MapboxMap object.
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
