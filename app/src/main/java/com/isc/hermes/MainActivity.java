package com.isc.hermes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

import com.isc.hermes.controller.FilterCategoriesController;
import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;
import com.isc.hermes.controller.FilterController;
import com.isc.hermes.controller.CurrentLocationController;

import android.widget.TextView;
import com.isc.hermes.controller.GenerateRandomIncidentController;

import com.isc.hermes.utils.MapManager;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.SharedSearcherPreferencesManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static Context context;
    private MapView mapView;
    private String mapStyle;
    private CurrentLocationController currentLocationController;
    private FilterCategoriesController filterCategoriesController;
    private boolean visibilityMenu = false;
    private TextView searchView;
    private SharedSearcherPreferencesManager sharedSearcherPreferencesManager;
    private MarkerManager markerManager;
    private boolean isStyleOptionsVisible = false;
    private ActivityResultLauncher<Intent> launcher;

    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initMapbox();
        setContentView(R.layout.activity_main);
        initMapView();
        this.mapStyle = "Default";
        addMapboxSearcher();
        mapView.getMapAsync(this);
        setupSearchView();
        changeSearchView();
        addIncidentGeneratorButton();
        MarkerManager.getInstance(this).removeSavedMarker();
        initFilterAdvancedView();
        launcher = createActivityResult();
        initCurrentLocationController();
    }
    /**
     * Set up the SearchView and set the text color to black.
     */
    private void setupSearchView() {
        searchView = findViewById(R.id.searchView);
        searchView.setTextColor(Color.BLACK);
    }

    /**
     * Method to add the searcher to the main scene above the map
     */
    private void addMapboxSearcher() {
        sharedSearcherPreferencesManager = new SharedSearcherPreferencesManager(this);
        markerManager = MarkerManager.getInstance(this);
    }

    /**
     * Method to initialize the filters advanced view.
     */
    private void initFilterAdvancedView() {
        filterCategoriesController = new FilterCategoriesController(this);
        filterCategoriesController.initComponents();
    }

    /**
     * Called when the map is ready to be used.
     */
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        FilterController filterController = new FilterController(mapboxMap, this);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {filterController.initComponents();}
        });
        MapManager.getInstance().setMapboxMap(mapboxMap);
        MapManager.getInstance().setMapClickConfiguration(new MapWayPointController(mapboxMap, this));
    }

    /**
     * This method is used to display a view where you can see the information about your account.
     *
     * @param view Helps build the view
     */
    public void goToAccountInformation(View view) {
        Intent intent = new Intent(this, AccountInformation.class);
        startActivity(intent);
    }

    /**
     * This function helps to give functionality to the side menu, so that it can be visible and hidden, when necessary.
     *
     * @param view Helps build the view.
     */
    public void openSideMenu(View view) {
        LinearLayout lateralMenu = findViewById(R.id.lateralMenu);
        if (!visibilityMenu) {
            lateralMenu.setVisibility(View.VISIBLE);
            visibilityMenu = true;
            setMapScrollGesturesEnabled(false);
        } else {
            lateralMenu.setVisibility(View.GONE);
            visibilityMenu = false;
            setMapScrollGesturesEnabled(true);
        }
    }

    /**
     * This method is used to change the search view.
     */
    private void changeSearchView() {
        addMapboxSearcher();
        searchView.setOnClickListener(v -> {
            new Handler().post(() -> {
                Intent intent = new Intent(MainActivity.this, SearchViewActivity.class);
                startActivity(intent);
            });
        });
    }

    /**
     * Enables or disables map scroll gestures.
     *
     * @param enabled Boolean indicating whether to enable map scroll gestures.
     */
    private void setMapScrollGesturesEnabled(boolean enabled) {
        mapView.getMapAsync(mapboxMap -> mapboxMap.getUiSettings().setScrollGesturesEnabled(enabled));
    }

    /**
     * Logs out the current user and redirects to the login activity.
     *
     * @param view The view of the button that has been clicked.
     */
    public void logOut(View view) {
        if (SignUpActivityView.authenticator != null) {
            SignUpActivityView.authenticator.signOut(this);
        }
        Intent intent = new Intent(this, SignUpActivityView.class);
        startActivity(intent);
    }

    /**
     * This method will init the current location controller to get the real time user location
     */
    private void initCurrentLocationController() {
        currentLocationController = CurrentLocationController.getControllerInstance(this);
        currentLocationController.initLocationButton();
    }

    /**
     * This method adds the button for incident generation.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addIncidentGeneratorButton() {
        GenerateRandomIncidentController incidentController = new GenerateRandomIncidentController(this);
    }

    /**
     * Method for initializing the Mapbox object instance.
     */
    private void initMapbox() {
        Mapbox.getInstance(this, getString(R.string.access_token));
    }

    /**
     * Method for initializing the MapView object instance.
     */
    private void initMapView() {
        mapView = findViewById(R.id.mapView);
    }

    /**
     * Method for starting the MapView object instance.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        String nameServiceUsed = sharedPref.getString(getString(R.string.save_authentication_state), "default");
        if (!nameServiceUsed.equals("default")) {
            SignUpActivityView.authenticator = AuthenticationFactory.createAuthentication(AuthenticationServices.valueOf(nameServiceUsed));
        } addMarkers();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (SignUpActivityView.authenticator != null) {
            editor.putString(getString(R.string.save_authentication_state), SignUpActivityView.authenticator.getServiceType().name());
            editor.apply();
        }
    }

    /**
     * Method for stopping the MapView object instance.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Method for handling low memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Opens the styles menu by toggling its visibility.
     *
     * @param view The view that triggered the method.
     */
    public void openStylesMenu(View view) {
        LinearLayout styleOptionsWindow = findViewById(R.id.styleOptionsWindow);
        LinearLayout lateralMenu = findViewById(R.id.lateralMenu);
        isStyleOptionsVisible = !isStyleOptionsVisible;

        if (isStyleOptionsVisible) {
            lateralMenu.setVisibility(View.GONE);
            styleOptionsWindow.setVisibility(View.VISIBLE);
            setMapScrollGesturesEnabled(true);
            visibilityMenu = false;
        } else styleOptionsWindow.setVisibility(View.GONE);
    }

    /**
     * Method to change the map style.
     *
     * @param view The button's view of the style that has been clicked.
     */
    public void changeMapStyle(View view) {
        LinearLayout styleOptionsWindow = findViewById(R.id.styleOptionsWindow);
        styleOptionsWindow.setVisibility(View.GONE);

        mapStyle = ((ImageButton) view).getTag().toString();
        MapManager.getInstance().getMapboxMap().setStyle(mapStyle);
        isStyleOptionsVisible = false;
    }

    /**
     * Adds markers to the map based on the current place preferences.
     */
    private void addMarkers() {
        WayPoint place = new WayPoint(sharedSearcherPreferencesManager.getPlaceName(),
                sharedSearcherPreferencesManager.getLatitude(),
                sharedSearcherPreferencesManager.getLongitude());
        addMarkerToMap(place);
        if (place.getPlaceName() != null)
            searchView.setText(place.getPlaceName());
        else {
            String resetSearch = "Search...";
            searchView.setText(resetSearch);
        }
    }

    /**
     * Adds a marker to the map for the given WayPoint if it has a valid place name.
     *
     * @param place The WayPoint representing the place to add the marker for.
     */
    private void addMarkerToMap(WayPoint place) {
        if (place.getPlaceName() != null) {
            markerManager.addMarkerToMap(mapView, place.getPlaceName(), place.getLatitude(), place.getLongitude());
        }
    }


    /*
     * This method used for open a new activity, offline settings.
     *
     * @param view view
     */
    public void goOfflineMaps(View view) {
        Intent intent = new Intent(MainActivity.this, OfflineMapsActivity.class);
        intent.putExtra("lat", MapManager.getInstance().getMapboxMap().getCameraPosition().target.getLatitude());
        intent.putExtra("long", MapManager.getInstance().getMapboxMap().getCameraPosition().target.getLongitude());
        intent.putExtra("zoom", MapManager.getInstance().getMapboxMap().getCameraPosition().zoom);

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
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle b = data.getExtras();
                            MapManager.getInstance().animateCameraPosition(b.getParcelable("center"), b.getDouble("zoom"));
                            openSideMenu(null);
                        }
                    }
                });
    }
}