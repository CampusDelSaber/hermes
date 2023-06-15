package com.isc.hermes;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.isc.hermes.controller.SearcherController;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;
import com.isc.hermes.model.Searcher;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
    private MapboxMap mapboxMap;
    private Button chooseCityButton;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;

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
        mapDisplay = new MapDisplay(this, mapView, new MapConfigure());
        mapDisplay.onCreate(savedInstanceState);
        addMapboxSearcher();
        mapStyleListener();
        initCurrentLocationController();
        mapView.getMapAsync(this);
    }

    /**
     * Method to add the searcher to the main scene above the map
     */
    private void addMapboxSearcher() {
        Searcher searcher = new Searcher();
        SearcherController searcherController = new SearcherController(searcher,
                findViewById(R.id.searchResults), findViewById(R.id.searchView));
        searcherController.runSearcher();
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
     * Enables or disables map scroll gestures.
     *
     * @param enabled Boolean indicating whether to enable map scroll gestures.
     */
    private void setMapScrollGesturesEnabled(boolean enabled) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.getUiSettings().setScrollGesturesEnabled(enabled);
            }
        });
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
        currentLocationController = new CurrentLocationController(this, mapDisplay);
        currentLocationController.initLocation();
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
     * Method for initializing the MapDisplay object instance.
     */
    private void initMapDisplay() {
        mapDisplay = new MapDisplay(this, mapView, new MapConfigure());
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                this);
        AuthenticationServices authenticationServices = AuthenticationServices.getAuthentication(
                sharedPreferences.getInt("cuenta", 0));
        if (authenticationServices != null)
            SignUpActivityView.authenticator = AuthenticationFactory.createAuthentication(
                    authenticationServices);

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

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initTextViews();
                initButtons();
            }
        });
    }

    private void initTextViews() {
        latEditText = findViewById(R.id.geocode_latitude_editText);
        longEditText = findViewById(R.id.geocode_longitude_editText);
        searchInput = findViewById(R.id.searchInput);
    }

    private void initButtons() {
        Button startGeocodeButton = findViewById(R.id.start_geocode_button);
        chooseCityButton = findViewById(R.id.searchButton);

        startGeocodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure the EditTexts aren't empty
                if (TextUtils.isEmpty(latEditText.getText().toString())) {
                    latEditText.setError(getString(R.string.fill_in_a_value));
                } else if (TextUtils.isEmpty(longEditText.getText().toString())) {
                    longEditText.setError(getString(R.string.fill_in_a_value));
                } else {
                    if (latCoordinateIsValid(Double.valueOf(latEditText.getText().toString()))
                            && longCoordinateIsValid(Double.valueOf(longEditText.getText().toString()))) {
                        // Make a geocoding search with the values inputted into the EditTexts
                        makeGeocodeSearch(new LatLng(Double.valueOf(latEditText.getText().toString()),
                                Double.valueOf(longEditText.getText().toString())));
                    } else {
                        Toast.makeText(MainActivity.this, R.string.make_valid_lat, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        chooseCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityListMenu();
            }
        });
    }

    private boolean latCoordinateIsValid(double value) {
        return value >= -90 && value <= 90;
    }

    private boolean longCoordinateIsValid(double value) {
        return value >= -180 && value <= 180;
    }

    private void setCoordinateEditTexts(LatLng latLng) {
        latEditText.setText(String.valueOf(latLng.getLatitude()));
        longEditText.setText(String.valueOf(latLng.getLongitude()));
    }

    private void showCityListMenu() {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(searchInput.getText().toString())
                .build();


        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CarmenFeature> results = response.body().features();
                    Log.d(TAG, "onResponse: " + searchInput.getText().toString());
                    if (results.size() > 0) {
                        // Log the first results Point.

                        Log.d(TAG, "onResponse: " +  results.get(0).toString());
                        Point point = results.get(0).center();
                        LatLng cityLatLng = new LatLng(point.latitude(), point.longitude());
                        setCoordinateEditTexts(cityLatLng);
                        animateCameraToNewPosition(cityLatLng);
                        makeGeocodeSearch(cityLatLng);
                        Log.d(TAG, "onResponse: " + point.toString());
                    } else {
                        // No results for your request were found.
                        Log.d(TAG, "onResponse: No result found");
                    }
                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    private void makeGeocodeSearch(final LatLng latLng) {
        try {
            // Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {
                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {

                            // Get the first Feature from the successful geocoding response
                            CarmenFeature feature = results.get(0);
                            animateCameraToNewPosition(latLng);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.no_results,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Timber.e("Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(11)
                        .build()), 1500);
    }
}