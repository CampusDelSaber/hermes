package com.isc.hermes.utils;
import android.content.Context;

import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * This class is in charged for managing offline maps.
 */
public class OfflineMapManager {
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;

    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    /**
     * This is a constructor method for OfflineMapManager.
     *
     * @param context The application context.
     */
    public OfflineMapManager(Context context) {
        offlineManager = OfflineManager.getInstance(context);
    }

    /**
     * This method defines the offline region definition for a specific map.
     *
     * @param styleUrl       The style URL of the map.
     * @param bounds         The bounds of the region.
     * @param minZoom        The minimum zoom level.
     * @param maxZoom        The maximum zoom level.
     * @param pixelRatio     The pixel ratio.
     * @return The offline tile pyramid region definition.
     */
    public OfflineTilePyramidRegionDefinition defineOfflineRegion(String styleUrl, LatLngBounds bounds,
                                                                  double minZoom, double maxZoom, float pixelRatio) {
        return new OfflineTilePyramidRegionDefinition(styleUrl, bounds, minZoom, maxZoom, pixelRatio);
    }

    /**
     * This method creates the metadata for the offline region.
     *
     * @param regionName The name of the region.
     * @return The metadata as a byte array.
     */
    public byte[] createMetadata(String regionName) {
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Timber.e("Failed to encode metadata: %s", exception.getMessage());
            metadata = null;
        }
        return metadata;
    }

    /**
     * This method creates the offline region.
     *
     * @param regionName                      The name of the region.
     * @param offlineTilePyramidRegionDefinition The offline tile pyramid region definition.
     * @param offlineRegionCallback           The callback for creating the offline region.
     */
    public void createOfflineRegion(String regionName, OfflineTilePyramidRegionDefinition offlineTilePyramidRegionDefinition,
                                    OfflineManager.CreateOfflineRegionCallback offlineRegionCallback) {
        offlineManager.createOfflineRegion(
                offlineTilePyramidRegionDefinition
                , createMetadata(regionName)
                , offlineRegionCallback);
    }

    /**
     * This method gets the name of the offline region.
     *
     * @param offlineRegion The offline region.
     * @return The name of the region.
     */
    public String getRegionName(OfflineRegion offlineRegion) {
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Timber.e("Failed to decode metadata: %s", exception.getMessage());
            regionName = String.format("", offlineRegion.getID());
        }
        return regionName;
    }

    /**
     * This method lists all the offline regions.
     *
     * @param listOfflineRegionsCallback The callback for listing the offline regions.
     */
    public void listRegions(OfflineManager.ListOfflineRegionsCallback listOfflineRegionsCallback) {
        offlineManager.listOfflineRegions(listOfflineRegionsCallback);
    }

    /**
     * This method deletes the offline region.
     *
     * @param offlineRegion                The offline region to delete.
     * @param offlineRegionDeleteCallback The callback for deleting the offline region.
     */
    public void deleteRegion(OfflineRegion offlineRegion, OfflineRegion.OfflineRegionDeleteCallback offlineRegionDeleteCallback) {
        offlineRegion.delete(offlineRegionDeleteCallback);
    }

    /**
     * This method gets the offline manager.
     *
     * @return The offline manager.
     */
    public OfflineManager getOfflineManager() {
        return offlineManager;
    }

    /**
     * This method sets the offline manager.
     *
     * @param offlineManager The offline manager.
     */
    public void setOfflineManager(OfflineManager offlineManager) {
        this.offlineManager = offlineManager;
    }

    /**
     * This method gets the offline region.
     *
     * @return The offline region.
     */
    public OfflineRegion getOfflineRegion() {
        return offlineRegion;
    }

    /**
     * This method sets the offline region.
     *
     * @param offlineRegion The offline region.
     */
    public void setOfflineRegion(OfflineRegion offlineRegion) {
        this.offlineRegion = offlineRegion;
    }
}