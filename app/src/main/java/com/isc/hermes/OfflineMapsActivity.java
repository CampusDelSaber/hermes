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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.isc.hermes.controller.PopUp.DialogListener;
import com.isc.hermes.controller.PopUp.TextInputPopup;
import com.isc.hermes.controller.offline.OfflineDataRepository;
import com.isc.hermes.model.RegionData;
import com.isc.hermes.controller.offline.CardViewHandler;
import com.isc.hermes.view.OfflineCardView;
import com.isc.hermes.utils.offline.MapboxOfflineManager;
import com.isc.hermes.controller.offline.RegionDeleter;
import com.isc.hermes.controller.offline.RegionDownloader;
import com.isc.hermes.controller.offline.RegionLoader;
import com.isc.hermes.controller.offline.RegionObserver;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import timber.log.Timber;


/**
 * This class represents the Offline Mode Settings UI.
 */
public class OfflineMapsActivity extends AppCompatActivity implements RegionObserver, DialogListener {
    private static final int RENAME = R.id.rename, NAVIGATE_TO = R.id.navigateTo, DELETE = R.id.delete;
    private LinearLayout vBoxDownloadedMaps;
    private OfflineCardView offlineCardView;
    private ActivityResultLauncher<Intent> launcher;
    private TextInputPopup textInputPopup;
    private String selectedNameDownloadedRegion;

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
        launcher = createActivityResult();
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

        launcher.launch(intent);
    }

    /**
     * This method creates an {@link ActivityResultLauncher} for starting an activity and handling the result.
     *
     * @return The created {@link ActivityResultLauncher} object.
     */
    private ActivityResultLauncher<Intent> createActivityResult() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        RegionData regionData = OfflineDataRepository.getInstance().getRegionData(OfflineDataRepository.DATA_TRANSACTION);
                        if (regionData != null)
                            downloadRegion(regionData);
                    }

                });
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
            OfflineRegion offlineRegion = MapboxOfflineManager.getInstance(this).getOfflineRegion(regionName);
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
     * Show Popup for Renames the selected downloaded map.
     */
    protected void displayPopupInputNewName() {
         textInputPopup = new TextInputPopup(this, this);
         textInputPopup.showPopup();
    }

    /**
     * Loads the available regions.
     *
     */
    public void loadRegions() {
        MapboxOfflineManager.getInstance(this).listRegions(new RegionLoader(this));
    }

    /**
     * Deletes a region.
     *
     * @param regionName The name of the region to delete.
     */
    private void deleteRegion(String regionName) {
        MapboxOfflineManager.getInstance(this)
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
        MapboxOfflineManager.getInstance(this)
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
                selectedNameDownloadedRegion = nameItem;
                displayPopupInputNewName();
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
        MapboxOfflineManager.getInstance(this)
                .getOfflineRegions()
                .forEach((key, value) -> uploadRegionsDownloaded(key));
    }

    /**
     * This method the actions that the popup will take.
     *
     * @param text The text received from the dialog.
     */
    @Override
    public void dialogClosed(String text) {
        if(MapboxOfflineManager.getInstance(this).getOfflineRegions().containsKey(text)){
            textInputPopup.setErrorMessage("That name already exists");
        }else{
            textInputPopup.closePopup();
            uploadNameRegion(selectedNameDownloadedRegion,text);
            update();
        }
    }

    /**
     * This method changes the name of the downloaded map.
     *
     * @param previousName the current name of the selected map
     * @param newName the new name for the selected map
     */
    private void uploadNameRegion(String previousName, String newName){
        OfflineRegion targetRegion = MapboxOfflineManager.getInstance(this).getOfflineRegion(previousName);
        byte[] metadata = targetRegion.getMetadata();

        try {
            JSONObject jsonMetadata = new JSONObject(new String(metadata, StandardCharsets.UTF_8));
            jsonMetadata.put("name", newName);
            byte[] newMetadata = jsonMetadata.toString().getBytes(StandardCharsets.UTF_8);
            targetRegion.updateMetadata(newMetadata,new OfflineRegion.OfflineRegionUpdateMetadataCallback() {
                @Override
                public void onUpdate(byte[] updatedMetadata) {
                    Timber.i("Map name has been updated");
                }

                @Override
                public void onError(String error) {
                    Timber.i("Error:%s", error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.i("An error occurred while updating the map");
        }
    }
}
