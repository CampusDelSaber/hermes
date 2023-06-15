package com.isc.hermes.controller;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.generators.GeneratorManager;
import com.isc.hermes.generators.Radium;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.view.GenerateRandomIncidentView;
import java.util.ArrayList;

/**
 * This class is the controller of the entire random event generation mechanism.
 */
public class GenerateRandomIncidentController  {

    private GenerateRandomIncidentView generateRandomIncidentVIew;
    private GeneratorManager generatorManager;

    /**
     * Constructor, initializes the view and the components necessary to generate the incidents.
     *
     * @param activity Receives an AppCompacActivity to get the xml elements and initialize it.
     */
    public GenerateRandomIncidentController(AppCompatActivity activity){
        generateRandomIncidentVIew = new GenerateRandomIncidentView(activity);
        Button generateButton = activity.findViewById(R.id.startGenerateIncidentButton);
        generateButton.setOnClickListener(v -> initGeneration());
        generatorManager = new GeneratorManager();
    }

    /**
     * This method initiates the incident generation process.
     */
    private void initGeneration(){
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null,null).getCurrentLocationModel();
        Double[] referencePoint = {currentLocation.getLatitude(), currentLocation.getLongitude()};
        Radium radium = generateRandomIncidentVIew.getRadiumSelected();
        int quantity = generateRandomIncidentVIew.getNumberIncidentsSelected();
        if(radium != null && quantity != 0){
            generateRandomIncidentVIew.hideOptions();
            ArrayList<Incident> incidents = generatorManager.generateRandomIncidents(radium, referencePoint, quantity);
        }
    }
}
