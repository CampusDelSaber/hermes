package com.isc.hermes.controller;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
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
        currentLocationModel = CurrentLocationModel.getInstance();
        locationListeningCallback = new LocationListeningCallback(activity);
        this.activity = activity;
        locationPermissionsController = new LocationPermissionsController(activity);
    }

    /**
     * Initializes the location functionality.
     * It initializes the location button and enables the location component on the map.
     */
    public void initLocation(){
        new Thread(() -> {
            MapboxMap map = MapManager.getInstance().getMapboxMap();
            while (mapboxMap == null || map== mapboxMap) {
                mapboxMap = MapManager.getInstance().getMapboxMap();
            }
            activity.runOnUiThread(this::enableLocationComponent);
        }).start();
    }

    /**
     * Method for initializing the location button and setting its click listener.
     */
    @SuppressLint("WrongViewCast")
    public void initLocationButton() {
        ImageButton locationButton = activity.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(v -> {
            mapboxMap = MapManager.getInstance().getMapboxMap();
            enableLocationComponent();
        });
    }

    /**
     * The isLocationEnabled method checks if the location is enabled on the device.
     *<p>
     * It verifies by checking the status of the GPS location is enabled, this
     * will be given in a boolean and it will check for network room verification.
     * </p>
     * @return true if GPS is enabled, otherwise false.
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gpsEnabled || networkEnabled;
    }

    /**
     * The method is going to check for enabling the location components on the map.
     */
    @SuppressWarnings("MissingPermission")
    private void enableLocationComponent() {
        if (locationPermissionsController.checkLocationPermissions()) {
            if (isLocationEnabled()) {
                activateLocationComponent();
            } else {
                showMessageToEnableGps();
            }
        } else {
            requestLocationPermissions();
        }
    }

    /**
     * The activateLocationComponent method is responsible for activating the location component.
     */
    private void activateLocationComponent() {
        LocationComponentOptions locationComponentOptions =
                LocationComponentOptions.builder(activity).pulseEnabled(true).build();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(
                                activity, Objects.requireNonNull(mapboxMap.getStyle()))
                        .locationComponentOptions(locationComponentOptions).build();

        if (isLocationEnabled()) {
            activateLocation(locationComponentActivationOptions);
            onLocationEngineConnected();
        } else {
            showMessageToEnableGps();
        }
    }

    /**
     * Displays the message to enable GPS on the device.
     */
    private void showMessageToEnableGps() {
        Toast.makeText(activity, "Please, turn on your GPS.", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method requests location permissions.
     * <p>
     * Method is in charge of requesting the location permissions if they are not present.
     * </p>
     */
    private void requestLocationPermissions() {
        locationPermissionsController.requestLocationPermissionAccess();
    }

    /**
     * Activates the location component with the given options.
     *
     * @param locationComponentActivationOptions The options to activate the location component.
     */
    @SuppressLint("MissingPermission")
    private void activateLocation(
            LocationComponentActivationOptions locationComponentActivationOptions
    ) {
        mapboxMap.getLocationComponent().activateLocationComponent(
                locationComponentActivationOptions
        );
        mapboxMap.getLocationComponent().setLocationComponentEnabled(true);
        mapboxMap.getLocationComponent().setCameraMode(CameraMode.TRACKING);
        mapboxMap.getLocationComponent().setRenderMode(RenderMode.COMPASS);
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
     * @param activity Receives an AppCompactActivity to generate changes to the activity passed to it.
     * @return Returns a instance of this class.
     */
    public static CurrentLocationController getControllerInstance(AppCompatActivity activity){
        if(activity == null){return controllerInstance;}
        if(controllerInstance == null || controllerInstance.isNewInstanceNeeded(activity)){
            controllerInstance = new CurrentLocationController(activity);}
        return controllerInstance;
    }
    /**
     * This method checks if a new instance of IncidentViewNavigation should be created.
     *
     * @param appActivity The activity in which the IncidentViewNavigation is used.
     * @return True if a new instance should be created, false otherwise.
     */
    private boolean isNewInstanceNeeded(AppCompatActivity appActivity) {
        return activity == null || activity != appActivity;
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
