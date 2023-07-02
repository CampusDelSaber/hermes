package com.isc.hermes;

import static com.mongodb.assertions.Assertions.assertNotNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.isc.hermes.controller.ViewIncidentsController;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Handler;
import com.isc.hermes.controller.FilterCategoriesController;
import com.isc.hermes.controller.MapPolygonController;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.view.IncidentViewNavigation;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;
import com.isc.hermes.controller.FilterController;
import com.isc.hermes.controller.CurrentLocationController;
import android.widget.TextView;
import com.isc.hermes.controller.GenerateRandomIncidentController;
import com.isc.hermes.database.AccountInfoManager;

import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.model.incidents.GeometryType;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.utils.MapManager;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.SharedSearcherPreferencesManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import timber.log.Timber;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private SharedSearcherPreferencesManager sharedSearcherPreferencesManager;
    private CurrentLocationController currentLocationController;
    private FilterCategoriesController filterCategoriesController;
    private ViewIncidentsController viewIncidentsController;
    private MarkerManager markerManager;
    private ActivityResultLauncher<Intent> launcher;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView searchView;

    private boolean visibilityMenu;

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private Toolbar toolbar;
    private MapView mapView;
    private String mapStyle;
    private ImageButton buttonClear;
    private final String resetSearchText = "Search...";

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
        incidentViewButton();
        initShowIncidentsController();
        MarkerManager.getInstance(this).removeSavedMarker();
        initFilterAdvancedView();
        launcher = createActivityResult();
        initShowIncidentsController();
        initCurrentLocationController();
        initializeBurgerButtonToolBar();
        initializeFunctionalityOfTheBurgerButton();
        try{
            setTheUserInformationInTheDropMenu();
        } catch(Exception e){
            e.printStackTrace();
        }

        Timber.plant(new Timber.DebugTree());
    }


    /**
     * Sets up the search view and clear button.
     * Initializes the search view, sets the text color to black,
     * and hides the clear button initially.
     */
    private void setupSearchView() {
        searchView = findViewById(R.id.searchView);
        searchView.setTextColor(Color.BLACK);
        buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setVisibility(View.GONE);
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
     * This method is the incident view button
     */
    private void incidentViewButton() {
        IncidentViewNavigation.getInstance(this);
    }

    /**
     * This method is used to change the search view.
     */
    private void changeSearchView() {
        addMapboxSearcher();
        searchView.setOnClickListener(v -> {
            resetMarkersAndSearch();
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
        this.finish();
    }

    /**
     * This method will init the current location controller to get the real time user location
     */
    private void initCurrentLocationController() {
        currentLocationController = CurrentLocationController.getControllerInstance(this);
        currentLocationController.initLocationButton();
        currentLocationController.initLocation();
    }

    /**
     * This method init the form with all button to show incidents from database
     */
    private void initShowIncidentsController() {
        viewIncidentsController = new ViewIncidentsController(this);
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
        }
        addMarkers();
        onActionButtonClear();
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
        styleOptionsWindow.setVisibility(View.VISIBLE);
        setMapScrollGesturesEnabled(true);
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
    }

    /**
     * This method set the information of the user in the header of the drop down menu
     */
    private void setTheUserInformationInTheDropMenu(){
        TextView userNameText = navigationView.getHeaderView(0)
                .findViewById(R.id.userNameText);
        userNameText.setText(UserRepository.getInstance().getUserContained().getUserName());
        TextView userEmailText = navigationView.getHeaderView(0)
                .findViewById(R.id.userEmailText);
        userEmailText.setText(UserRepository.getInstance().getUserContained().getEmail());
        ImageView userImage = navigationView.getHeaderView(0)
                .findViewById(R.id.userAccountImage);
        if (UserRepository.getInstance().getUserContained().getPathImageUser() != null)
            Glide.with(this).load(Uri.parse(
                    UserRepository.getInstance().getUserContained().getPathImageUser())).into(userImage);
    }

    /**
     * Adds markers to the map based on the current place preferences.
     */
    private void addMarkers() {
        WayPoint place = new WayPoint(sharedSearcherPreferencesManager.getPlaceName(),
                sharedSearcherPreferencesManager.getLatitude(),
                sharedSearcherPreferencesManager.getLongitude());
        addMarkerToMap(place);
        if (place.getPlaceName() != null){
            buttonClear.setVisibility(View.VISIBLE);
            searchView.setText(place.getPlaceName());
        } else
            searchView.setText(resetSearchText);
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

    /**
     * Sets up the action for the clear button.
     * Clears the search view, removes all markers from the map, and hides the clear button.
     */
    private void onActionButtonClear() {
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMarkersAndSearch();
            }
        });
    }

    /**
     * Resets the markers and search view.
     * This method sets the text of the search view to the specified reset search value,
     * removes all markers from the map view using the marker manager,
     * and hides the clear button.
     */
    public void resetMarkersAndSearch() {
        searchView.setText(resetSearchText);
        markerManager.removeAllMarkers(mapView);
        buttonClear.setVisibility(View.GONE);
    }

    /**
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
                drawerLayout.closeDrawer(GravityCompat.START);
                logOut(new View(context));
                return true;
            case R.id.mapStyle:
                drawerLayout.closeDrawer(GravityCompat.START);
                openStylesMenu(new View(context));
                return true;
            case R.id.offlineMaps:
                drawerLayout.closeDrawer(GravityCompat.START);
                goOfflineMaps(new View(context));
                return true;
            case R.id.userAccount:
                drawerLayout.closeDrawer(GravityCompat.START);
                goToAccountInformation(new View(context));
                return true;
            case R.id.thirdDimensionMode:
                //TODO: Implement the third dimension mode;
                return true;
            case R.id.changeDisplayMode:
                //TODO: Implement the change dimension mode;
                return true;
        }
        return true;
    }

    /**
     * Accept natural disasters and upload a corresponding incident.
     *
     * Generates a JSON for the natural disaster incident and sends it to the server.
     */
    public void acceptNaturalDisasters(){
        String JsonString = IncidentsUploader.getInstance()
                .generateJsonIncident(
                        "Natural Disaster",
                        "Natural Disaster",
                        "2023-06-22T19:40:47.955Z",
                        "2023-06-22T19:40:47.955Z" ,
                        GeometryType.POLYGON.getName(),
                        MapPolygonController.getInstance(
                                MapManager.getInstance().getMapboxMap(), context)
                                .getPolygonPoints().toString()
                );
        IncidentsUploader.getInstance().uploadIncident(JsonString);
    }
}