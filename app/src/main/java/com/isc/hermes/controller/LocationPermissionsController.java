package com.isc.hermes.controller;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.isc.hermes.model.LocationPermissionsModel;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import java.util.List;

/**
 * The LocationPermissionsController class handles location permission management.
 * It checks for and requests location permissions, and notifies the user about the permission status.
 */
public class LocationPermissionsController implements PermissionsListener {

    private PermissionsManager permissionsManager;
    private AppCompatActivity activity;
    private LocationPermissionsModel locationPermissionsModel;

    /**
     * Constructs a new LocationPermissionsController with the specified activity.
     *
     * @param activity The AppCompatActivity instance.
     */
    public LocationPermissionsController(AppCompatActivity activity){
        this.activity = activity;
        locationPermissionsModel = new LocationPermissionsModel();
        locationPermissionsModel.setGranted(
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        );
    }

    /**
     * Checks if location permissions are granted.
     * If not granted, it requests location permission access.
     *
     * @return true if location permissions are granted, false otherwise.
     */
    public boolean checkLocationPermissions() {
        if (!locationPermissionsModel.isGranted())
            requestLocationPermissionAccess();

        return locationPermissionsModel.isGranted();
    }

    /**
     * Requests location permission access.
     * It initializes the PermissionsManager to handle the permission request.
     */
    public void requestLocationPermissionAccess(){
        permissionsManager = new PermissionsManager(this);
        permissionsManager.requestLocationPermissions(activity);
        locationPermissionsModel.setGranted(
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        );
    }

    /**
     * Called when an explanation is needed for the requested permissions.
     * Displays a toast message indicating the need for permissions.
     *
     * @param list The list of permissions that need explanation.
     */
    @Override
    public void onExplanationNeeded(List<String> list) {
        Toast.makeText(
                activity, "Need permissions for access to location", Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Method called when permissions are granted.
     */
    @Override
    public void onPermissionResult(boolean granted) {
        if (!granted) {
            Toast.makeText(activity, "Location permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

}
