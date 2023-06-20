package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isc.hermes.controller.PopUp.DialogListener;
import com.isc.hermes.controller.PopUp.TextInputPopup;
import com.isc.hermes.model.RegionData;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.Objects;

/**
 * This activity allows the user to select a region on a MapView.
 */
public class ActivitySelectRegion extends AppCompatActivity implements DialogListener {

    private MapboxMap mapboxMap;
    private TextInputPopup textInputPopup;
    public static final String MAP_CENTER_LATITUDE = "mapCenterLatitude";
    public static final String MAP_CENTER_LONGITUDE = "mapCenterLongitude";
    private MapView mapView;

    /**
     * This method is called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_select_region);
        mapView = this.findViewById(R.id.mapViewRegion);
        mapView.onCreate(savedInstanceState);
        initializeMapBoxMap();
        initializePopup();
    }

    /**
     * Initializes the Mapbox map and performs necessary setup.
     */
    private void initializeMapBoxMap() {
        mapView.getMapAsync(mapboxMap -> {
            setStyle(mapboxMap);
            verifyDataReception(getIntent().getExtras());
        });
    }

    /**
     * Initializes the popup for text input.
     */
    private void initializePopup() {
        textInputPopup = new TextInputPopup(this, this);
    }

    /**
     * This method verifies if the necessary data has been received in the Bundle and configures the MapView accordingly.
     *
     * @param bundle The Bundle containing the required data.
     *               The Bundle must contain the following keys:
     *               - "mapCenterLatitude": The latitude value for the center of the map.
     *               - "mapCenterLongitude": The longitude value for the center of the map.
     */
    private void verifyDataReception(Bundle bundle) {
        if (bundle != null) {
            double centerLatitude = bundle.getDouble(MAP_CENTER_LATITUDE);
            double centerLongitude = bundle.getDouble(MAP_CENTER_LONGITUDE);
            configureMapView(centerLatitude, centerLongitude);
        }
    }

    /**
     * This method configures the MapView with the provided center latitude and longitude.
     *
     * @param centerLatitude  The latitude value for the center of the map.
     * @param centerLongitude The longitude value for the center of the map.
     */
    private void configureMapView(double centerLatitude, double centerLongitude) {
        if (mapboxMap != null && mapboxMap.getStyle() != null) {
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(centerLatitude, centerLongitude))
                    .zoom(12)
                    .build());
        }
    }

    /**
     * This method sets the map style to Mapbox Streets for the provided MapboxMap instance.
     *
     * @param map The MapboxMap instance to set the style for.
     */
    private void setStyle(MapboxMap map) {
        this.mapboxMap = map;
        map.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS));
    }

    /**
     * This method handles the cancel action and sets the result as canceled.
     *
     * @param view The cancel button view.
     */
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * This method is responsible for returning data from the map to the activity that instantiated it.
     *
     * @param regionName Name of the region to download
     */
    public void sendData(String regionName) {
        if (mapboxMap != null) {
            Style mapStyle = mapboxMap.getStyle();
            String styleUrl = mapStyle != null ? mapStyle.getUri() : Style.MAPBOX_STREETS;
            double minZoom = mapboxMap.getCameraPosition().zoom;
            double maxZoom = mapboxMap.getMaxZoomLevel();
            float pixelRatio = getResources().getDisplayMetrics().density;
            LatLngBounds latLngBounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
            Intent intent = createIntent(regionName, styleUrl, minZoom, maxZoom, pixelRatio, latLngBounds);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * This method selects the coordinates of the map you are viewing to download it.
     *
     * @param view the button to select the region to be downloaded
     */
    public void selectRegion(View view) {
        textInputPopup.showPopup();
    }

    /**
     * This method creates an intent with the selected region data.
     *
     * @param regionName   The region name to download
     * @param styleUrl     The URL of the selected map style.
     * @param minZoom      The minimum zoom level of the selected region.
     * @param maxZoom      The maximum zoom level of the selected region.
     * @param pixelRatio   The pixel ratio of the device screen.
     * @param latLngBounds The bounding box of the selected region.
     * @return The created intent with the selected region data.
     */
    private Intent createIntent(String regionName, String styleUrl, double minZoom, double maxZoom, float pixelRatio, LatLngBounds latLngBounds) {
        Intent intent = new Intent();
        RegionData regionData = new RegionData(regionName, styleUrl, minZoom, maxZoom, pixelRatio, latLngBounds);
        intent.putExtra("REGION_DATA", regionData);
        return intent;
    }

    @Override
    public void dialogClosed(String text) {
        textInputPopup.closePopup();
        sendData(text);
    }
}