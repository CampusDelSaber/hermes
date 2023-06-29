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
    private final AppCompatActivity activity;
    private final Button displayIncidentsButton;
    private Button okButton;
    private final Button cancelButton;
    private final ConstraintLayout displayIncidents;


    public ViewIncidentsController(AppCompatActivity activity){
        this.activity = activity;
        displayIncidentsButton = activity.findViewById(R.id.displayIncidentsButton);
        displayIncidents = activity.findViewById(R.id.display_incidents);
        okButton = activity.findViewById(R.id.okButton);
        cancelButton = activity.findViewById(R.id.cancelButton);
        showIncidentsScreen();
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
}
