package com.isc.hermes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;

import com.google.android.material.navigation.NavigationView;
import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;

import com.isc.hermes.controller.FilterController;
import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.model.User;

import android.widget.SearchView;
import android.widget.TextView;

import com.isc.hermes.controller.GenerateRandomIncidentController;
import com.isc.hermes.model.Utils.MapPolyline;

import com.isc.hermes.utils.MapClickEventsManager;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.SharedSearcherPreferencesManager;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    public static Context context;
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
    private ActivityResultLauncher<Intent> launcher;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


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
        mapDisplay = MapDisplay.getInstance(this, mapView, new MapConfigure());
        mapDisplay.onCreate(savedInstanceState);
        addMapboxSearcher();
        initCurrentLocationController();
        mapView.getMapAsync(this);
        searchView = findViewById(R.id.searchView);
        changeSearchView();
        addIncidentGeneratorButton();
        MarkerManager.getInstance(this).removeSavedMarker();
        launcher = createActivityResult();
        testPolyline(); // this is a test method that will be removed once the functionality has been verified.
        initializeBurgerButtonToolBar();
        initializeFunctionalityOfTheBurgerButton();
        setTheUserInformationInTheDropMenu();
    }

    public void testPolyline() { // this is a test method that will be removed once the functionality has been verified.
        Map<String, String> r = new HashMap<>();
        List<String> routes = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        r.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        r.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        r.put("Route C", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.159019, -17.398311],[-66.154399, -17.397043],[-66.151315, -17.398656],[-66.147585, -17.400585],[-66.142978, -17.401595]]}}");

        String jsonA = r.get("Route A");
        String jsonB = r.get("Route B");
        String jsonC = r.get("Route C");

        routes.add(jsonA);
        routes.add(jsonB);
        routes.add(jsonC);

        colors.add(0xFF2867DC);
        colors.add(0XFFC5D9FD);
        colors.add(0XFFC5D9FD);

        MapPolyline mapPolyline = new MapPolyline(mapView);
        mapPolyline.displaySavedCoordinates(routes, colors);
    }

    /**
     * This method initialize the drop down menu
     */
    private void initializeBurgerButtonToolBar(){
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.navigationView = findViewById(R.id.dropdown_menu);
        this.toolbar = findViewById(R.id.dropdown_menu_toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * This method initialize the functionalities of the dropdown menu
     */
    private void initializeFunctionalityOfTheBurgerButton(){
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,
                toolbar, R.string.close, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger_button);
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
        MapClickEventsManager.getInstance().setMapboxMap(mapboxMap);
        MapClickEventsManager.getInstance().setMapClickConfiguration(new MapWayPointController(mapboxMap,this));
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
     * Opens the styles menu by toggling its visibility.
     *
     * @param view The view that triggered the method.
     */
    public void openStylesMenu(View view) {
        LinearLayout styleOptionsWindow = findViewById(R.id.styleOptionsWindow);
        styleOptionsWindow.setVisibility(View.VISIBLE);
        setMapScrollGesturesEnabled(true);
    }

    /**
     * This method set the information of the user in the header of the drop down menu
     */
    private void setTheUserInformationInTheDropMenu(){
        TextView userNameText = navigationView.getHeaderView(0)
                .findViewById(R.id.userNameText);
        userNameText.setText(userRegistered.getUserName());
        TextView userEmailText = navigationView.getHeaderView(0)
                .findViewById(R.id.userEmailText);
        userEmailText.setText(userRegistered.getEmail());
        ImageView userImage = navigationView.getHeaderView(0)
                .findViewById(R.id.userAccountImage);
        userImage.setImageURI(Uri.parse(userRegistered.getPathImageUser()));


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
                            mapDisplay.animateCameraPosition(b.getParcelable("center"), b.getDouble("zoom"));
                            openSideMenu(null);
                        }
                    }

                });
    }

    /**
     * This method analyzes the option that the user wants to perform and activates it.
     *
     * @param item The selected item
     * @return a boolean if all is correct
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                logOut(new View(context));
                return true;
            case R.id.mapStyle:
                openStylesMenu(new View(context));
                return true;
            case R.id.offlineMaps:
                goOfflineMaps(new View(context));
                return true;
            case R.id.userAccount:
                goToAccountInformation(new View(context));
                return true;
            case R.id.thirdDimensionMode:
                //TODO: Implement the third dimension mode;
                return true;
            case R.id.changeDisplayMode:
                //TODO: Implement the change dimension mode;
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}