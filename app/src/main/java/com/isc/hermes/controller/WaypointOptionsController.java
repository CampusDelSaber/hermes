package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class WaypointOptionsController {
    private final RelativeLayout waypointOptions;
    private final IncidentFormController incidentFormController;
    private final TrafficFormController trafficFormController;
    private final Button navigateButton;
    private final Button reportIncidentButton;
    private final Button reportTrafficButton;
    private TextView placeName;

    /**
     * This is the constructor method. Init all the components of UI.
     *
     * @param context Is the context application.
     * @param mapController Is the controller of the map.
     */
    public WaypointOptionsController(Context context, MapController mapController) {
        waypointOptions = ((AppCompatActivity)context).findViewById(R.id.waypoint_options);
        incidentFormController = new IncidentFormController(context, mapController);
        trafficFormController = new TrafficFormController(context,mapController);
        navigateButton = ((AppCompatActivity) context).findViewById(R.id.navigate_button);
        reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
        reportTrafficButton = ((AppCompatActivity) context).findViewById(R.id.report_traffic_button);
        placeName = ((AppCompatActivity) context).findViewById(R.id.place_name);
        setButtonsOnClick();

    }

    /**
     * Method to assign functionality to the buttons of the view.
     */
    private void setButtonsOnClick(){
        navigateButton.setOnClickListener(v -> {

        });

        reportIncidentButton.setOnClickListener(v -> {
            waypointOptions.startAnimation(Animations.exitAnimation);
            incidentFormController.getIncidentForm().startAnimation(Animations.entryAnimation);
            incidentFormController.getIncidentForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
            incidentFormController.getMapController().deleteMarks();

        });

        reportTrafficButton.setOnClickListener(v -> {

            waypointOptions.startAnimation(Animations.exitAnimation);
            trafficFormController.getTrafficForm().startAnimation(Animations.entryAnimation);
            trafficFormController.getTrafficForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
            incidentFormController.getMapController().deleteMarks();

        });
    }


    /**
     * This is a getter method to waypoint options layout.
     * @return Return a layout.
     */
    public RelativeLayout getWaypointOptions() {
        return waypointOptions;
    }

    /**
     * This is the getter method to incident form controller.
     * @return Return the controller class of incident form view.
     */
    public IncidentFormController getIncidentFormController() {
        return incidentFormController;
    }

    public TrafficFormController getTrafficFormController() {
        return trafficFormController;
    }
}
