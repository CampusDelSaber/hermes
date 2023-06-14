package com.isc.hermes;

import static com.isc.hermes.controller.offline.OfflineController.LAT_LNG_BOUNDS;
import static com.isc.hermes.controller.offline.OfflineController.MAX_ZOOM;
import static com.isc.hermes.controller.offline.OfflineController.MIN_ZOOM;
import static com.isc.hermes.controller.offline.OfflineController.PIXEL_RATIO;
import static com.isc.hermes.controller.offline.OfflineController.REGION_NAME;
import static com.isc.hermes.controller.offline.OfflineController.STYLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    /**
     *This method is called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
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
     * This method handles the download action and creates an intent with the selected region data.
     *
     * @param view The download button view.
     */
    public void download(View view) {
        if (mapboxMap != null) {
            Style mapStyle = Objects.requireNonNull(mapboxMap.getStyle(), "Default");
            String styleUrl = mapStyle.getUri();
            double minZoom = mapboxMap.getCameraPosition().zoom;
            double maxZoom = mapboxMap.getMaxZoomLevel();
            float pixelRatio = getResources().getDisplayMetrics().density;
            LatLngBounds latLngBounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;

            Intent intent = createIntent(styleUrl, minZoom, maxZoom, pixelRatio, latLngBounds);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * This method creates an intent with the selected region data.
     *
     * @param styleUrl      The URL of the selected map style.
     * @param minZoom       The minimum zoom level of the selected region.
     * @param maxZoom       The maximum zoom level of the selected region.
     * @param pixelRatio    The pixel ratio of the device screen.
     * @param latLngBounds  The bounding box of the selected region.
     * @return The created intent with the selected region data.
     */
    private Intent createIntent(String styleUrl, double minZoom, double maxZoom, float pixelRatio, LatLngBounds latLngBounds) {
        Intent intent = new Intent();
        intent.putExtra(STYLE, styleUrl);
        intent.putExtra(MIN_ZOOM, minZoom);
        intent.putExtra(MAX_ZOOM, maxZoom);
        intent.putExtra(PIXEL_RATIO, pixelRatio);
        intent.putExtra(LAT_LNG_BOUNDS, latLngBounds);
        intent.putExtra(REGION_NAME,"NAME_REGION");
        return intent;
    }

}