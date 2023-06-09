package com.isc.hermes;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.isc.hermes.controller.SearcherController;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapDisplay mapDisplay;
    private LinearLayout searchResultsLayout;

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
        searchResultsLayout = findViewById(R.id.searchResultsLayout);
        addMapboxSearcher();
    }

    private void addMapboxSearcher() {

        // Create a SearchView widget and set it up
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Searcher searcher = new Searcher();
                SearcherController searcherController = new SearcherController(searcher,
                        findViewById(R.id.searchResults));
                searcherController.manageResultsContainerBehaviour();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes or suggestions if needed
                return false;
            }
        });
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
}