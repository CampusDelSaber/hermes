package com.isc.hermes.model;

import com.mapbox.mapboxsdk.geometry.LatLngBounds;


/**
 * The RegionData class represents the data needed to create a region in Mapbox.
 */
public class RegionData {
    private String regionName;
    private String styleUrl;
    private double minZoom;
    private double maxZoom;
    private float pixelRatio;
    private LatLngBounds latLngBounds;

    /**
     * Constructs a new RegionData instance with the specified parameters.
     *
     * @param regionName   The name of the region.
     * @param styleUrl     The URL of the map style for the region.
     * @param minZoom      The minimum zoom level for the region.
     * @param maxZoom      The maximum zoom level for the region.
     * @param pixelRatio   The pixel ratio of the device screen.
     * @param latLngBounds The bounding box of the region.
     */
    public RegionData(String regionName, String styleUrl, double minZoom, double maxZoom,
                      float pixelRatio, LatLngBounds latLngBounds) {
        this.regionName = regionName;
        this.styleUrl = styleUrl;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.pixelRatio = pixelRatio;
        this.latLngBounds = latLngBounds;
    }

    /**
     * Returns the name of the region.
     *
     * @return The region name.
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * Returns the URL of the map style for the region.
     *
     * @return The style URL.
     */
    public String getStyleUrl() {
        return styleUrl;
    }

    /**
     * Returns the minimum zoom level for the region.
     *
     * @return The minimum zoom level.
     */
    public double getMinZoom() {
        return minZoom;
    }

    /**
     * Returns the maximum zoom level for the region.
     *
     * @return The maximum zoom level.
     */
    public double getMaxZoom() {
        return maxZoom;
    }

    /**
     * Returns the pixel ratio of the device screen.
     *
     * @return The pixel ratio.
     */
    public float getPixelRatio() {
        return pixelRatio;
    }

    /**
     * Returns the bounding box of the region.
     *
     * @return The LatLngBounds of the region.
     */
    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

}