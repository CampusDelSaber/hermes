package com.isc.hermes.controller;

import static com.mongodb.assertions.Assertions.assertNotNull;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.isc.hermes.ActivitySelectRegion;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.model.incidentsRequesting.NaturalDisasterRequesting;
import com.isc.hermes.view.IncidentViewNavigation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class to manage all view elements in view incidents form
 */
public class ViewIncidentsController{
    private AppCompatActivity activity;
    private final Button displayIncidentsButton;
    private Button okButton;
    private final Button cancelButton;
    private final ConstraintLayout displayIncidents;
    private final NaturalDisasterRequesting requesting;
    private boolean isTrafficVisible = false;
    private ShowTrafficController showTrafficController = new ShowTrafficController();


    public ViewIncidentsController(AppCompatActivity activity){
        this.activity = activity;
        this.requesting = new NaturalDisasterRequesting();
        displayIncidentsButton = activity.findViewById(R.id.displayIncidentsButton);
        displayIncidents = activity.findViewById(R.id.display_incidents);
        okButton = activity.findViewById(R.id.okButton);
        cancelButton = activity.findViewById(R.id.cancelButton);
        showIncidentsScreen();
    }

    /**
     *This method displays the incidents screen.
     *
     * The types of incidents that you have in the application will
     * be displayed where you can mark or unmark the incidents that
     * you want to be shown.
     */
    /**
     * Method to init the action about click on a single button on show incidents form
     */
    private void initViewDifferentIncidentsTypeButton(ActivitySelectRegion mapDisplay) {
        viewTrafficButton.setOnClickListener(v -> {
            try {
                if (isTrafficVisible) {
                    showTrafficController.hideTraffic(mapDisplay);
                    isTrafficVisible = false;
                } else {
                    showTrafficController.showTraffic(mapDisplay);
                    isTrafficVisible = true;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            disappearForm();
        });

        viewSocialEventButton.setOnClickListener(v -> {
            disappearForm();
        });

        viewNaturalDisasterButton.setOnClickListener(v -> {
            PolygonVisualizationController.getInstance().displayPointsPolygonOnMap(requesting.getAllPolygonPoints(),"#Ff0000");
            disappearForm();
        });
    }
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
}
