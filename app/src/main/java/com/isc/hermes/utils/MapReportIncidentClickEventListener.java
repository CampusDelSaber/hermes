package com.isc.hermes.utils;

import android.content.Context;
import android.graphics.PointF;
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
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

/**
 * Class to configure the event of do click on a map
 */
public class MapReportIncidentClickEventListener implements MapboxMap.OnMapClickListener {
    private final MapboxMap mapboxMap;
    private final Context context;
    private final RelativeLayout relativeLayout;
    private RelativeLayout formLayout;
    private boolean pointSet;
    TranslateAnimation entryAnimation;
    TranslateAnimation exitAnimation;


    public MapReportIncidentClickEventListener(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        this.context = context;
         relativeLayout = ((AppCompatActivity)context).findViewById(R.id.incident_form);
         relativeLayout.setVisibility(View.GONE);
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
        
        if (MapClickEventsManager.isOnReportIncidentMode){
            doMarkOnMapAction(point);
        }
        return true;
    }

    private void doMarkOnMapAction(LatLng point){
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (pointSet) return;
        if (!features.isEmpty() && (features.get(0).geometry().type().equals("MultiLineString") || features.get(0).geometry().type().equals("LineString") ) ) {
            MarkerOptions markerOptions = new MarkerOptions().position(point);
            mapboxMap.addMarker(markerOptions);
            formLayout.startAnimation(entryAnimation);
            relativeLayout.setVisibility(View.VISIBLE);

            pointSet = true;
        } else {

            Toast.makeText(context, "Touch on street or avenue", Toast.LENGTH_SHORT).show();
        }
    }

    private void initForm(){
        formLayout = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        entryAnimation = new TranslateAnimation(0, 0, 1000, 0);
        entryAnimation.setDuration(800);
        entryAnimation.setStartOffset(100);

        exitAnimation = new TranslateAnimation(0, 0, 0, 1000);
        exitAnimation.setDuration(800);
        exitAnimation.setStartOffset(100);

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
            formLayout.startAnimation(exitAnimation);
            formLayout.setVisibility(View.GONE);
            for (Marker marker:mapboxMap.getMarkers()) {
                mapboxMap.removeMarker(marker);
            }
        });

        Button acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(v -> {
            MapClickEventsManager.isOnReportIncidentMode = false;
            pointSet = false;
            formLayout.startAnimation(exitAnimation);
            formLayout.setVisibility(View.GONE);
            for (Marker marker:mapboxMap.getMarkers()) {
                mapboxMap.removeMarker(marker);
            }
            Toast.makeText(context, "Incident Saved Correctly.", Toast.LENGTH_SHORT).show();
        });
    }
}
