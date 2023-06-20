package com.isc.hermes.controller.offline;

import com.isc.hermes.model.RegionData;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

/**
 * Repository class for managing offline data related to a region.
 */
public class OfflineDataRepository {
    private RegionData regionData;
    private static OfflineDataRepository offlineDataRepository;

    /**
     * Retrieves the singleton instance of the OfflineDataRepository.
     *
     * @return The OfflineDataRepository instance.
     */
    public static OfflineDataRepository getInstance() {
        if (offlineDataRepository == null)
            offlineDataRepository = new OfflineDataRepository();
        return offlineDataRepository;
    }

    /**
     * Retrieves the region data associated with the offline data repository.
     *
     * @return The region data.
     */
    public RegionData getRegionData() {
        return regionData;
    }

    /**
     * Sets the region data for the offline data repository.
     *
     * @param regionData The region data to be set.
     */
    public void setRegionData(RegionData regionData) {
        this.regionData = regionData;
    }

    /**
     * This method saves the region data.
     *
     * @param regionName   The region name to download
     * @param styleUrl     The URL of the selected map style.
     * @param minZoom      The minimum zoom level of the selected region.
     * @param maxZoom      The maximum zoom level of the selected region.
     * @param pixelRatio   The pixel ratio of the device screen.
     * @param latLngBounds The bounding box of the selected region.
     */
    public void saveData(String regionName, String styleUrl, double minZoom, double maxZoom, float pixelRatio, LatLngBounds latLngBounds) {
        this.regionData = new RegionData(regionName, styleUrl, minZoom, maxZoom, pixelRatio, latLngBounds);
    }
}
