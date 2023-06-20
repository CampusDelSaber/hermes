package com.isc.hermes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;

import com.isc.hermes.controller.FilterController;
import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.model.User;

import android.widget.SearchView;

import com.isc.hermes.controller.GenerateRandomIncidentController;
import com.isc.hermes.model.Utils.MapPolyline;

import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.SharedSearcherPreferencesManager;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapDisplay mapDisplay;
    private String mapStyle;
    private CurrentLocationController currentLocationController;
    private User userRegistered;
    private boolean visibilityMenu = false;
    private SearchView searchView;
    private SharedSearcherPreferencesManager sharedSearcherPreferencesManager;
    private MarkerManager markerManager;
    private boolean isStyleOptionsVisible = false;
    private static final int REQUEST_MAP_CODE = 177;

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
        this.mapStyle = "Default";
        mapDisplay = MapDisplay.getInstance(this, mapView, new MapConfigure());
        mapDisplay.onCreate(savedInstanceState);
        addMapboxSearcher();
        getUserInformation();
        initCurrentLocationController();
        mapView.getMapAsync(this);
        searchView = findViewById(R.id.searchView);
        changeSearchView();
        addIncidentGeneratorButton();
        MarkerManager.getInstance(this).removeSavedMarker();

        testPolyline(); // this is a test method that will be removed once the functionality has been verified.
    }

    public void testPolyline() { // this is a test method that will be removed once the functionality has been verified.
        Map<String, String> r = new HashMap<>();

        r.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        r.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        r.put("Route C", "{}");

        String jsonA = r.get("Route A");
        String jsonB = r.get("Route B");
        String jsonC = r.get("Route C");

        MapPolyline mapPolyline = new MapPolyline(mapView);
        mapPolyline.displaySavedCoordinates(jsonB, Color.RED);
        //mapPolyline.displaySavedCoordinates(jsonA, Color.BLUE);

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
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                filterController.initComponents();
            }
        });
    }


    /**
     * Sends a User object to another activity using an Intent.
     *
     * @param user The User object to be sent to the other activity.
     */
    private void sendUserBetweenActivities(User user) {
        Intent intent = new Intent(this, AccountInformation.class);
        intent.putExtra("userObtained", user);
        startActivity(intent);
    }

    /**
     * This method is used to display a view where you can see the information about your account.
     *
     * @param view Helps build the view
     */
    public void goToAccountInformation(View view) {
        sendUserBetweenActivities(userRegistered);
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
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        String nameServiceUsed = sharedPref.getString(getString(R.string.save_authentication_state), "default");
        if (!nameServiceUsed.equals("default")) {
            SignUpActivityView.authenticator = AuthenticationFactory.createAuthentication(AuthenticationServices.valueOf(nameServiceUsed));
        }

        addMarkers();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapDisplay.onPause();
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
     * Retrieves the user information passed through the intent.
     * Gets the Parcelable "userObtained" extra from the intent and assigns it to the userRegistered variable.
     */
    private void getUserInformation() {
        Intent intent = getIntent();
        userRegistered = intent.getParcelableExtra("userObtained");
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
        } else {
            styleOptionsWindow.setVisibility(View.GONE);
        }
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
        mapDisplay.setMapStyle(mapStyle);
        isStyleOptionsVisible = false;
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

    /**
     * This method used for open a new activity, offline settings.
     *
     * @param view view
     */
    public void goOfflineMaps(View view) {
        Intent intent = new Intent(MainActivity.this, OfflineMapsActivity.class);
        intent.putExtra("lat", mapDisplay.getMapboxMap().getCameraPosition().target.getLatitude());
        intent.putExtra("long", mapDisplay.getMapboxMap().getCameraPosition().target.getLongitude());
        intent.putExtra("zoom", mapDisplay.getMapboxMap().getCameraPosition().zoom);
        startActivityForResult(intent, REQUEST_MAP_CODE);
    }

    /**
     * This Callback method is called when an activity launched with startActivityForResult() returns a result.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAP_CODE && resultCode == RESULT_OK && Objects.requireNonNull(data).getExtras() != null) {
            Bundle b = data.getExtras();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(b.getParcelable("center"))
                    .zoom(b.getDouble("zoom"))
                    .build();
            mapDisplay.getMapboxMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500);
            openSideMenu(null);
        }
    }
}