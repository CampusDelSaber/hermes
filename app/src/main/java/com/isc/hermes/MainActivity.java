package com.isc.hermes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapDisplay mapDisplay;
    private String mapStyle = "default";
    private CurrentLocationController currentLocationController;

    private boolean visibilityMenu = false, isMapClickable = true;

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
        initMapDisplay();
        mapDisplay.onCreate(savedInstanceState);
        mapStyleListener();
        initCurrentLocationController();
    }

    /**
     *This function helps to give functionality to the side menu, so that it can be visible and hidden, when necessary.
     *
     * @param view Helps build the view.
     */
    public void openSideMenu(View view) {
        LinearLayout lateralMenu = findViewById(R.id.lateralMenu);
        if (!visibilityMenu) {
            lateralMenu.setVisibility(View.VISIBLE);
            visibilityMenu = true;
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    mapboxMap.getUiSettings().setScrollGesturesEnabled(false);
                    isMapClickable = false;
                }
            });
        } else {
            lateralMenu.setVisibility(View.GONE);
            visibilityMenu = false;
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    mapboxMap.getUiSettings().setScrollGesturesEnabled(true);
                    isMapClickable = true;
                }
            });
        }
    }


    /**
     * This method will init the current location controller to get the real time user location
     */
    private void initCurrentLocationController(){
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
        mapDisplay = new MapDisplay(mapView, new MapConfigure());
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
    }

    /**
     * Method for pausing the MapView object instance.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapDisplay.onPause();
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
     * Method for adding maps styles listener
     */
    private void mapStyleListener(){
        Button styleButton = findViewById(R.id.btn_change_style);
        styleButton.setOnClickListener(styleMap -> {
            if (mapStyle.equals("default")) {
                mapStyle = "satellite";
            } else if (mapStyle.equals("satellite")) {
                mapStyle = "dark";
            } else {
                mapStyle = "default";
            }
            mapDisplay.setMapStyle(mapStyle);
        });
    }
}