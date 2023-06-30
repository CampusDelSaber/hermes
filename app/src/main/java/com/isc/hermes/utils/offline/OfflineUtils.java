package com.isc.hermes.utils.offline;

import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import timber.log.Timber;

/**
 * Utility class for offline-related operations and functionalities.
 */
public class OfflineUtils {

    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    /**
     * Gets the name of the offline region.
     *
     * @param offlineRegion The offline region.
     * @return The name of the region.
     */
    public static String getRegionName(OfflineRegion offlineRegion) {
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Timber.e("Failed to decode metadata: %s", exception.getMessage());
            regionName = String.format("%s", offlineRegion.getID());
        }
        return regionName;
    }

    /**
     * Creates the metadata for an offline region.
     *
     * @param regionName The name of the region.
     * @return The metadata as a byte array.
     */
    public static byte[] createMetadata(String regionName) {
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
     * Updates the metadata for an offline region.
     *
     * @param newName The name of the region.
     * @param previousMetadata the previous metadata
     * @return The metadata as a byte array.
     */
    public static byte[] updateMetadata(String newName, byte[] previousMetadata) throws UnsupportedEncodingException, JSONException {
          JSONObject jsonMetadata = new JSONObject(new String(previousMetadata, OfflineUtils.JSON_CHARSET));
            jsonMetadata.put(JSON_FIELD_REGION_NAME, newName);
            return jsonMetadata.toString().getBytes(OfflineUtils.JSON_CHARSET);
    }
}
