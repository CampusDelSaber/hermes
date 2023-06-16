package com.isc.hermes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.io.Serializable;

/**
 * The RegionData class represents the data needed to create a region in Mapbox.
 */
public class RegionData implements Parcelable {
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
     * Sets the name of the region.
     *
     * @param regionName The region name to set.
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
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
     * Sets the URL of the map style for the region.
     *
     * @param styleUrl The style URL to set.
     */
    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
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
     * Sets the minimum zoom level for the region.
     *
     * @param minZoom The minimum zoom level to set.
     */
    public void setMinZoom(double minZoom) {
        this.minZoom = minZoom;
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
     * Sets the maximum zoom level for the region.
     *
     * @param maxZoom The maximum zoom level to set.
     */
    public void setMaxZoom(double maxZoom) {
        this.maxZoom = maxZoom;
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
     * Sets the pixel ratio of the device screen.
     *
     * @param pixelRatio The pixel ratio to set.
     */
    public void setPixelRatio(float pixelRatio) {
        this.pixelRatio = pixelRatio;
    }

    /**
     * Returns the bounding box of the region.
     *
     * @return The LatLngBounds of the region.
     */
    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    /**
     * Sets the bounding box of the region.
     *
     * @param latLngBounds The LatLngBounds to set.
     */
    public void setLatLngBounds(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }


    /**
     * This method describes the type of the Parcelable objects' contents.
     *
     * @return An integer representing the type of contents (in this case, always returns 0).
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This method writes the values of the class fields to the Parcel.
     *
     * @param dest  The Parcel to which the values will be written.
     * @param flags Additional options for writing the Parcel (not used in this case).
     */

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(regionName);
        dest.writeString(styleUrl);
        dest.writeDouble(minZoom);
        dest.writeDouble(maxZoom);
        dest.writeFloat(pixelRatio);
        dest.writeParcelable(latLngBounds, flags);
    }

    /**
     * This method constructs a new RegionData object from a Parcel.
     *
     * @param in The Parcel from which to read the RegionData object.
     */
    protected RegionData(Parcel in) {
        regionName = in.readString();
        styleUrl = in.readString();
        minZoom = in.readDouble();
        maxZoom = in.readDouble();
        pixelRatio = in.readFloat();
        latLngBounds = in.readParcelable(LatLngBounds.class.getClassLoader());
    }

    /**
     * This is a creator for the RegionData class, used to create new instances of RegionData from a Parcel.
     */

    public static final Creator<RegionData> CREATOR = new Creator<RegionData>() {
        @Override
        public RegionData createFromParcel(Parcel in) {
            return new RegionData(in);
        }

        @Override
        public RegionData[] newArray(int size) {
            return new RegionData[size];
        }
    };
}