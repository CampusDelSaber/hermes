package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
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
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.utils.Animations;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
     * @param context   Is the context application.
     */
    public MapController(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        waypointOptionsController = new WaypointOptionsController(context, this);
        mapboxMap.addOnMapClickListener(this);
        isMarked = false;
        Animations.loadAnimations();
    }

    /**
     * Method to add markers to map variable
     *
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        doMarkOnMapAction(point);
        IncidentsUploader.getInstance().setLastClickedPoint(point);
        return true;
    }

    /**
     * Method to perform action to click on map
     *
     * @param point Is point passed as parameter with its latitude and longitude
     */

    private void doMarkOnMapAction(LatLng point) {
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (isMarked || !mapboxMap.getMarkers().isEmpty()) {
            deleteMarks();
            if (waypointOptionsController.getWaypointOptions().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getWaypointOptions().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getWaypointOptions().setVisibility(View.GONE);
            }
            if (waypointOptionsController.getIncidentFormController().getIncidentForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getIncidentFormController().getIncidentForm().startAnimation(Animations.exitAnimation);

                if (markerCount == 1 ) {
                    waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
                    markerCount = 0;
                }
                if (!features.isEmpty() && (features.get(0).geometry().type().equals("MultiLineString") || features.get(0).geometry().type().equals("LineString"))) {
                    MarkerOptions markerOptions = new MarkerOptions().position(point);
                    mapboxMap.addMarker(markerOptions);
                    markerCount++;


                }else{
                    Toast.makeText(context, "Invalid geometry type", Toast.LENGTH_SHORT).show();
                    waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
                }
            }

            if (waypointOptionsController.getTrafficFormController().gettrafficForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getTrafficFormController().gettrafficForm().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getTrafficFormController().gettrafficForm().setVisibility(View.GONE);
                AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... voids) {

                        return waypointOptionsController.getTrafficFormController().uploadtrafficDataBase();
                    }
                    @Override
                    protected void onPostExecute(Integer responseCode) {
                        waypointOptionsController.getTrafficFormController().handleUploadResponse(responseCode);
                    }
                };
                task.execute();


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
        for (Marker marker : mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }

    /**
     * This is a setter method to isMarked attribute.
     *
     * @param marked Is the new value to isMarked.
     */
    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
