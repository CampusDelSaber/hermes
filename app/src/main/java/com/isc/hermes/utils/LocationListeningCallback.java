package com.isc.hermes.utils;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.controller.LocationPermissionsController;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.location.LocationIntervals;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import java.lang.ref.WeakReference;
/**
 * The LocationListeningCallback class is used as a callback for receiving location updates
 * from the location engine. It implements the LocationEngineCallback interface.
 * It retrieves the current location and updates the CurrentLocationModel.
 */
public class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {
    private final WeakReference<AppCompatActivity> activityWeakReference;
    private CurrentLocationModel currentLocationModel;
    private LocationEngine locationEngine;
    private LocationPermissionsController locationPermissionsController;
    private final int UPDATE_INTERVAL_MS = 4000;
    private final float SMALLEST_DISPLACEMENT_METERS = 100f;

    /**
     * Constructs a new LocationListeningCallback with the specified activity and currentLocationModel.
     *
     * @param activity             The AppCompatActivity to hold a weak reference to.
     * @param currentLocationModel The CurrentLocationModel to update with the received location.
     */
    public LocationListeningCallback(
            AppCompatActivity activity, CurrentLocationModel currentLocationModel,
            LocationEngine locationEngine, LocationPermissionsController locationPermissionsController
    ) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.currentLocationModel = currentLocationModel;
        this.locationEngine = locationEngine;
        this.locationPermissionsController = locationPermissionsController;
    }

    /**
     * Called when the location engine successfully retrieves a location update.
     * Updates the CurrentLocationModel with the latitude and longitude of the received location.
     *
     * @param result The LocationEngineResult containing the last received location.
     */
    @Override
    public void onSuccess(LocationEngineResult result) {
        AppCompatActivity activity = activityWeakReference.get();
        if (activity == null) return;

        Location location = result.getLastLocation();
        if (location != null) {
            currentLocationModel.setLatitude(location.getLatitude());
            currentLocationModel.setLongitude(location.getLongitude());
        }
    }

    /**
     * Called when the location engine fails to retrieve a location update.
     * This method is currently empty and does not handle the exception.
     *
     * @param exception The exception that occurred during location retrieval.
     */
    @Override
    public void onFailure(@NonNull Exception exception) {
        new Handler().postDelayed(this::tryGettingCurrentLocationAgain, 2000);
    }

    @SuppressLint("MissingPermission")
    private void tryGettingCurrentLocationAgain(){
        AppCompatActivity activity = activityWeakReference.get();
        if (activity != null && locationPermissionsController.checkLocationPermissions()) {
            LocationEngineRequest locationEngineRequest =
                    new LocationEngineRequest.Builder(
                        (long) LocationIntervals.UPDATE_INTERVAL_MS.getValue()
                    )
                    .setFastestInterval((long) LocationIntervals.UPDATE_INTERVAL_MS.getValue())
                    .setDisplacement(LocationIntervals.SMALLEST_DISPLACEMENT_METERS.getValue())
                    .setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .build();

            locationEngine.requestLocationUpdates(
                    locationEngineRequest, this, activity.getMainLooper()
            );
        } else
            Toast.makeText(activity, "Location permission denied.", Toast.LENGTH_SHORT).show();
    }
}


