package com.isc.hermes.controller;

import android.content.Context;

import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.utils.Animations;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import org.json.JSONException;


/**
 * Class to configure the event of do click on a map
 */
public class MapWayPointController implements MapClickConfigurationController {
    private final MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;
    private Context context;

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
        Animations.loadAnimations();
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if (NavigationOptionsController.isActive) {
            waypointOptionsController.getNavOptionsFormController().setStartPoint(point);
            markPointBehavior(point);
        } else if (!InfoRouteController.getInstance(context).isActive()) {
            Log.i("Mau","Entre a dibuajr unb");
            doMarkOnMapAction(point);
            waypointOptionsController.getNavOptionsFormController().setFinalNavigationPoint(point);
        } else {
            Log.i("Mau","No puedo dibujar");
        }
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
     * Method to handle the visibility of the layouts on screen
     */
    private void handleVisibilityPropertiesForLayouts() {
        if(waypointOptionsController.getWaypointOptions().getVisibility() == View.VISIBLE) {
            waypointOptionsController.getWaypointOptions().startAnimation(Animations.exitAnimation);
            waypointOptionsController.getWaypointOptions().setVisibility(View.GONE);
        }
        if(waypointOptionsController.getIncidentFormController().getIncidentForm().getVisibility() == View.VISIBLE) {
            waypointOptionsController.getIncidentFormController().getIncidentForm().startAnimation(Animations.exitAnimation);
            waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
        }

        if(waypointOptionsController.getNavOptionsFormController().getNavOptionsForm().getVisibility() == View.VISIBLE) {
            waypointOptionsController.getNavOptionsFormController().getNavOptionsForm().startAnimation(Animations.exitAnimation);
            waypointOptionsController.getNavOptionsFormController().getNavOptionsForm().setVisibility(View.GONE);
        }
    }

    /**
     * Method to set the marker behavior on map
     * @param point geocode point to set
     */
    private void markPointBehavior(LatLng point) {
            deleteMarks();
            setMarkerOnMap(point);
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point){
        if (isMarked){
            deleteMarks();
            handleVisibilityPropertiesForLayouts();
            isMarked = false;
        } else {
            setMarkerOnMap(point);
            waypointOptionsController.getWaypointOptions().startAnimation(Animations.entryAnimation);
            waypointOptionsController.getWaypointOptions().setVisibility(View.VISIBLE);
            waypointOptionsController.setReportIncidentStatus(point);
            isMarked = true;
        }
    }

    /**
     * Method to render a marker on map
     */
    public void setMarkerOnMap(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions().position(point);
        mapboxMap.addMarker(markerOptions);
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
