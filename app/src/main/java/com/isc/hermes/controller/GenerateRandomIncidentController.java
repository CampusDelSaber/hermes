package com.isc.hermes.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.generators.GeneratorManager;
import com.isc.hermes.generators.Radium;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.view.GenerateRandomIncidentView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class is the controller of the entire random event generation mechanism.
 */
public class GenerateRandomIncidentController  {

    private GenerateRandomIncidentView generateRandomIncidentVIew;
    private GeneratorManager generatorManager;
    private IncidentsUploader incidentsUploader;

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
        incidentsUploader = IncidentsUploader.getInstance();
    }

    /**
     * This method initiates the incident generation process.
     */
    private void initGeneration(){
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null,null).getCurrentLocationModel();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Double[] referencePoint = {currentLocation.getLatitude(), currentLocation.getLongitude()};
        Radium radium = generateRandomIncidentVIew.getRadiumSelected();
        int quantity = generateRandomIncidentVIew.getNumberIncidentsSelected();
        if(radium != null && quantity != 0){
            generateRandomIncidentVIew.hideOptions();
            ArrayList<Incident> incidents = generatorManager.generateRandomIncidents(radium, referencePoint, quantity);
            for (Incident currentIncident : incidents){
                String dateCreated = simpleDateFormat.format(currentIncident.getDateCreated());
                String dateDeath = simpleDateFormat.format(currentIncident.getDeathDate());
                String jsonString = incidentsUploader.generateJsonIncident(IncidentsUtils.getInstance().generateObjectId(),
                        currentIncident.getType(),
                        currentIncident.getReason(),
                        dateCreated,
                        dateDeath,
                        currentIncident.getGeometry().getType(),
                        currentIncident.getGeometry().getStringCoordinates()
                        );
                Log.i("MAU",jsonString);
                uploadToDataBase(jsonString);

            }
        }
    }

    public void uploadToDataBase(String jsonString){
        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return incidentsUploader.uploadIncident(jsonString);
            };
        };
        task.execute();
    }
}
