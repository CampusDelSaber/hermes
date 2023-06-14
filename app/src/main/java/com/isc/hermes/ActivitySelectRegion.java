package com.isc.hermes;

import static com.isc.hermes.controller.offline.OfflineController.LAT_LNG_BOUNDS;
import static com.isc.hermes.controller.offline.OfflineController.MAX_ZOOM;
import static com.isc.hermes.controller.offline.OfflineController.MIN_ZOOM;
import static com.isc.hermes.controller.offline.OfflineController.PIXEL_RATIO;
import static com.isc.hermes.controller.offline.OfflineController.REGION_NAME;
import static com.isc.hermes.controller.offline.OfflineController.STYLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
public class ActivitySelectRegion extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;

    private AlertDialog alertDialog;

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
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            double centerLatitude = bundle.getDouble("mapCenterLatitude");
            double centerLongitude = bundle.getDouble("mapCenterLongitude");
            configureMapView(centerLatitude, centerLongitude);
        }
        createPopup();
    }

    /**
     * This method configures the MapView with the provided center latitude and longitude.
     *
     * @param centerLatitude  The latitude value for the center of the map.
     * @param centerLongitude The longitude value for the center of the map.
     */
    private void configureMapView(double centerLatitude, double centerLongitude) {
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(centerLatitude, centerLongitude))
                    .zoom(12)
                    .build());
        });
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
            Style mapStyle = Objects.requireNonNull(mapboxMap.getStyle(), "Default");
            String styleUrl = mapStyle.getUri();
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
        alertDialog.show();
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
        intent.putExtra(STYLE, styleUrl);
        intent.putExtra(MIN_ZOOM, minZoom);
        intent.putExtra(MAX_ZOOM, maxZoom);
        intent.putExtra(PIXEL_RATIO, pixelRatio);
        intent.putExtra(LAT_LNG_BOUNDS, latLngBounds);
        intent.putExtra(REGION_NAME, regionName);
        return intent;
    }

    /**
     * This method creates the pop-up window for the user to name the region to download.
     */
    private void createPopup() {
        LinearLayout popupDialog = findViewById(R.id.region_dialog);
        View view = LayoutInflater.from(ActivitySelectRegion.this).inflate(R.layout.name_a_region_popup, popupDialog);
        Button naming = view.findViewById(R.id.ok_name_region_popup);
        Button close = view.findViewById(R.id.cancel_name_region_popup);
        EditText inputName = view.findViewById(R.id.inputName);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySelectRegion.this);
        builder.setView(view);
        alertDialog = builder.create();
        setDismissPopupAction(close);
        setRegionNameAction(naming, inputName.getText().toString());
    }

    /**
     * This method takes care of putting the popup closing function on a button.
     *
     * @param close button to close the popup
     */
    private void setDismissPopupAction(Button close) {
        close.setOnClickListener(v -> alertDialog.dismiss());
    }

    /**
     * This method takes care of setting the region name function to a button.
     *
     * @param naming     button to confirm region naming
     * @param regionName name the region to download
     */
    private void setRegionNameAction(Button naming, String regionName) {
        naming.setOnClickListener(v -> {
            sendData(regionName);
        });
    }

}