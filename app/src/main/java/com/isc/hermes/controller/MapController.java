package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to configure the event of do click on a map
 */
public class MapController implements MapboxMap.OnMapClickListener {
    private final MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;

    /**
     * This is the constructor method.
     *
     * @param mapboxMap Is the map.
     * @param context Is the context application.
     */
    public MapController(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        waypointOptionsController = new WaypointOptionsController(context, this);
        mapboxMap.addOnMapClickListener(this);
        isMarked = false;
        Animations.loadAnimations();
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        waypointOptionsController.assignPlaceName(point);
        doMarkOnMapAction(point);
        return true;
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point){
        if (isMarked){
            deleteMarks();
            if(waypointOptionsController.getWaypointOptions().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getWaypointOptions().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getWaypointOptions().setVisibility(View.GONE);
            }
            if(waypointOptionsController.getIncidentFormController().getIncidentForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getIncidentFormController().getIncidentForm().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
            }
            isMarked = false;
        } else {
            MarkerOptions markerOptions = new MarkerOptions().position(point);
            mapboxMap.addMarker(markerOptions);
            waypointOptionsController.getWaypointOptions().startAnimation(Animations.entryAnimation);
            waypointOptionsController.getWaypointOptions().setVisibility(View.VISIBLE);
            isMarked = true;
        }
    }

    /**
     * Method to delete all the marks in the map.
     */
    public void deleteMarks() {
        for (Marker marker:mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }

    /**
     * This is a setter method to isMarked attribute.
     * @param marked Is the new value to isMarked.
     */
    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
