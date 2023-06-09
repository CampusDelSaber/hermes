package com.isc.hermes.utils;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isc.hermes.R;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

/**
 * Class to configure the event of do click on a map
 */
public class MapClickEventListener implements MapboxMap.OnMapClickListener {

    private LatLng latLngPoint;
    private final MapboxMap mapboxMap;
    private final Context context;

    FloatingActionButton incident2Button;
    FloatingActionButton incident3Button;

    public MapClickEventListener(MapboxMap mapboxMap,Context context ) {
        this.mapboxMap = mapboxMap;
        this.context = context;

        incident2Button = ((AppCompatActivity) context).findViewById(R.id.incident2_button);
        incident3Button = ((AppCompatActivity) context).findViewById(R.id.incident3_button);

        // Ocultar los botones de incidentes al principio

        incident2Button.setVisibility(View.GONE);
        incident3Button.setVisibility(View.GONE);

        mapboxMap.addOnMapClickListener(this);
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        latLngPoint = point;

        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(latLngPoint);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty() && features.get(0).geometry().type().equals("MultiLineString")) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLngPoint);
            mapboxMap.addMarker(markerOptions);
            incident2Button.setVisibility(View.VISIBLE);
            incident3Button.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(context, "Haz clic en una calle o avenida", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public LatLng getLatLngPoint() {return latLngPoint;}
}
