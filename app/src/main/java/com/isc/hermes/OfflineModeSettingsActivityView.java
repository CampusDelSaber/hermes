package com.isc.hermes;

import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LATITUDE;
import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LONGITUDE;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.isc.hermes.model.RegionData;
import com.isc.hermes.utils.OfflineMapManager;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * This class represents the Offline Mode Settings UI.
 */
public class OfflineModeSettingsActivityView extends AppCompatActivity {

    private LinearLayout vBoxDownloadedMaps;
    private OfflineMapManager offlineMapManager;
    private Map<String, OfflineRegion> offlineRegionsNames;
    private Map<String, CardView> cardViews;
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
        vBoxDownloadedMaps = findViewById(R.id.vBoxMapsDownloaded);
    }

    /**
     * Creates a new card view for a downloaded map.
     *
     * @param name The name of the downloaded region.
     * @param size The size of the downloaded region.
     */
    protected void createNewCardMapDownloaded(String name, String size) {
        LinearLayout textViewsVertical = createVerticalLinearLayout();
        textViewsVertical.setPadding(100, 0, 100, 0);

        TextView nameTextView = createTextView(name, 18, Typeface.BOLD);


        textViewsVertical.addView(nameTextView);

        CardView cardView = joinComponents(createCheckImageView(), textViewsVertical, createButtonPopup(name));
        cardViews.put(name, cardView);
        showCard(cardView);
    }

    /**
     * Creates a vertical LinearLayout.
     *
     * @return The created vertical LinearLayout.
     */
    protected LinearLayout createVerticalLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.6f
        ));
        return linearLayout;
    }

    /**
     * Creates a TextView with the specified text, text size, and text style.
     *
     * @param text      The text to be displayed.
     * @param textSize  The size of the text.
     * @param textStyle The style of the text.
     * @return The created TextView.
     */
    protected TextView createTextView(String text, int textSize, int textStyle) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTypeface(null, textStyle);
        textView.setPadding(0, 10, 0, 10);
        return textView;
    }

    /**
     * Joins the components (check image, text views, and popup button) to create a CardView.
     *
     * @param checkImage    The check image.
     * @param vBoxTextViews A vertical LinearLayout containing the text views.
     * @param popupMenu     The popup button.
     * @return The created CardView.
     */
    protected CardView joinComponents(CircleImageView checkImage, LinearLayout vBoxTextViews, CircleImageView popupMenu) {
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        cardView.addView(container);

        cardView.setPadding(10, 10, 10, 10);

        container.addView(checkImage);
        container.addView(vBoxTextViews);
        container.addView(popupMenu);

        return cardView;
    }

    /**
     * Displays the created CardView.
     *
     * @param cardView The CardView to be displayed.
     */
    protected void showCard(CardView cardView) {
        vBoxDownloadedMaps.addView(cardView);
    }

    /**
     * Creates a popup button as a CircleImageView.
     *
     * @return The created popup button.
     */
    protected CircleImageView createButtonPopup(String nameItem) {
        CircleImageView popupButton = new CircleImageView(this);
        popupButton.setImageResource(R.drawable.img_tree_points_vertical);
        popupButton.setPadding(10, 10, 30, 0);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        popupButton.setLayoutParams(params);

        popupButton.setOnClickListener(v -> showPopupMenu(v, nameItem));
        return popupButton;
    }

    /**
     * Creates a check image as a CircleImageView.
     *
     * @return The created check image.
     */
    protected CircleImageView createCheckImageView() {
        CircleImageView checkImageView = new CircleImageView(this);
        checkImageView.setImageResource(R.drawable.img_check);
        checkImageView.setPadding(30, 10, 10, 0);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        checkImageView.setLayoutParams(params);

        return checkImageView;
    }


    /**
     * Shows the popup menu.
     *
     * @param view The view that triggered the popup menu.
     */
    public void showPopupMenu(View view, String nameItem) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
        });
        popupMenu.inflate(R.menu.downloaded_maps_options);
        popupMenu.show();
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
    protected void renameDownloadedMap() {
        System.out.println("rename");
    }


    /**
     * Handles the click event for the "Download New Map" button.
     *
     * @param view The view that triggered the click event.
     */
    public void downloadNewMap(View view) {
        // Code to download a new map
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
            System.out.println("DATA RECEIVED");
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
                    createNewCardMapDownloaded(regionName, "120MB");
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
                    System.out.println("REGION DOWNLOADED");
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
                System.out.println("The download was successful");
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


}
