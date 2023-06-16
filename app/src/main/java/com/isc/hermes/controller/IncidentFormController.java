package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;

import timber.log.Timber;

import java.net.HttpURLConnection;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class IncidentFormController {
    private final Context context;
    private final RelativeLayout incidentForm;
    private final Button cancelButton;
    private final Button acceptButton;
    private final LinearLayout incidentTypesContainer;
    private final TextView incidentText;
    private final TextInputLayout reasonTextField;
    public static String incidentType;
    private final MapWayPointController mapWayPointController;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public IncidentFormController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        this.mapWayPointController = mapWayPointController;
        incidentForm = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        incidentTypesContainer = ((AppCompatActivity) context).findViewById(R.id.incidentTypesContainer);
        incidentText = ((AppCompatActivity) context).findViewById(R.id.incident_text);
        reasonTextField = ((AppCompatActivity) context).findViewById(R.id.reason_text_field);

        setButtonsOnClick();
        setIncidentComponents();
    }

    /**
     * This method assigns functionality to the buttons of the view.
     */
    private void setButtonsOnClick() {
        cancelButton.setOnClickListener(v -> {
            handleCancelButtonClick();
        });

        acceptButton.setOnClickListener(v -> {
            handleAcceptButtonClick();
        });
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    private void handleCancelButtonClick() {
        mapWayPointController.setMarked(false);
        incidentForm.startAnimation(Animations.exitAnimation);
        incidentForm.setVisibility(View.GONE);
        mapWayPointController.deleteMarks();
    }

    /**
     * This method handles the actions performed when the accept button is clicked.
     */
    private void handleAcceptButtonClick() {
        handleCancelButtonClick();
        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return uploadIncidentDataBase();
            }

            @Override
            protected void onPostExecute(Integer responseCode) {
                handleUploadResponse(responseCode);
            }
        };
        task.execute();
    }
    /**
     * This method handles the response received after uploading the incident to the database.
     *
     * @param responseCode the response code received after uploading the incident
     */
    private void handleUploadResponse(Integer responseCode) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, R.string.incidents_uploaded, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.incidents_not_uploaded, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method assigns values to the incident components.
     *
     * <p>
     *     This method assign values and views to the incident components such as the incident type
     *     spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setIncidentComponents() {
        String[] incidentTypes = context.getResources().getStringArray(R.array.incidents_type);
        String[] incidentTypeColors = context.getResources().getStringArray(R.array.incidents_type_colors);
        String[] incidentTypeIcons = context.getResources().getStringArray(R.array.incidents_type_icons);

        if (incidentTypes.length == incidentTypeColors.length &&
                incidentTypeIcons.length == incidentTypes.length) {
            for (int i = 0; i < incidentTypes.length; i++) {
                IncidentTypeButton incidentTypeButton = new IncidentTypeButton();
                Button button = incidentTypeButton.getIncidentTypeButton(
                        context,
                        incidentTypes[i].toLowerCase(),
                        Color.parseColor((String) incidentTypeColors[i]),
                        incidentTypeIcons[i]);
                setIncidentButtonAction(button);

                incidentTypesContainer.addView(button);
            }
        } else {
            Timber.i("The arrays about colors, icons and the type buttons have a different size");
        }

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

    /**
     * This method change the title of the type incident, based in a string.
     * @param title new title
     */
    public void changeTypeTitle(String title) {
        incidentText.setText(title);
    }

    /**
     * This method set the event to the incident buttons.
     * @param typeButton button to set the event.
     */
    private void setIncidentButtonAction(Button typeButton) {
        typeButton.setOnClickListener(
                v -> {
                    IncidentFormController.incidentType = typeButton.getText().toString();
                    changeTypeTitle("Incident Type: " + typeButton.getText());
                }
        );
    }

    /**

     This method etrieves the selected incident type from the buttons.
     @return The selected incident type as a string.
     */
    private String getIncidentType(){
        return incidentType;
    }
    /**

     This method retrieves the selected incident time from the number picker and time spinner.
     @return The selected incident time as a  string.
     */
    private String getIncidentTime(){
        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        int selectedIncidentTime = incidentTimePicker.getValue();
        Spinner incidentTimeSpinner = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        String selectedIncidentTimeOption = incidentTimeSpinner.getSelectedItem().toString();
        return selectedIncidentTime+ " " + selectedIncidentTimeOption;
    }

    /**
     * This method is used to access to the text input for the user on the reason field.
     * @return reasonText
     */
    private String getReason() {
        return reasonTextField.getEditText().getText().toString();
    }

    /**

     This method uploads an incident to the database by generating the necessary data and invoking the appropriate methods.
     @return The HTTP response code indicating the status of the upload.
     */
    private int uploadIncidentDataBase(){
        String id = IncidentsUtils.getInstance().generateObjectId();
        String reason = getReason();
        String dateCreated = IncidentsUtils.getInstance().generateCurrentDateCreated();
        String deathDate = IncidentsUtils.getInstance().addTimeToCurrentDate(getIncidentTime());
        String coordinates = IncidentsUploader.getInstance().getCoordinates();
        String JsonString = IncidentsUploader.getInstance().generateJsonIncident(id, getIncidentType(), reason, dateCreated, deathDate ,coordinates);
        return IncidentsUploader.getInstance().uploadIncident(JsonString);
    }
}
