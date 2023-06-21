package com.isc.hermes.controller;

import android.content.Context;

import android.graphics.PointF;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.utils.Animations;

import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class to configure the event of do click on a map
 */
public class MapWayPointController implements MapClickConfigurationController {
    private final MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;
    private Context context;
    private int markerCount;
    /**
     * This is the constructor method.
     *
     * @param mapboxMap Is the map.
     * @param context Is the context application.
     */
    public MapWayPointController(MapboxMap mapboxMap, Context context ) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        waypointOptionsController = new WaypointOptionsController(context, this);
        isMarked = false;
        markerCount = 0;
        Animations.loadAnimations();
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        doMarkOnMapAction(point);
        IncidentsUploader.getInstance().setLastClickedPoint(point);
        try {
            IncidentDialogController.getInstance(context).showDialogCorrect(point);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        TrafficUploader.getInstance().setLastClickedPoint(point);

        return true;
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */

    private void doMarkOnMapAction(LatLng point) {

        if (isMarked || !mapboxMap.getMarkers().isEmpty()) {
            deleteMarks();
            if (waypointOptionsController.getWaypointOptions().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getWaypointOptions().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getWaypointOptions().setVisibility(View.GONE);
            }
            if (waypointOptionsController.getIncidentFormController().getIncidentForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getIncidentFormController().getIncidentForm().startAnimation(Animations.exitAnimation);
                conditionalTwoPoints(point);
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
     * Method to control when te user what to report an incident on a house
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void conditionalTwoPoints(LatLng point){
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (markerCount == 1 ) {
            waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
            markerCount = 0;
        }
        if (!features.isEmpty() && (features.get(0).geometry().type().equals("MultiLineString") || features.get(0).geometry().type().equals("LineString"))) {
            MarkerOptions markerOptions = new MarkerOptions().position(point);
            mapboxMap.addMarker(markerOptions);
            markerCount++;

        }else{
            Toast.makeText(context, "Please indicate a street or avenue", Toast.LENGTH_SHORT).show();
            waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
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

    /**
     * Method to will disable this controller from mapbox map given
     * @param mapboxMap is map witch will disable this controller
     */
    public void discardFromMap(MapboxMap mapboxMap) {mapboxMap.removeOnMapClickListener(this);}
}
