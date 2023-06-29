package com.isc.hermes.controller;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.isc.hermes.R;
import com.isc.hermes.model.incidentsRequesting.NaturalDisasterRequesting;
import com.isc.hermes.utils.MapManager;
import com.isc.hermes.view.IncidentViewNavigation;

/**
 * Class to manage all view elements in view incidents form
 */
public class ViewIncidentsController{
    private final AppCompatActivity activity;
    private final Button displayIncidentsButton;
    private Button okButton;
    private final Button cancelButton;
    private final ConstraintLayout displayIncidents;
    private final CheckBox naturalDisasters;
    private final CheckBox traffic;
    private final CheckBox streetIncident;
    private final NaturalDisasterRequesting requesting;

    /**
     * Constructor for ViewIncidentsController.
     *
     * @param activity The activity associated with the controller.
     */
    public ViewIncidentsController(AppCompatActivity activity){
        this.activity = activity;
        this.requesting = new NaturalDisasterRequesting();
        displayIncidentsButton = activity.findViewById(R.id.displayIncidentsButton);
        displayIncidents = activity.findViewById(R.id.display_incidents);
        okButton = activity.findViewById(R.id.okButton);
        cancelButton = activity.findViewById(R.id.cancelButton);
        naturalDisasters = activity.findViewById(R.id.checkBoxNaturalDisasters);
        traffic = activity.findViewById(R.id.checkBoxTraffic);
        streetIncident = activity.findViewById(R.id.checkBoxStreetIncident);
        showIncidentsScreen();
        showIncidentOptions();
    }

    /**
     * Initializes the action when the display incidents button is clicked.
     */
    private void showIncidentsScreen() {
        IncidentViewNavigation.getInstance(activity)
                .initIncidentButtonFunctionality(displayIncidentsButton, displayIncidents);
        cancelButton.setOnClickListener(v -> hideOptions());
    }

    /**
     * This method hides the screen Display IncidentsButton screen.
     */
    public void hideOptions(){
        displayIncidents.setVisibility(View.GONE);
    }

    /**
     * Performs the necessary actions when the OK button is clicked.
     * Displays incidents based on the selected checkboxes and hides
     * the screen Display IncidentsButton.
     */
    private void showIncidentOptions(){
        okButton.setOnClickListener(v -> {
            MapManager.getInstance().getMapboxMap().clear();
                if (naturalDisasters.isChecked()) {
                    PolygonVisualizationController
                            .getInstance()
                            .displayPointsPolygonOnMap(
                                    requesting.getAllPolygonPoints(),"#Ff0000"
                            );
                }
                if (traffic.isChecked()) {
                    ShowTrafficController.getInstance().getTraffic(activity);
                }
                if (streetIncident.isChecked()) {
                    IncidentsGetterController.getInstance().getNearIncidentsWithinRadius(activity);
                }
            hideOptions();
        });
    }
}