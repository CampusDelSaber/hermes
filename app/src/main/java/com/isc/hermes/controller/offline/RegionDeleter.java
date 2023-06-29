package com.isc.hermes.controller.offline;

import android.app.Activity;
import android.app.ProgressDialog;

import com.isc.hermes.utils.offline.MapboxOfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import timber.log.Timber;

/**
 * This class represents a RegionDeleter, which is responsible for deleting offline regions.
 * It implements the OfflineRegionDeleteCallback interface.
 */
public class RegionDeleter implements OfflineRegion.OfflineRegionDeleteCallback {

    private final Activity activity;
    private final String regionName;
    private final boolean notifyDeleted;
    private ProgressDialog progressDialog;

    /**
     * Constructs a new RegionDeleter object.
     *
     * @param activity      The activity in which the deletion operation is performed.
     * @param regionName    The name of the region to be deleted.
     * @param notifyDeleted this param decide if notify to the user when the delete process finish.
     */
    public RegionDeleter(Activity activity, String regionName, boolean notifyDeleted) {
        this.activity = activity;
        this.regionName = regionName;
        this.notifyDeleted = notifyDeleted;
        initProgressDialog(notifyDeleted);
    }

    /**
     * This method show the deleting process.
     *
     * @param notifyDeleted this boolean decide if the progress task  will be showed.
     * TODO: Change this deprecated Progress Dialog
     */
    private void initProgressDialog(Boolean notifyDeleted) {
        if (notifyDeleted) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Deleting ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    /**
     * This method deletes the offline region and performs necessary cleanup tasks.
     * It is called when the region deletion is successful.
     */
    @Override
    public void onDelete() {
        if (notifyDeleted) {
            MapboxOfflineManager.getInstance(activity).getOfflineRegions().remove(regionName);
            CardViewHandler.getInstance().notifyObservers();
            progressDialog.dismiss();
        }
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
