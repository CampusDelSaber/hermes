package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    private final NavigationOptionsController navigationOptionsFormController;
    private final Button navigateButton;
    private final Button reportIncidentButton;
    private final Button reportTrafficButton;
    private final Button reportNaturalDisasterButton;
    private final Context context;

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
        navigationOptionsFormController = new NavigationOptionsController(context, mapWayPointController);
        navigateButton = ((AppCompatActivity) context).findViewById(R.id.navigate_button);
        reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
        reportTrafficButton = ((AppCompatActivity) context).findViewById(R.id.report_traffic_button);
        reportNaturalDisasterButton = ((AppCompatActivity) context).findViewById(R.id.report_natural_disaster_button);
        setButtonsOnClick();
    }

    /**
     * Method to assign functionality to the buttons of the view.
     */
    private void setButtonsOnClick(){
        navigateButton.setOnClickListener(v -> {
            waypointOptions.startAnimation(Animations.exitAnimation);
            navigationOptionsFormController.getNavOptionsForm().startAnimation(Animations.entryAnimation);
            navigationOptionsFormController.getNavOptionsForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
        });

        reportIncidentButton.setOnClickListener(v -> {
            waypointOptions.startAnimation(Animations.exitAnimation);
            incidentFormController.getIncidentForm().startAnimation(Animations.entryAnimation);
            incidentFormController.getIncidentForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
        });

        reportTrafficButton.setOnClickListener(v -> {

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

    /**
     * This is the getter method to get the navigation options controller instance.
     * @return Return the navigation options controller form view.
     */
    public NavigationOptionsController getNavOptionsFormController() {
        return navigationOptionsFormController;
    }
}
