package com.isc.hermes.controller;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.utils.LocationListeningCallback;
import com.isc.hermes.utils.MapManager;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.Objects;

/**
 * The CurrentLocationController class is responsible for managing the current location functionality.
 * It handles initializing the location button, enabling the location component on the map,
 * and requesting location updates from the location engine.
 */
public class CurrentLocationController {
    private final LocationEngine locationEngine;
    private LocationListeningCallback locationListeningCallback;
    private AppCompatActivity activity;
    private final LocationPermissionsController locationPermissionsController;

    private MapboxMap mapboxMap;
    private CurrentLocationModel currentLocationModel;
    private static CurrentLocationController controllerInstance;

    /**
     * Constructs a new CurrentLocationController with the specified activity and map display.
     *
     * @param activity    The AppCompatActivity instance.
     */
    private CurrentLocationController(AppCompatActivity activity) {
        mapboxMap = MapManager.getInstance().getMapboxMap();
        locationEngine = LocationEngineProvider.getBestLocationEngine(activity);
        currentLocationModel = new CurrentLocationModel();
        locationListeningCallback = new LocationListeningCallback(activity, currentLocationModel);
        this.activity = activity;
        locationPermissionsController = new LocationPermissionsController(activity);
    }

    /**
     * Method for initializing the location button and setting its click listener.
     */
    @SuppressLint("WrongViewCast")
    public void initLocationButton() {
        ImageButton locationButton = activity.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(v -> {
            Log.i("tag","sdfvsdvsdv");
            mapboxMap = MapManager.getInstance().getMapboxMap();
            enableLocationComponent();
        });
    }

    /**
     * Method for enabling the location component on the map.
     */
    @SuppressWarnings("MissingPermission")
    private void enableLocationComponent() {
        if (locationPermissionsController.checkLocationPermissions()) {
            LocationComponentOptions locationComponentOptions =
                    LocationComponentOptions.builder(activity).pulseEnabled(true).build();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(
                                    activity, Objects.requireNonNull(mapboxMap.getStyle()))
                            .locationComponentOptions(locationComponentOptions).build();

            mapboxMap.getLocationComponent().activateLocationComponent(
                    locationComponentActivationOptions
            );
            mapboxMap.getLocationComponent().setLocationComponentEnabled(true);
            mapboxMap.getLocationComponent().setCameraMode(CameraMode.TRACKING);
            mapboxMap.getLocationComponent().setRenderMode(RenderMode.COMPASS);

            onLocationEngineConnected();
        } else {
            locationPermissionsController.requestLocationPermissionAccess();
        }
    }

    /**
     * Method called when the location engine is successfully connected.
     */
    @SuppressLint("MissingPermission")
    private void onLocationEngineConnected() {
        if (locationPermissionsController.checkLocationPermissions()) {
            LocationEngineRequest locationEngineRequest = new LocationEngineRequest.Builder(1000)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .build();

            locationEngine.requestLocationUpdates(
                    locationEngineRequest, locationListeningCallback, activity.getMainLooper()
            );
        } else
            Toast.makeText(activity, "Location permission denied.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Generates an instance of this class if an existing one is not found and returns it.
     *
     * @param activity Receives an AppCompacActivity to generate changes to the activity passed to it.
     * @return Returns a instance of this class.
     */
    public static CurrentLocationController getControllerInstance(AppCompatActivity activity){
        if(controllerInstance == null){
            controllerInstance = new CurrentLocationController(activity);
        }
        return controllerInstance;
    }

    /**
     * Returns the current CurrentLocationModel of the class.
     *
     * @return CurrentLocationModel type.
     */
    public CurrentLocationModel getCurrentLocationModel(){
        return currentLocationModel;
    }
}