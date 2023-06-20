package com.isc.hermes.controller.offline;

import android.app.Activity;
import android.widget.Toast;

import com.isc.hermes.utils.offline.OfflineMapManager;
import com.isc.hermes.utils.offline.OfflineUtils;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;


import timber.log.Timber;

/**
 * The RegionLoader class is responsible for loading offline regions.
 * It implements the OfflineManager.ListOfflineRegionsCallback interface.
 */
public class RegionLoader implements OfflineManager.ListOfflineRegionsCallback {
    private final Activity activity;

    /**
     * Constructs a new RegionLoader object.
     *
     * @param activity The activity in which the loading operation is performed.
     */
    public RegionLoader(Activity activity) {
        this.activity = activity;
    }

    /**
     * This method is called when the list of offline regions is retrieved successfully.
     *
     * @param offlineRegions An array of OfflineRegion objects representing the downloaded regions.
     */
    @Override
    public void onList(OfflineRegion[] offlineRegions) {
        if (offlineRegions == null || offlineRegions.length == 0) {
            Toast.makeText(activity, "THERE IS NO REGIONS DOWNLOADED YET", Toast.LENGTH_SHORT).show();
            return;
        }
        String regionName;
        for (OfflineRegion offlineRegion : offlineRegions) {
            regionName = OfflineUtils.getRegionName(offlineRegion);
            OfflineMapManager.getInstance(activity).addOfflineRegion(regionName, offlineRegion);
        }
        CardViewHandler.getInstance().notifyObservers();
    }

    /**
     * This method is called when an error occurs while retrieving the list of offline regions.
     *
     * @param error The error message describing the cause of the error.
     */
    @Override
    public void onError(String error) {
        Timber.e("Error: %s", error);
    }
}
