package com.isc.hermes.controller;

import static com.mongodb.assertions.Assertions.assertNotNull;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.ActivitySelectRegion;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.model.incidentsRequesting.NaturalDisasterRequesting;
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
    private final LinearLayout incidentsVisualizationForm;
    private final ImageButton viewIncidentsFormButton;
    private final Button viewTrafficButton;
    private final Button viewSocialEventButton;
    private final Button viewNaturalDisasterButton;
    private boolean buttonMarked;
    private final NaturalDisasterRequesting requesting;
    private boolean isTrafficVisible = false;
    private ShowTrafficController showTrafficController = new ShowTrafficController();



    public ViewIncidentsController(AppCompatActivity activity,ActivitySelectRegion mapDisplay){
        this.requesting = new NaturalDisasterRequesting();
        this.incidentsVisualizationForm = activity.findViewById(R.id.viewIncidentsForm);
        this.viewIncidentsFormButton = activity.findViewById(R.id.viewIncidentsButton);
        this.viewTrafficButton = activity.findViewById(R.id.viewTrafficButton);
        this.viewSocialEventButton = activity.findViewById(R.id.viewSocialIncidentsButton);
        this.viewNaturalDisasterButton = activity.findViewById(R.id.viewNaturalDisasterButton);
        initEyeButtonFunctionality();
        initViewDifferentIncidentsTypeButton(mapDisplay);
    }


    /**
     * Method to init the button with eye functionality
     */
    private void initEyeButtonFunctionality() {
        viewIncidentsFormButton.setOnClickListener(v -> {
            if (!buttonMarked) incidentsVisualizationForm.setVisibility(View.VISIBLE);
            else incidentsVisualizationForm.setVisibility(View.GONE);
            buttonMarked = !buttonMarked;
        });
    }

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

    /**
     * Method to make buttons form invisible
     */
    private void disappearForm(){
        incidentsVisualizationForm.setVisibility(View.GONE);
        buttonMarked = !buttonMarked;
    }
}
