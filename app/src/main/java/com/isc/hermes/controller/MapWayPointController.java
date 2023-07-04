package com.isc.hermes.controller;

import android.content.Context;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.model.navigation.directions.DirectionEnum;
import com.isc.hermes.model.navigation.directions.DirectionsParser;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import org.json.JSONException;

import java.util.concurrent.CompletableFuture;


/**
 * Class to configure the event of do click on a map
 */
public class MapWayPointController implements MapClickConfigurationController {
    private MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;
    private Context context;
    private DirectionsParser directionsParser;

    /**
     * This is the constructor method.
     * @param context Is the context application.
     */
    public MapWayPointController(Context context ) {
        this.context = context;
        this.mapboxMap = MapManager.getInstance().getMapboxMap();
        waypointOptionsController = new WaypointOptionsController(context, this);
        isMarked = false;
        Animations.loadAnimations();
        directionsParser = new DirectionsParser();
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        mapboxMap = MapManager.getInstance().getMapboxMap();
        if (NavigationOptionsController.isActive) {
            deleteMarks();
            waypointOptionsController.getNavOptionsFormController().setStartPoint(point);
            markPointBehavior(point);
        } else if (!InfoRouteController.getInstance(context, waypointOptionsController.getNavOptionsFormController()).isActive()) {
            doMarkOnMapAction(point);
            waypointOptionsController.getNavOptionsFormController().setFinalNavigationPoint(point);
        } setIncidentComponents(point);
        return true;
    }

    /**
     * Method to set the incident components alerts' dialog lebales
     * @param point latlng point to set the mark on map
     */
    private void setIncidentComponents(LatLng point) {
        IncidentsUploader.getInstance().setLastClickedPoint(point);
        try {
            IncidentDialogController.getInstance(context).showDialogCorrect(point);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        TrafficUploader.getInstance().setLastClickedPoint(point);
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
            setMarkerOnMap(point);
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point){
        if (isMarked){
            deleteLastMark();
            handleVisibilityPropertiesForLayouts();
            isMarked = false;
        } else {
            setMarkerOnMap(point);
            waypointOptionsController.getWaypointOptions().startAnimation(Animations.entryAnimation);
            waypointOptionsController.getWaypointOptions().setVisibility(View.VISIBLE);
            waypointOptionsController.setReportIncidentStatus(point);
            isMarked = true;
        }

        CompletableFuture.supplyAsync(() -> {
            String endFeatures = directionsParser.reverseGeocode(point);
            return endFeatures.isEmpty() ? "" : endFeatures;
        }).thenAccept(endStreetName -> ((AppCompatActivity) context).runOnUiThread(() -> {
            TextView t = ((AppCompatActivity) context).findViewById(R.id.place_name);
            t.setText(endStreetName.length() > 70 ? endStreetName.substring(0, 70) + "..." :
                    endStreetName);
        }));
    }

    /**
     * Method to delete all waypoint marks on the map
     */
    public void deleteMarks() {
        this.mapboxMap = MapManager.getInstance().getMapboxMap();
        for (Marker marker:mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }

    /**
     * Method to render a marker on map
     */
    public void setMarkerOnMap(LatLng point) {
        this.mapboxMap = MapManager.getInstance().getMapboxMap();
        MarkerOptions markerOptions = new MarkerOptions().position(point);
        mapboxMap.addMarker(markerOptions);
    }

    /**
     * Method to delete last mark set on map
     */
    public void deleteLastMark(){
        mapboxMap = MapManager.getInstance().getMapboxMap();
        if(mapboxMap.getMarkers().size()>0){
            mapboxMap.removeMarker(mapboxMap.getMarkers().remove(mapboxMap.getMarkers().size()-1));
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
