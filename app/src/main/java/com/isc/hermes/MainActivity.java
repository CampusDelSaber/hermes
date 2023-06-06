package com.isc.hermes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapDisplay mapDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapbox();
        setContentView(R.layout.activity_main);
        initMapView();
        initMapDisplay();
        mapDisplay.onCreate(savedInstanceState);
    }

    private void initMapbox() {
        Mapbox.getInstance(this, getString(R.string.access_token));
    }

    private void initMapView() {
        mapView = findViewById(R.id.mapView);
    }

    private void initMapDisplay() {
        mapDisplay = new MapDisplay(mapView, new MapConfigure());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapDisplay.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapDisplay.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapDisplay.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapDisplay.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapDisplay.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapDisplay.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapDisplay.onSaveInstanceState(outState);
    }
}