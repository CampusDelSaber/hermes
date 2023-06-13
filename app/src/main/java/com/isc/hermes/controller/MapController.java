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
    private final Context context;
    private RelativeLayout waypointInfo;
    private final IncidentFormController incidentForm;
    /*private Button cancelButton;
    private Button acceptButton;*/
    private Button reportIncidentButton;
    private boolean isMarked;


    public MapController(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        incidentForm = new IncidentFormController(context, this);
        assignViews();
        initForm();
        mapboxMap.addOnMapClickListener(this);
        isMarked = false;
        setIncidentComponents();
    }

    private void assignViews() {
        //incidentForm = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        waypointInfo = ((AppCompatActivity)context).findViewById(R.id.waypoint_info);
        /*cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);*/
        reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
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
        if (isMarked){
            deleteMarks();
            if(waypointInfo.getVisibility() == View.VISIBLE) {
                waypointInfo.startAnimation(Animations.exitAnimation);
                waypointInfo.setVisibility(View.GONE);
            }
            if(incidentForm.getIncidentForm().getVisibility() == View.VISIBLE) {
                incidentForm.getIncidentForm().startAnimation(Animations.exitAnimation);
                incidentForm.getIncidentForm().setVisibility(View.GONE);
            }
            isMarked = false;
        } else {
            MarkerOptions markerOptions = new MarkerOptions().position(point);
            mapboxMap.addMarker(markerOptions);
            waypointInfo.startAnimation(Animations.entryAnimation);
            waypointInfo.setVisibility(View.VISIBLE);
            isMarked = true;
        }
    }

    /**
     * Method to init all elements on report incident form
     */
    private void initForm(){

        reportIncidentButton.setOnClickListener(v -> {
            waypointInfo.startAnimation(Animations.exitAnimation);
            incidentForm.getIncidentForm().startAnimation(Animations.entryAnimation);
            incidentForm.getIncidentForm().setVisibility(View.VISIBLE);
            waypointInfo.setVisibility(View.GONE);
        });
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
     * Method assign values to the incident components.
     *
     * <p>
     *     This method assign values and views to the incident components such as the incident type
     *     spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setIncidentComponents() {
        Spinner incidentType = ((AppCompatActivity) context).findViewById(R.id.incident_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.incidents_type, R.layout.incident_spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentType.setAdapter(adapter);

        Spinner incidentEstimatedTime = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        ArrayAdapter<CharSequence> adapterTime=ArrayAdapter.createFromResource(context, R.array.incidents_estimated_time, R.layout.incident_spinner_items);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentEstimatedTime.setAdapter(adapterTime);

        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        incidentTimePicker.setMinValue(1);
        incidentTimePicker.setMaxValue(10);
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
