package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.generators.IncidentGenerator;
import com.isc.hermes.model.Radium;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.view.GenerateRandomIncidentView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This class is the controller of the entire random event generation mechanism.
 */
public class GenerateRandomIncidentController {

    private GenerateRandomIncidentView generateRandomIncidentView;
    private IncidentGenerator incidentGenerator;
    private IncidentsUploader incidentsUploader;

    /**
     * Constructor, initializes the view and the components necessary to generate the incidents.
     *
     * @param activity Receives an AppCompacActivity to get the xml elements and initialize it.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public GenerateRandomIncidentController(AppCompatActivity activity) {
        generateRandomIncidentView = new GenerateRandomIncidentView(activity);
        Button generateButton = activity.findViewById(R.id.startGenerateIncidentButton);
        generateButton.setOnClickListener(v -> initGeneration());
        incidentGenerator = new IncidentGenerator();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initGeneration() {
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null).getCurrentLocationModel();
        Double[] referencePoint = {-66.17558339723521, -17.366337924269107};
        Radium radium = generateRandomIncidentView.getRadiumSelected();
        int quantity = generateRandomIncidentView.getNumberIncidentsSelected();
        if (radium != null && quantity != 0) {
            startGeneratingIncidents(referencePoint, radium, quantity);
        }
        generateRandomIncidentView.hideOptions();
    }

    /**
     * This method starts generating incidents within a specified radius around a reference point.
     *
     * @param referencePoint The reference point around which incidents will be generated.
     * @param radium The radius in which the incidents will be generated.
     * @param quantity The number of incidents to generate.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startGeneratingIncidents(Double[] referencePoint, Radium radium, int quantity) {
        String message;
        if (!incidentGenerator.withoutTimeout()) {
            Long minutesToWait = incidentGenerator.getMinutesBetweenTimeout();
            Long secondsToWait = incidentGenerator.getSecondsBetweenTimeout();
            message = "You have to wait " +
                    ((minutesToWait == 0) ? (secondsToWait + " seg ") : (minutesToWait + " min ")) +
                    "to generate incidents";
        } else if (quantity < 2 || quantity > 30) {
            message = "Invalid number of incidents to generate";
        } else {
            uploadToDataBase(incidentGenerator.getIncidentsRandomly(referencePoint, radium, quantity));
            message = quantity + " Incidents Successfully Generated";
        }

        showShortNotification(message);
    }

    /**
     * This method show a short notification on the mobile window.
     *
     * @param message is the message to show.
     */
    private void showShortNotification(String message) {
        Toast.makeText(MainActivity.context, message, Toast.LENGTH_SHORT).show();
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
            String jsonString = incidentsUploader.generateJsonIncident(
                    currentIncident.getType(),
                    currentIncident.getReason(),
                    dateCreated,
                    dateDeath,
                    currentIncident.getGeometry().getType(),
                    currentIncident.getGeometry().getStringCoordinates()
            );
            AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                @SuppressLint("StaticFieldLeak")
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
