package com.isc.hermes.controller.offline;

import android.app.Activity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;

import timber.log.Timber;
/**
 * This class represents a RegionDownloader, which is responsible for downloading offline regions.
 * It implements the OfflineManager.CreateOfflineRegionCallback interface.
 */
public class RegionDownloader implements OfflineManager.CreateOfflineRegionCallback {
    private final Activity activity;

    /**
     * Constructs a new RegionDownloader object.
     *
     * @param activity The activity in which the download operation is performed.
     */
    public RegionDownloader(Activity activity) {
        this.activity = activity;
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
        Toast.makeText(activity, "THE DOWNLOAD HAS STARTED", Toast.LENGTH_SHORT).show();
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                if (status.isComplete()) {
                    Toast.makeText(activity, "REGION DOWNLOADED", Toast.LENGTH_SHORT).show();
                    CardViewHandler.getInstance().notifyObservers();
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // download status
                }

                Timber.d("%s/%s resources; %s bytes downloaded.",
                        String.valueOf(status.getCompletedResourceCount()),
                        String.valueOf(status.getRequiredResourceCount()),
                        String.valueOf(status.getCompletedResourceSize()));
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Timber.e("Mapbox tile count limit exceeded: %s", limit);
            }
        });

        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }
}
