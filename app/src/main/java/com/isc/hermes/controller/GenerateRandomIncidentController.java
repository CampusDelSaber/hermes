package com.isc.hermes.controller;

import android.os.AsyncTask;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.generators.IncidentGeneratorManage;
import com.isc.hermes.generators.Radium;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.view.GenerateRandomIncidentView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller of the entire random event generation mechanism.
 */
public class GenerateRandomIncidentController {

    private GenerateRandomIncidentView generateRandomIncidentView;
    private IncidentGeneratorManage incidentGeneratorManage;
    private IncidentsUploader incidentsUploader;

    /**
     * Constructor, initializes the view and the components necessary to generate the incidents.
     *
     * @param activity Receives an AppCompacActivity to get the xml elements and initialize it.
     */
    public GenerateRandomIncidentController(AppCompatActivity activity) {
        generateRandomIncidentView = new GenerateRandomIncidentView(activity);
        Button generateButton = activity.findViewById(R.id.startGenerateIncidentButton);
        generateButton.setOnClickListener(v -> initGeneration());
        incidentGeneratorManage = new IncidentGeneratorManage();
        incidentsUploader = IncidentsUploader.getInstance();
    }

    /**
     * This method initiates the incident generation process.
     * <p>
     * Gets the current location and input parameters received from the UI to generate the
     * incidents, uses the generatorManager to generate incidents and builds a string with
     * the structure of a json to upload the data to the database.
     * </p>
     */
    private void initGeneration() {
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null, null).getCurrentLocationModel();
        Double[] referencePoint = {currentLocation.getLatitude(), currentLocation.getLongitude()};
        Radium radium = generateRandomIncidentView.getRadiumSelected();
        int quantity = generateRandomIncidentView.getNumberIncidentsSelected();
        if (radium != null && quantity != 0) {
            ArrayList<Incident> incidents = incidentGeneratorManage.generateRandomIncidents(radium, referencePoint, quantity);
            uploadToDataBase(incidents);
        }
        generateRandomIncidentView.hideOptions();
    }

    /**
     * This method creates a thread and calls the database to upload the generated incidents.
     *
     * @param incidents list incidents to upload to the data base.
     */
    private void uploadToDataBase(List<Incident> incidents) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (Incident currentIncident : incidents) {
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
            AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    return incidentsUploader.uploadIncident(jsonString);
                }

                ;
            };
            task.execute();
        }
    }
}
