package com.isc.hermes.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.isc.hermes.R;
import com.isc.hermes.controller.PopUp.DialogListener;
import com.isc.hermes.controller.PopUp.PopUpConfirmIncidentCanceling;
import com.isc.hermes.controller.PopUp.PopUpWarningUpdateUserType;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.GeometryType;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.regex.InputValidator;
import com.isc.hermes.view.IncidentTypeButton;

import timber.log.Timber;

import java.net.HttpURLConnection;
import java.util.Objects;

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
    private String incidentTypeReported;
    private final MapWayPointController mapWayPointController;
    private static IncidentFormController instance;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    private IncidentFormController(Context context, MapWayPointController mapWayPointController) {
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
        initializeDefaultIncidentType();
    }

    public MapWayPointController getMapController() {
        return mapWayPointController;
    }

    /**
     * This method assigns functionality to the buttons of the view.
     */
    private void setButtonsOnClick() {
        cancelButton.setOnClickListener(v -> {
            new PopUpConfirmIncidentCanceling((AppCompatActivity) context).show();
            hideKeyboard(v);
        });

        acceptButton.setOnClickListener(v -> {
            if (validateDescription()){
                handleAcceptButtonClick();
                hideKeyboard(v);
            }
        });
    }

    /**
     * This method validates the incident form description input
     *
     * @return if the input is correct or not
     */
    private boolean validateDescription(){
        String description = reasonTextField.getEditText().getText().toString();
        return IncidentDialogController.getInstance(context).validateInput(description);
    }

    /**
     * This method hides the keyboard
     *
     * @param view View class
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    public void handleCancelButtonClick() {
        mapWayPointController.setMarked(false);
        incidentForm.startAnimation(Animations.exitAnimation);
        incidentForm.setVisibility(View.GONE);
        mapWayPointController.deleteLastMark();

    }


    /**
     * This method handles the actions performed when the accept button is clicked.
     */
    private void handleAcceptButtonClick() {
        IncidentsUploader uploader = new IncidentsUploader();
        String reasonInserted = reasonTextField.getEditText().getText().toString();
        String reasonSelected = reasonTextField.equals("")? reasonInserted: incidentTypeReported;
        uploader.uploadIncident(uploader.generateJsonIncident(
                incidentTypeReported,reasonSelected,
                IncidentsUtils.getInstance().generateCurrentDateCreated(),
                IncidentsUtils.getInstance().addTimeToCurrentDate(getIncidentTime()),
                "Point",
                IncidentsUploader.getInstance().getCoordinates()));
        handleCancelButtonClick();
    }
    /**
     * This method handles the response received after uploading the incident to the database.
     *
     * @param responseCode the response code received after uploading the incident
     */
    private void handleUploadResponse(Integer responseCode) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, R.string.incidents_uploaded, Toast.LENGTH_SHORT).show();
            clearForm();
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
        incidentTypesContainer.removeAllViews();
        String[] incidentTypes = context.getResources().getStringArray(R.array.incidents_type);
        String[] incidentTypeColors = context.getResources().getStringArray(R.array.incidents_type_colors);
        String[] incidentTypeIcons = context.getResources().getStringArray(R.array.incidents_type_icons);

        if (incidentTypes.length == incidentTypeColors.length &&
                incidentTypeIcons.length == incidentTypes.length) {
            for (int i = 0; i < incidentTypes.length; i++) {
                Button button = IncidentTypeButton.getIncidentTypeButton(
                        context,
                        incidentTypes[i].toLowerCase(),
                        Color.parseColor((String) incidentTypeColors[i]),
                        incidentTypeIcons[i]);
                setIncidentButtonAction(button, i);
                incidentTypesContainer.addView(button);
            }

        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }

        setEstimatedTimePicker();
    }

    /**
     * This method sets the default option for incident type.
     */
    private void initializeDefaultIncidentType(){
        for (int i = 0; i < incidentTypesContainer.getChildCount(); i++) {
            Button button = (Button) incidentTypesContainer.getChildAt(i);
            if(i == 0 ){
                incidentTypeReported = button.getText().toString();
                IncidentFormController.incidentType = button.getText().toString();
                changeTypeTitle("Point incident type: " + button.getText());
            }
            if (i != 0) button.setAlpha(0.3f);
        }
    }

    /**
     * This method set the content of the time picker,
     * and set the number picker according the kind of time.
     */
    private void setEstimatedTimePicker() {
        Spinner incidentEstimatedTime = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        ArrayAdapter<CharSequence> adapterTime=ArrayAdapter.createFromResource(context, R.array.incidents_estimated_time, R.layout.incident_spinner_items);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_item);

        incidentEstimatedTime.setAdapter(adapterTime);
        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        incidentTimePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            incidentTimePicker.setTextColor(Color.BLACK);
        }

        setTimeSelectedItem(incidentEstimatedTime, adapterTime, incidentTimePicker);
    }

    private void setTimeSelectedItem(Spinner incidentEstimatedTime,
                          ArrayAdapter<CharSequence> adapterTime,
                          NumberPicker incidentTimePicker) {
        incidentEstimatedTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = adapterTime.getItem(position).toString();

                switch (selectedOption) {
                    case "min" -> {
                        incidentTimePicker.setMinValue(1);
                        incidentTimePicker.setMaxValue(60);
                        break;
                    }
                    case "hr" -> {
                        incidentTimePicker.setMinValue(1);
                        incidentTimePicker.setMaxValue(24);
                        break;
                    }
                    case "day" -> {
                        incidentTimePicker.setMinValue(1);
                        incidentTimePicker.setMaxValue(31);
                        break;
                    }
                    case "month" -> {
                        incidentTimePicker.setMinValue(1);
                        incidentTimePicker.setMaxValue(12);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                incidentTimePicker.setMinValue(1);
                incidentTimePicker.setMaxValue(10);
            }
        });
    }

    /**
     * This is a getter method to PointIncidet form layout.
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
     * @param numberButton number button in the incidentTypesContainer.
     */
    private void setIncidentButtonAction(Button typeButton, int numberButton) {
        typeButton.setOnClickListener(
                v -> {
                    incidentTypeReported = typeButton.getText().toString();
                    IncidentFormController.incidentType = typeButton.getText().toString();
                    changeTypeTitle("Point incident type: " + typeButton.getText());
                    typeButton.setAlpha(1.0f);
                    for (int i = 0; i < incidentTypesContainer.getChildCount(); i++) {
                        Button button = (Button) incidentTypesContainer.getChildAt(i);
                        if (i != numberButton) button.setAlpha(0.3f);
                    }
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
        String JsonString = IncidentsUploader.getInstance().generateJsonIncident(getIncidentType(),reason,dateCreated, deathDate ,GeometryType.POINT.getName(),coordinates);        return IncidentsUploader.getInstance().uploadIncident(JsonString);
    }

    /**
     * This method clear the fields of the form.
     */
    private void clearForm() {
        incidentType = null;
        changeTypeTitle("PointIncidet Type: ");
        Objects.requireNonNull(reasonTextField.getEditText()).setText("");
    }

    /**
     * This method returns this instance class.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     * @return The instance of this class.
     */
    public static IncidentFormController getInstance(Context context, MapWayPointController mapWayPointController){
        if (instance == null || instance.context == null|| instance.context!= context){
            instance = new IncidentFormController(context, mapWayPointController);
        }
        return instance;
    }
}
