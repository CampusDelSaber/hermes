package com.isc.hermes.controller;
import android.annotation.SuppressLint;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.utils.LocationListeningCallback;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
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
    private final MapDisplay mapDisplay;
    private static CurrentLocationModel currentLocationModel;

    /**
     * Constructs a new CurrentLocationController with the specified activity and map display.
     *
     * @param activity    The AppCompatActivity instance.
     * @param mapDisplay  The MapDisplay instance.
     */
    public CurrentLocationController(AppCompatActivity activity, MapDisplay mapDisplay) {
        locationEngine = LocationEngineProvider.getBestLocationEngine(activity);
        currentLocationModel = new CurrentLocationModel();
        locationListeningCallback = new LocationListeningCallback(activity, currentLocationModel);
        this.activity = activity;
        locationPermissionsController = new LocationPermissionsController(activity);
        this.mapDisplay = mapDisplay;
    }

    /**
     * Initializes the location functionality.
     * It initializes the location button and enables the location component on the map.
     */
    public void initLocation(){
        initLocationButton();
        new Thread(() -> {
            while (mapDisplay.getMapboxMap() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(this::enableLocationComponent);
        }).start();
    }

    /**
     * Method for initializing the location button and setting its click listener.
     */
    @SuppressLint("WrongViewCast")
    private void initLocationButton() {
        ImageButton locationButton = activity.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(v -> enableLocationComponent());
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
                                    activity, Objects.requireNonNull(mapDisplay.getMapboxMap().getStyle()))
                    .locationComponentOptions(locationComponentOptions).build();

            mapDisplay.getMapboxMap().getLocationComponent().activateLocationComponent(
                    locationComponentActivationOptions
            );
            mapDisplay.getMapboxMap().getLocationComponent().setLocationComponentEnabled(true);
            mapDisplay.getMapboxMap().getLocationComponent().setCameraMode(CameraMode.TRACKING);
            mapDisplay.getMapboxMap().getLocationComponent().setRenderMode(RenderMode.COMPASS);

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
    public static CurrentLocationModel getInstance() {
        if (currentLocationModel == null) currentLocationModel = new CurrentLocationModel();
        return currentLocationModel;
    }
}
