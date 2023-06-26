package com.isc.hermes.controller;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.incidentsRequesting.NaturalDisasterRequesting;

import java.util.List;

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

    public ViewIncidentsController(AppCompatActivity activity){
        this.requesting = new NaturalDisasterRequesting();
        this.incidentsVisualizationForm = activity.findViewById(R.id.viewIncidentsForm);
        this.viewIncidentsFormButton = activity.findViewById(R.id.viewIncidentsButton);
        this.viewTrafficButton = activity.findViewById(R.id.viewTrafficButton);
        this.viewSocialEventButton = activity.findViewById(R.id.viewSocialIncidentsButton);
        this.viewNaturalDisasterButton = activity.findViewById(R.id.viewNaturalDisasterButton);
        initEyeButtonFunctionality();
        initViewDifferentIncidentsTypeButton();
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
    private void initViewDifferentIncidentsTypeButton() {

        viewTrafficButton.setOnClickListener(v -> {
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
