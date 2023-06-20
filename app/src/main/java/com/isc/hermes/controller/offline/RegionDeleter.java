package com.isc.hermes.controller.offline;

import android.app.Activity;
import android.widget.Toast;

import com.isc.hermes.utils.offline.OfflineMapManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import timber.log.Timber;

/**
 * This class represents a RegionDeleter, which is responsible for deleting offline regions.
 * It implements the OfflineRegionDeleteCallback interface.
 */
public class RegionDeleter implements OfflineRegion.OfflineRegionDeleteCallback {

    private final Activity activity;
    private final String regionName;

    /**
     * Constructs a new RegionDeleter object.
     *
     * @param activity The activity in which the deletion operation is performed.
     * @param regionName The name of the region to be deleted.
     */
    public RegionDeleter(Activity activity, String regionName) {
        this.activity = activity;
        this.regionName = regionName;
    }

    /**
     * This method deletes the offline region and performs necessary cleanup tasks.
     * It is called when the region deletion is successful.
     */
    @Override
    public void onDelete() {
        OfflineMapManager.getInstance(activity).getOfflineRegions().remove(regionName);
        CardViewHandler.getInstance().notifyObservers();
        Toast.makeText(activity, "THE REGION HAS BEEN DELETED SUCCESSFULLY",
                Toast.LENGTH_LONG).show();
    }

    /**
     * This method handles the error that occurs during the region deletion process.
     *
     * @param error The error message describing the cause of the error.
     */
    @Override
    public void onError(String error) {
        Timber.e("Error: %s", error);
    }
}
