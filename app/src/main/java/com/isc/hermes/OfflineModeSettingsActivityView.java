package com.isc.hermes;

import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LATITUDE;
import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LONGITUDE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.isc.hermes.model.RegionData;
import com.isc.hermes.utils.offline.OfflineCardView;
import com.isc.hermes.utils.offline.OfflineMapManager;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.util.HashMap;
import java.util.Map;
import timber.log.Timber;

/**
 * This class represents the Offline Mode Settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity {

    private LinearLayout vBoxDownloadedMaps;
    private OfflineMapManager offlineMapManager;
    public static Map<String, OfflineRegion> offlineRegionsNames;
    private Map<String, CardView> cardViews;
    private OfflineCardView offlineCardView;
    private static final int REQUEST_CODE_OFFLINE = 231;

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
        offlineMapManager = new OfflineMapManager(this);
        offlineCardView = new OfflineCardView(this);
        vBoxDownloadedMaps = findViewById(R.id.vBoxMapsDownloaded);
    }



    /**
     * Navigates to the selected downloaded map.
     */
    protected void navigateToDownloadedMap(String regionName) {
        OfflineRegion offlineRegion = offlineRegionsNames.get(regionName);
        if (offlineRegion == null) return;
        LatLngBounds bounds = offlineRegion.getDefinition().getBounds();
        double regionZoom = offlineRegion.getDefinition().getMinZoom();
        Intent intent = new Intent();
        intent.putExtra("center", bounds.getCenter());
        intent.putExtra("zoom", regionZoom);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Renames the selected downloaded map.
     */
    protected void renameDownloadedMap(){

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
        intent.putExtra("zoom", bundle.getDouble("zoom"));

        startActivityForResult(intent, REQUEST_CODE_OFFLINE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OFFLINE && resultCode == RESULT_OK) {
            RegionData regionData = data.getExtras().getParcelable("REGION_DATA");
            downloadRegion(regionData);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadRegions();
    }

    /**
     * Loads the available regions.
     */
    private void loadRegions() {
        cardViews = new HashMap<>();
        offlineMapManager.listRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(getApplicationContext(), "THERE IS NO REGIONS DOWNLOADED YET", Toast.LENGTH_SHORT).show();
                    return;
                }

                offlineRegionsNames = new HashMap<>();
                vBoxDownloadedMaps.removeAllViews();
                String regionName;
                for (OfflineRegion offlineRegion : offlineRegions) {
                    regionName = offlineMapManager.getRegionName(offlineRegion);
                    offlineRegionsNames.put(regionName, offlineRegion);
                    uploadRegionsDownloaded(regionName,"100MB");
                }
            }

            @Override
            public void onError(String error) {
                Timber.e("Error: %s", error);
            }
        });
    }

    /**
     * Deletes a region.
     *
     * @param regionName The name of the region to delete.
     */
    private void deleteRegion(String regionName) {
        OfflineRegion offlineRegion = offlineRegionsNames.get(regionName);
        CardView cardView = cardViews.get(regionName);
        if (offlineRegion == null) return;
        offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
            @Override
            public void onDelete() {
                vBoxDownloadedMaps.removeView(cardView);
                Toast.makeText(getApplicationContext(), "THE REGION HAS BEEN DELETED SUCCESSFULLY",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Timber.e("Error: %s", error);
            }
        });
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
     * Downloads a region.
     *
     * @param regionData The region data to download.
     */
    public void downloadRegion(RegionData regionData) {
        Thread downloadThread = new Thread(() -> {
            offlineMapManager.createOfflineRegion(regionData.getRegionName(), getDefinition(regionData),
                    new OfflineManager.CreateOfflineRegionCallback() {
                        @Override
                        public void onCreate(OfflineRegion offlineRegion) {
                            launchDownload(offlineRegion);
                        }

                        @Override
                        public void onError(String error) {
                            Timber.e("Error: %s", error);
                        }
                    });
        });
        downloadThread.start();
    }

    /**
     * Launches the download of a region.
     *
     * @param offlineRegion The region to download.
     */
    private void launchDownload(OfflineRegion offlineRegion) {
        Toast.makeText(getApplicationContext(), "THE DOWNLOAD HAS STARTED",
                Toast.LENGTH_SHORT).show();
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                if (status.isComplete()) {
                    Toast.makeText(getApplicationContext(), "REGION DOWNLOADED",
                            Toast.LENGTH_SHORT).show();
                    loadRegions();
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // ...
                }

                Timber.d("%s/%s resources; %s bytes downloaded.",
                        String.valueOf(status.getCompletedResourceCount()),
                        String.valueOf(status.getRequiredResourceCount()),
                        String.valueOf(status.getCompletedResourceSize()));
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Timber.e("Mapbox tile count limit exceeded: %s", limit);
            }
        });

        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }
    private void uploadRegionsDownloaded(String regionName, String size){
        CardView cardView = offlineCardView.createNewCardMapDownloaded(regionName, size, v -> showPopupMenu(v,regionName));
        cardViews.put(regionName,cardView);
        vBoxDownloadedMaps.addView(cardView);
    }

    public boolean showPopupMenu(MenuItem item,String nameItem) {
        switch (item.getItemId()) {
            case R.id.rename:
                renameDownloadedMap();
                return true;
            case R.id.navigateTo:
                navigateToDownloadedMap(nameItem);
                return true;
            case R.id.delete:
                deleteRegion(nameItem);
                return true;
            default:
                return false;
        }
    }

}
