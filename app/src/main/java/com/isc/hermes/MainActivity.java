package com.isc.hermes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;

import android.widget.LinearLayout;
import com.isc.hermes.controller.FilterController;
import com.isc.hermes.controller.CurrentLocationController;
import android.widget.SearchView;
import com.isc.hermes.controller.GenerateRandomIncidentController;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.SharedSearcherPreferencesManager;
import com.isc.hermes.view.MapDisplay;
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
    private MapView mapView;
    private MapDisplay mapDisplay;
    private String mapStyle = "default";
    private CurrentLocationController currentLocationController;
    private boolean visibilityMenu = false;
    private SearchView searchView;
    private SharedSearcherPreferencesManager sharedSearcherPreferencesManager;
    private MarkerManager markerManager;

    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapbox();
        setContentView(R.layout.activity_main);
        initMapView();
        mapDisplay = MapDisplay.getInstance(this, mapView, new MapConfigure());
        mapDisplay.onCreate(savedInstanceState);
        mapStyleListener();
        initCurrentLocationController();
        mapView.getMapAsync(this);
        searchView = findViewById(R.id.searchView);
        changeSearchView();
        addIncidentGeneratorButton();
        MarkerManager.getInstance(this).removeSavedMarker();
    }

    /**
     * Method to add the searcher to the main scene above the map
     */
    private void addMapboxSearcher() {
        sharedSearcherPreferencesManager = new SharedSearcherPreferencesManager(this);
        markerManager = MarkerManager.getInstance(this);
    }

    /**
     * Called when the map is ready to be used.
     */
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        FilterController filterController = new FilterController(mapboxMap, this);
        mapboxMap.setStyle(new Style.Builder(), style -> filterController.initComponents());
    }


    /**
     * This method is used to display a view where you can see the information about your account.
     *
     * @param view Helps build the view
     */
    public void showAccount(View view) {
        System.out.println("Your account information will be displayed");
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
        SignUpActivityView.authenticator.signOut(this);
        Intent intent = new Intent(MainActivity.this, SignUpActivityView.class);
        startActivity(intent);
    }

    /**
     * This method will init the current location controller to get the real time user location
     */
    private void initCurrentLocationController() {
        currentLocationController = CurrentLocationController.getControllerInstance(this, mapDisplay);
        currentLocationController.initLocationButton();
    }

    /**
     * This method adds the button for incident generation.
     */
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
        mapDisplay.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapDisplay.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AuthenticationServices authenticationServices = AuthenticationServices.getAuthentication(sharedPreferences.getInt("cuenta", 0));
        if (authenticationServices != null)
            SignUpActivityView.authenticator = AuthenticationFactory.createAuthentication(authenticationServices);

        addMarkers();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapDisplay.onPause();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor = datos.edit();
        miEditor.putInt("cuenta", SignUpActivityView.authenticator.getServiceType().getID());
        miEditor.apply();
    }

    /**
     * Method for stopping the MapView object instance.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mapDisplay.onStop();
    }

    /**
     * Method for handling low memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapDisplay.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapDisplay.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapDisplay.onSaveInstanceState(outState);
    }

    /**
     * Method for adding maps styles.xml listener
     */
    private void mapStyleListener() {
        ImageButton styleButton = findViewById(R.id.btn_change_style);
        styleButton.setOnClickListener(styleMap -> {
            if (mapStyle.equals("default")) mapStyle = "satellite";
            else if (mapStyle.equals("satellite")) mapStyle = "dark";
            else mapStyle = "default";
            mapDisplay.setMapStyle(mapStyle);
        });
    }

    /**
     * Adds markers to the map based on the shared searcher preferences.
     * The markers are added using the MarkerManager instance.
     */
    private void addMarkers() {
        markerManager.addMarkerToMap(mapView, sharedSearcherPreferencesManager.getPlaceName(),
                sharedSearcherPreferencesManager.getLatitude(),
                sharedSearcherPreferencesManager.getLongitude());
    }
}