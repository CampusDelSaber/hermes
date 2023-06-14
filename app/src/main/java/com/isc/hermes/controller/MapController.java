package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to configure the event of do click on a map
 */
public class MapController implements MapboxMap.OnMapClickListener {
    private final MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;
    private int markerCount = 0;
    private final Context context;

    private LatLng previousPoint;
    private Polyline drawnLine;
    /**
     * This is the constructor method.
     *
     * @param mapboxMap Is the map.
     * @param context Is the context application.
     */
    public MapController(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        this.context = context;
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
        doMarkOnMapAction(point);
        return true;
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point){
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
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
            if(waypointOptionsController.getTrafficFormController().getTrafficForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getTrafficFormController().getTrafficForm().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getTrafficFormController().getTrafficForm().setVisibility(View.GONE);
                doMarkOnMapAction2(point);


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
     * Method to perform action to click on map and
     * will help to control that the point is placed at a point
     * and from that point create the traffic line that is there.
     * @param point2 Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction2(LatLng point2) {


        PointF screenPoint2 = mapboxMap.getProjection().toScreenLocation(point2);
        List<Feature> features2 = mapboxMap.queryRenderedFeatures(screenPoint2);


            if (previousPoint != null && (features2.get(0).geometry().type().equals("MultiLineString") || features2.get(0).geometry().type().equals("LineString"))) {
                List<LatLng> points = new ArrayList<>();
                points.add(previousPoint);
                points.add(point2);

                if (drawnLine != null) {
                    mapboxMap.removePolyline(drawnLine);
                }

                drawnLine = mapboxMap.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .color(Color.RED)
                        .width(5f));
            }else {
                Toast.makeText(context, "Touch on street or avenue", Toast.LENGTH_SHORT).show();
            }

            previousPoint = point2;
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
