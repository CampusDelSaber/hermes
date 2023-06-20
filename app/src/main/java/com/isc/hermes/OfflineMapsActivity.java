package com.isc.hermes;

import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LATITUDE;
import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LONGITUDE;
import static com.isc.hermes.ActivitySelectRegion.ZOOM_LEVEL;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.isc.hermes.controller.offline.OfflineDataRepository;
import com.isc.hermes.model.RegionData;
import com.isc.hermes.controller.offline.CardViewHandler;
import com.isc.hermes.view.OfflineCardView;
import com.isc.hermes.utils.offline.OfflineMapManager;
import com.isc.hermes.controller.offline.RegionDeleter;
import com.isc.hermes.controller.offline.RegionDownloader;
import com.isc.hermes.controller.offline.RegionLoader;
import com.isc.hermes.controller.offline.RegionObserver;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineRegion;


/**
 * This class represents the Offline Mode Settings UI.
 */
public class OfflineMapsActivity extends AppCompatActivity implements RegionObserver {
    private static final int REQUEST_CODE_OFFLINE = 231;
    private static final int RENAME = R.id.rename, NAVIGATE_TO = R.id.navigateTo, DELETE = R.id.delete;
    private LinearLayout vBoxDownloadedMaps;
    private OfflineCardView offlineCardView;

    /**
     * Method for creating the activity configuration.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_mode_settings_view);
        offlineCardView = new OfflineCardView(this);
        vBoxDownloadedMaps = findViewById(R.id.vBoxMapsDownloaded);
        CardViewHandler.getInstance().addObserver(this);
    }


    /**
     * Closes the activity and returns to the map.
     *
     * @param view The view that triggered the click event.
     */
    public void backToMap(View view) {
        finish();
    }

    /**
     * Selects a new region.
     *
     * @param view The view that triggered the event.
     */
    public void selectNewRegion(View view) {
        Bundle bundle = getIntent().getExtras();
        Intent intent = new Intent(this, ActivitySelectRegion.class);
        intent.putExtra(MAP_CENTER_LATITUDE, bundle.getDouble("lat"));
        intent.putExtra(MAP_CENTER_LONGITUDE, bundle.getDouble("long"));
        intent.putExtra(ZOOM_LEVEL, bundle.getDouble("zoom"));

        startActivityForResult(intent, REQUEST_CODE_OFFLINE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OFFLINE && resultCode == RESULT_OK) {
            RegionData regionData = OfflineDataRepository.getInstance().getRegionData();
            if (regionData != null)
                downloadRegion(regionData);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadRegions();
    }

    /**
     * Navigates to the selected downloaded map.
     */
    protected void navigateToDownloadedMap(String regionName) {
        try {
            OfflineRegion offlineRegion = OfflineMapManager.getInstance(this).getOfflineRegion(regionName);
            if (offlineRegion == null) throw new NullPointerException();
            LatLngBounds bounds = offlineRegion.getDefinition().getBounds();
            double regionZoom = offlineRegion.getDefinition().getMinZoom();
            Intent intent = new Intent();
            intent.putExtra("center", bounds.getCenter());
            intent.putExtra("zoom", regionZoom);
            setResult(RESULT_OK, intent);
            finish();
        } catch (NullPointerException ignored) {
            Toast.makeText(this, "The map does not exist in your downloads", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Renames the selected downloaded map.
     */
    protected void renameDownloadedMap() {

    }

    /**
     * Loads the available regions.
     */
    public void loadRegions() {
        OfflineMapManager.getInstance(this).listRegions(new RegionLoader(this));
    }

    /**
     * Deletes a region.
     *
     * @param regionName The name of the region to delete.
     */
    private void deleteRegion(String regionName) {
        OfflineMapManager.getInstance(this)
                .deleteRegion(
                        regionName,
                        new RegionDeleter(this, regionName)
                );
    }

    /**
     * Downloads a region.
     *
     * @param regionData The region data to download.
     */
    public void downloadRegion(RegionData regionData) {
        OfflineMapManager.getInstance(this)
                .createOfflineRegion(
                        regionData,
                        new RegionDownloader(this)
                );
    }

    /**
     * This method uploads the downloaded regions and adds a new card to the view.
     *
     * @param regionName The name of the region to be uploaded.
     */
    private void uploadRegionsDownloaded(String regionName) {
        CardView cardView = offlineCardView.createNewCardMapDownloaded(regionName, v -> showPopupMenu(v, regionName));
        vBoxDownloadedMaps.addView(cardView);
    }

    /**
     * This method shows the popup menu and handles the selected menu item.
     *
     * @param item     The selected menu item.
     * @param nameItem The name of the item.
     * @return {@code true} if the menu item is handled, {@code false} otherwise.
     */
    public boolean showPopupMenu(MenuItem item, String nameItem) {
        switch (item.getItemId()) {
            case RENAME:
                renameDownloadedMap();
                return true;
            case NAVIGATE_TO:
                navigateToDownloadedMap(nameItem);
                return true;
            case DELETE:
                deleteRegion(nameItem);
                return true;
            default:
                return false;
        }
    }

    /**
     * This method updates the view by removing all views and adding the downloaded regions.
     */
    @Override
    public void update() {
        vBoxDownloadedMaps.removeAllViews();
        OfflineMapManager.getInstance(this)
                .getOfflineRegions()
                .forEach((key, value) -> uploadRegionsDownloaded(key));
    }

}
