package com.isc.hermes.utils.offline;

import android.content.Context;

import com.isc.hermes.R;
import com.isc.hermes.model.RegionData;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.util.HashMap;
import java.util.Map;



/**
 * This class is responsible for managing offline maps.
 */
public class MapboxOfflineManager {
    private final OfflineManager offlineManager;
    private Map<String, OfflineRegion> offlineRegions;

    private static MapboxOfflineManager offlineMapManager;

    /**
     * Returns the singleton instance of OfflineMapManager.
     *
     * @param context The application context.
     * @return The instance of OfflineMapManager.
     */
    public static MapboxOfflineManager getInstance(Context context) {
        if (offlineMapManager == null) {
            offlineMapManager = new MapboxOfflineManager(context);
        }
        return offlineMapManager;
    }

    /**
     * Constructs a new OfflineMapManager.
     *
     * @param context The application context.
     */
    public MapboxOfflineManager(Context context) {
        Mapbox.getInstance(context, context.getString(R.string.access_token));
        offlineManager = OfflineManager.getInstance(context);
    }


    /**
     * Creates an offline region using the provided region data and metadata.
     *
     * @param regionData              The region data.
     * @param offlineRegionCallback   The callback for creating the offline region.
     */
    public void createOfflineRegion(RegionData regionData, OfflineManager.CreateOfflineRegionCallback offlineRegionCallback) {
        offlineManager.createOfflineRegion(
                getDefinition(regionData),
                OfflineUtils.createMetadata(regionData.getRegionName()),
                offlineRegionCallback);
    }

    /**
     * Retrieves the definition of a region.
     *
     * @param regionData The region data.
     * @return The definition of the region.
     */
    public OfflineTilePyramidRegionDefinition getDefinition(RegionData regionData) {
        return new OfflineTilePyramidRegionDefinition(
                regionData.getStyleUrl(),
                regionData.getLatLngBounds(),
                regionData.getMinZoom(),
                regionData.getMaxZoom(),
                regionData.getPixelRatio());
    }



    /**
     * Lists all the offline regions.
     *
     * @param listOfflineRegionsCallback The callback for listing the offline regions.
     */
    public void listRegions(OfflineManager.ListOfflineRegionsCallback listOfflineRegionsCallback) {
        offlineManager.listOfflineRegions(listOfflineRegionsCallback);
    }

    /**
     * Deletes an offline region.
     *
     * @param regionName                   The name of the region to delete.
     * @param offlineRegionDeleteCallback  The callback for deleting the offline region.
     */
    public void deleteRegion(String regionName, OfflineRegion.OfflineRegionDeleteCallback offlineRegionDeleteCallback) {
        OfflineRegion offlineRegion = getOfflineRegion(regionName);
        if (offlineRegion != null) {
            offlineRegion.delete(offlineRegionDeleteCallback);
        }
    }

    /**
     * Gets the map of offline regions.
     *
     * @return The map of offline regions.
     */
    public Map<String, OfflineRegion> getOfflineRegions() {
        if (offlineRegions == null) offlineRegions = new HashMap<>();
        return offlineRegions;
    }

    /**
     * Adds an offline region to the map of offline regions.
     *
     * @param name           The name of the region.
     * @param offlineRegion  The offline region object.
     */
    public void addOfflineRegion(String name, OfflineRegion offlineRegion) {
        if (offlineRegions == null) offlineRegions = new HashMap<>();
        offlineRegions.put(name, offlineRegion);
    }

    /**
     * Gets an offline region by its name.
     *
     * @param name The name of the offline region.
     * @return The offline region object, or null if not found.
     */
    public OfflineRegion getOfflineRegion(String name) {
        OfflineRegion offlineRegion = null;
        if (offlineRegions != null) offlineRegion = offlineRegions.get(name);
        return offlineRegion;
    }
}
