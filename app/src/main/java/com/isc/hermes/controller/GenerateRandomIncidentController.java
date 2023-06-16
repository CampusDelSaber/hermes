package com.isc.hermes.controller;

import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.generators.Radium;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.view.GenerateRandomIncidentView;

/**
 * This class is the controller of the entire random event generation mechanism.
 */
public class GenerateRandomIncidentController  {

    GenerateRandomIncidentView generateRandomIncidentVIew;

    /**
     * Constructor, initializes the view and the components necessary to generate the incidents.
     *
     * @param activity Receives an AppCompacActivity to get the xml elements and initialize it.
     */
    public GenerateRandomIncidentController(AppCompatActivity activity){
        generateRandomIncidentVIew = new GenerateRandomIncidentView(activity);
        Button generateButton = activity.findViewById(R.id.startGenerateIncidentButton);
        generateButton.setOnClickListener(v -> initGeneration());
    }

    /**
     * This method initiates the incident generation process.
     */
    private void initGeneration(){
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null,null).getCurrentLocationModel();
        Radium radium = generateRandomIncidentVIew.getRadiumSelected();
        int quantity = generateRandomIncidentVIew.getNumberIncidentsSelected();
        if(radium != null && quantity != 0){
            generateRandomIncidentVIew.hideOptions();
            Log.i("INFOMAU", "Radium:  "+ radium.getValue()+ " --- Quantity : " + quantity);
        }
    }
}
