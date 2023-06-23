package com.isc.hermes.view;

import android.content.Context;
import android.os.Bundle;

import com.isc.hermes.controller.IncidentsGetterController;
import com.isc.hermes.utils.MapConfigure;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.MapboxMap;


/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;
    private MapboxMap mapboxMap;
    private static MapDisplay instance;
    private IncidentsGetterController incidentsGetterController;
    private Context context;

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
        incidentsGetterController = IncidentsGetterController.getInstance();
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
    /**
     * This method animates the camera position on the Mapbox map to the specified latitude and longitude with a given zoom level.
     *
     * @param latLng The latitude and longitude to move the camera to.
     * @param zoom The zoom level for the camera position.
     */
    public void animateCameraPosition(LatLng latLng,double zoom){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500);
    }

    /**
     * Obtains the MapView of the map.
     *
     * @return MapView object.
     */
    public MapView getMapView() {
        return mapView;
    }
}
