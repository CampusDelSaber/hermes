package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to configure the event of do click on a map
 */
public class MapClickEventListenerController implements MapboxMap.OnMapClickListener {
    private final MapboxMap mapboxMap;
    private final Context context;
    private final RelativeLayout waypointInfo;
    private final RelativeLayout incidentForm;
    private boolean pointSet;
    TranslateAnimation entryAnimation;
    TranslateAnimation exitAnimation;


    public MapClickEventListenerController(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        incidentForm = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        waypointInfo = ((AppCompatActivity)context).findViewById(R.id.waypoint_info);
        initForm();
        mapboxMap.addOnMapClickListener(this);
        pointSet = false;
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        /*if (MapClickEventsManager.isOnReportIncidentMode){
            doMarkOnMapAction(point);
        }*/
        doMarkOnMapAction(point);
        return true;
    }

    /**
     * Method to perform action to click on map
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point){
        if (pointSet){
            deleteMarks();
            if(waypointInfo.getVisibility() == View.VISIBLE) {
                waypointInfo.startAnimation(exitAnimation);
                waypointInfo.setVisibility(View.GONE);
            }
            if(incidentForm.getVisibility() == View.VISIBLE) {
                incidentForm.startAnimation(exitAnimation);
                incidentForm.setVisibility(View.GONE);
            }

            pointSet = false;
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions().position(point);
        mapboxMap.addMarker(markerOptions);
        waypointInfo.startAnimation(entryAnimation);
        waypointInfo.setVisibility(View.VISIBLE);
        pointSet = true;
    }

    /**
     * Method to init all elements on report incident form
     */
    private void initForm(){
        entryAnimation = new TranslateAnimation(0, 0, 800, 0);
        entryAnimation.setDuration(600);
        entryAnimation.setStartOffset(50);

        exitAnimation = new TranslateAnimation(0, 0, 0, 800);
        exitAnimation.setDuration(600);
        exitAnimation.setStartOffset(50);

        Spinner incidentType = ((AppCompatActivity) context).findViewById(R.id.incident_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.incidents_type, R.layout.incident_spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentType.setAdapter(adapter);

        Spinner incidentEstimatedTime = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        ArrayAdapter<CharSequence> adapterTime=ArrayAdapter.createFromResource(context, R.array.incidents_estimated_time, R.layout.incident_spinner_items);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentEstimatedTime.setAdapter(adapterTime);

        NumberPicker numberPicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);


        Button cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> {
            MapClickEventsManager.isOnReportIncidentMode = false;
            pointSet = false;
            incidentForm.startAnimation(exitAnimation);
            incidentForm.setVisibility(View.GONE);
            deleteMarks();
        });

        Button acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(v -> {
            MapClickEventsManager.isOnReportIncidentMode = false;
            pointSet = false;
            incidentForm.startAnimation(exitAnimation);
            incidentForm.setVisibility(View.GONE);
            deleteMarks();
            Toast.makeText(context, "Incident Saved Correctly.", Toast.LENGTH_SHORT).show();
        });

        Button reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
        reportIncidentButton.setOnClickListener(v -> {
            waypointInfo.startAnimation(exitAnimation);
            incidentForm.startAnimation(entryAnimation);
            incidentForm.setVisibility(View.VISIBLE);
            waypointInfo.setVisibility(View.GONE);
        });

    }

    private void deleteMarks() {
        for (Marker marker:mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }
}
