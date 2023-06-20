package com.isc.hermes.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapClickEventsManager;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class WaypointOptionsController {
    private final RelativeLayout waypointOptions;
    private final IncidentFormController incidentFormController;
    private final TrafficAutomaticFormController trafficAutomaticFormController;
    private final Button navigateButton;
    private final Button reportIncidentButton;
    private final Button reportTrafficButton;
    private final Button reportNaturalDisasterButton;
    private final Context context;
    private TextView placeName;

    /**
     * This is the constructor method. Init all the components of UI.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public WaypointOptionsController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        waypointOptions = ((AppCompatActivity)context).findViewById(R.id.waypoint_options);
        incidentFormController = new IncidentFormController(context, mapWayPointController);
        trafficAutomaticFormController = new TrafficAutomaticFormController(context, mapWayPointController);
        navigateButton = ((AppCompatActivity) context).findViewById(R.id.navigate_button);
        reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
        reportTrafficButton = ((AppCompatActivity) context).findViewById(R.id.report_traffic_button);
        reportNaturalDisasterButton = ((AppCompatActivity) context).findViewById(R.id.report_natural_disaster_button);
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


            AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {

                    return trafficAutomaticFormController.uploadTrafficDataBase();
                }
                @Override
                protected void onPostExecute(Integer responseCode) {
                    trafficAutomaticFormController.handleUploadResponse(responseCode);
                }
            };

            task.execute();
            waypointOptions.setVisibility(View.GONE);
            incidentFormController.getMapController().deleteMarks();

        });

        reportNaturalDisasterButton.setOnClickListener(v->{
            MapClickEventsManager.getInstance().removeCurrentClickController();
            MapClickEventsManager.getInstance().setMapClickConfiguration(new MapPolygonController(MapClickEventsManager.getInstance().getMapboxMap(), this.context));
            waypointOptions.startAnimation(Animations.exitAnimation);
            waypointOptions.setVisibility(View.GONE);
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

}
