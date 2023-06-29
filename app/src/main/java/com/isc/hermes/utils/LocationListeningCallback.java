package com.isc.hermes.utils;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.model.CurrentLocationModel;
import com.mapbox.android.core.location.LocationEngineCallback;
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

    /**
     * Constructs a new LocationListeningCallback with the specified activity and currentLocationModel.
     *
     * @param activity             The AppCompatActivity to hold a weak reference to.
     */
    public LocationListeningCallback(AppCompatActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.currentLocationModel = CurrentLocationModel.getInstance();
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
        return;
    }
}


