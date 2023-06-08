package com.isc.hermes.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isc.hermes.R;
import com.isc.hermes.utils.MapClickEventListener;
import com.isc.hermes.utils.MapClickEventsManager;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.utils.MapLongClickEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;
    private final Context context;
    MapClickEventListener mapClickEventListener;
    MapboxMap mapboxMap;
    MapLongClickEventListener mapLongClickEventListener;
    /**
     * Constructor to create a MapDisplay object.
     *
     * @param mapView the MapView object to display the map
     * @param mapConfigure the MapConfigure object to configure the map
     */
    public MapDisplay(Context context, MapView mapView, MapConfigure mapConfigure) {

        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
        this.context = context;
        mapConfigure.setContext(context);
        initFabButton();
    }

    public void initFabButton() {
        FloatingActionButton fabButton = ((AppCompatActivity) context).findViewById(R.id.fab_button);
        fabButton.setOnClickListener(v -> Toast.makeText(context, "To report incident: Do click on map\n To report traffic: click to mark start point and long click to mark end point", Toast.LENGTH_SHORT).show());
    }


    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapConfigure::configure);
    }



    /**
     * Method for starting the MapView object instance.
     */
    public void onStart() {
        mapView.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    public void onResume() {
        mapView.onResume();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    public void onPause() {
        mapView.onPause();
    }

    /**
     * Method for stopping the MapView object instance.
     */
    public void onStop() {
        mapView.onStop();
    }

    /**
     * Method for handling low memory situations.
     */
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    public void onDestroy() {
        mapView.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance to save
     */
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }
}
