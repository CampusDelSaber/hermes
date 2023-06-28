package com.isc.hermes.controller;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LATITUDE;
import static com.isc.hermes.ActivitySelectRegion.MAP_CENTER_LONGITUDE;
import static com.isc.hermes.ActivitySelectRegion.ZOOM_LEVEL;

import com.isc.hermes.ActivitySelectRegion;
import com.isc.hermes.R;
import com.isc.hermes.model.incidentsRequesting.NaturalDisasterRequesting;
import com.isc.hermes.utils.MapManager;
import com.isc.hermes.view.IncidentViewNavigation;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;

/**
 * Class to manage all view elements in view incidents form
 */
public class ViewIncidentsController{
    private AppCompatActivity activity;
    private final Button displayIncidentsButton;
    private Button okButton;
    private final Button cancelButton;
    private final ConstraintLayout displayIncidents;
    private final CheckBox naturalDisasters;
    private final CheckBox traffic;
    private final CheckBox streetIncident;
    private final NaturalDisasterRequesting requesting;


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
        mostrar();
    }

    /**
     * Method to init the action about click on a single button on show incidents form
     */

    private void showIncidentsScreen() {
        IncidentViewNavigation.getInstance(activity)
                .initIncidentButtonFunctionality(displayIncidentsButton, displayIncidents);
        cancelButton.setOnClickListener(v -> hideOptions());
    }

    /**
     * This method hides the incident generation selection menu.
     */
    public void hideOptions(){
        displayIncidents.setVisibility(View.GONE);
    }

    private void mostrar(){
        okButton.setOnClickListener(v -> {
                if (naturalDisasters.isChecked()) {
                    PolygonVisualizationController
                            .getInstance()
                            .displayPointsPolygonOnMap(
                                    requesting.getAllPolygonPoints(),"#Ff0000"
                            );
                }
                if (traffic.isChecked()) {
                    try {
                        ShowTrafficController.getInstance().showTraffic(MapManager.getInstance().getMapboxMap());
                    } catch (JSONException e) {
                        Toast.makeText(activity, "Failure to get the incidents data. Please, try again", Toast.LENGTH_SHORT).show();
                    }
                }
                if (streetIncident.isChecked()) {
                    IncidentsGetterController
                            .getInstance()
                            .getNearIncidentsWithinRadius(
                                    MapManager.getInstance().getMapboxMap(), activity
                            );
                }
            hideOptions();
        });
    }
}