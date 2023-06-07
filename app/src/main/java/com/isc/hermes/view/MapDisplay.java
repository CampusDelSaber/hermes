package com.isc.hermes.view;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.isc.hermes.utils.MapConfigure;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;
    private String mapStyle;

    /**
     * Constructor to create a MapDisplay object.
     *
     * @param mapView the MapView object to display the map
     * @param mapConfigure the MapConfigure object to configure the map
     */
    public MapDisplay(MapView mapView, MapConfigure mapConfigure, String mapStyle) {
        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
        this.mapStyle = mapStyle;
    }

    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapConfigure::configure);
        mapView.getMapAsync(mapConfigure::configure);
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
     * Method to set the map style to satellite, dark and default.
     *
     * @param mapStyle is the style for the map.
     */
    public void setMapStyle(String mapStyle) {
        this.mapStyle = mapStyle;
        if (mapView != null && mapStyle != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    if (mapStyle.equals("satellite")) {
                        mapboxMap.setStyle(Style.SATELLITE_STREETS);
                    } else if (mapStyle.equals("dark")){
                        mapboxMap.setStyle(Style.DARK);
                    } else{
                        mapboxMap.setStyle(Style.MAPBOX_STREETS);
                    }
                }
            });
        }
    }
}
