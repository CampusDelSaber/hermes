package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.utils.Animations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class IncidentFormController {
    private final Context context;
    private final RelativeLayout incidentForm;
    private final Button cancelButton;
    private final Button acceptButton;
    private final MapController mapController;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapController Is the controller of the map.
     */
    public IncidentFormController(Context context, MapController mapController) {
        this.context = context;
        this.mapController = mapController;
        incidentForm = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        setButtonsOnClick();
        setIncidentComponents();
    }

    /**
     * Method to assign functionality to the buttons of the view.
     */
    private void setButtonsOnClick(){
        cancelButton.setOnClickListener(v -> {
            mapController.setMarked(false);
            incidentForm.startAnimation(Animations.exitAnimation);
            incidentForm.setVisibility(View.GONE);
            mapController.deleteMarks();
        });

        acceptButton.setOnClickListener(v -> {
            mapController.setMarked(false);
            incidentForm.startAnimation(Animations.exitAnimation);
            incidentForm.setVisibility(View.GONE);
            mapController.deleteMarks();
            Toast.makeText(context, "Incident Saved Correctly.", Toast.LENGTH_SHORT).show();

            // Start a background task to perform the network request
            @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    // JSON payload representing the incident
                    String jsonPayload = "{\"_id\": \"647f77f7ca71d0ca1049542f\",\"type\": \"Social-Event\",\"reason\": \"March.\",\"dateCreated\": \"2018-12-10T13:49:51.141Z\",\"deathDate\": \"2019-12-10T13:49:51.141Z\",\"geometry\": {\"type\": \"Point\",\"coordinates\": [12, 123]}}";
                    try {
                        URL url = new URL("https://api-rest-hermes.onrender.com/incidents");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // Set the necessary headers
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);

                        // Send the JSON payload
                        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                        outputStream.writeBytes(jsonPayload);
                        outputStream.flush();
                        outputStream.close();

                        // Get the response code
                        return connection.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }

                @Override
                protected void onPostExecute(Integer responseCode) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        System.out.println("Incident uploaded successfully.");
                        Toast.makeText(context, "GOOOD", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("FAILED" + responseCode);
                    }
                }
            };

            // Execute the task
            task.execute();
        });

    }

    /**
     * Method assign values to the incident components.
     *
     * <p>
     *     This method assign values and views to the incident components such as the incident type
     *     spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setIncidentComponents() {
        Spinner incidentType = ((AppCompatActivity) context).findViewById(R.id.incident_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, R.array.incidents_type, R.layout.incident_spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentType.setAdapter(adapter);

        Spinner incidentEstimatedTime = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        ArrayAdapter<CharSequence> adapterTime=ArrayAdapter.createFromResource(context, R.array.incidents_estimated_time, R.layout.incident_spinner_items);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentEstimatedTime.setAdapter(adapterTime);

        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        incidentTimePicker.setMinValue(1);
        incidentTimePicker.setMaxValue(10);
    }

    /**
     * This is a getter method to Incident form layout.
     * @return Return a layout.
     */
    public RelativeLayout getIncidentForm() {
        return incidentForm;
    }



    private String getIncidentType(){
        Spinner incidentTypeSpinner = ((AppCompatActivity) context).findViewById(R.id.incident_spinner);
        String selectedIncidentType = incidentTypeSpinner.getSelectedItem().toString();
        return selectedIncidentType;
    }
    private String getIncidentTime(){
        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        int selectedIncidentTime = incidentTimePicker.getValue();
        Spinner incidentTimeSpinner = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        String selectedIncidentTimeOption = incidentTimeSpinner.getSelectedItem().toString();
        return selectedIncidentTime+ " " + selectedIncidentTimeOption;
    }
    private void uploadIncidentDataBase(){
        String id = IncidentsUploader.getInstance().createRandomIncidentId();
        String longitud = IncidentsUploader.getInstance().getCurrentLongitude()+"";
        String latitud = IncidentsUploader.getInstance().getCurrentLatitude()+"";
        String JsonString = IncidentsUploader.getInstance().createJSONString("1ba","1ba","Reason", "getIncidentTime()","Point", "longitud", "latitud");
        IncidentsUploader.getInstance().uploadIncident(JsonString);
    }


}
