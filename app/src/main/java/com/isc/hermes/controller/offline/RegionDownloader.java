package com.isc.hermes.controller.offline;

import android.app.Activity;
import android.widget.Toast;

import com.isc.hermes.controller.PopUp.ProgressDialogManager;
import com.isc.hermes.utils.offline.MapboxOfflineManager;
import com.isc.hermes.utils.offline.OfflineUtils;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;

import timber.log.Timber;

/**
 * This class  is responsible for downloading offline regions.
 * It implements the OfflineManager.CreateOfflineRegionCallback interface.
 */
public class RegionDownloader implements OfflineManager.CreateOfflineRegionCallback {
    private final Activity activity;
    private final ProgressDialogManager progressManager;

    /**
     * Constructs a new RegionDownloader object.
     *
     * @param activity The activity in which the download operation is performed.
     */
    public RegionDownloader(Activity activity) {
        this.activity = activity;
        progressManager = new ProgressDialogManager(activity, "Downloading ...");
    }

    /**
     * This method is called when the offline region is created successfully.
     * It launches the download of the region.
     *
     * @param offlineRegion The offline region that was created.
     */
    @Override
    public void onCreate(OfflineRegion offlineRegion) {
        launchDownload(offlineRegion);
    }

    /**
     * This method is called when an error occurs during the creation of the offline region.
     *
     * @param error The error message describing the cause of the error.
     */
    @Override
    public void onError(String error) {
        Timber.e("Error: %s", error);
    }

    /**
     * Launches the download of a region.
     *
     * @param offlineRegion The region to download.
     */
    private void launchDownload(OfflineRegion offlineRegion) {
        progressManager.showProgressDialog();
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                        0.0;
                if (status.isComplete()) {
                    completeDownload(offlineRegion);
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    progressManager.updateProgress((int) Math.round(percentage));
                }
                printLogStatus(status);
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Toast.makeText(activity, "The region is too large please select a smaller one.", Toast.LENGTH_SHORT).show();
                Timber.e("Mapbox tile count limit exceeded: %s", limit);
            }
        });

        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    /**
     * This method print the Download status in the logcat
     *
     * @param status offline download status.
     */
    private void printLogStatus(OfflineRegionStatus status) {
        Timber.d("%s/%s resources; %s bytes downloaded.",
                String.valueOf(status.getCompletedResourceCount()),
                String.valueOf(status.getRequiredResourceCount()),
                String.valueOf(status.getCompletedResourceSize()));
    }

    /**
     * This method manage the state when a download is finished.
     *
     * @param offlineRegion offline downloaded.
     */
    private void completeDownload(OfflineRegion offlineRegion) {
        MapboxOfflineManager.getInstance(activity).addOfflineRegion(
                OfflineUtils.getRegionName(offlineRegion),
                offlineRegion
        );
        CardViewHandler.getInstance().notifyObservers();
        progressManager.dismissProgressDialog();
    }
}
