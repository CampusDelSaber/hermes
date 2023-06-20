package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
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
import com.isc.hermes.model.incidents.GeometryType;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;
import java.net.HttpURLConnection;
import java.util.Objects;
import timber.log.Timber;


public class NavigationOptionsFormController {
    private final Context context;
    private final RelativeLayout navOptionsForm;
    private final Button cancelButton;
    private final Button startButton;
    private final LinearLayout transportationTypesContainer;
    private final TextView navOptionsText;
    private final TextInputLayout chooseTextField;
    public static String incidentType;
    private final MapWayPointController mapWayPointController;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public NavigationOptionsFormController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        this.mapWayPointController = mapWayPointController;
        navOptionsForm = ((AppCompatActivity)context).findViewById(R.id.navOptions_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_navOptions_button);
        startButton = ((AppCompatActivity) context).findViewById(R.id.start_button);
        transportationTypesContainer = ((AppCompatActivity) context).findViewById(R.id.transportationTypesContainer);
        navOptionsText = ((AppCompatActivity) context).findViewById(R.id.navOptions_Text);
        chooseTextField = ((AppCompatActivity) context).findViewById(R.id.choose_text_field);
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

        startButton.setOnClickListener(v -> {
            handleAcceptButtonClick();
        });
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    private void handleCancelButtonClick() {
        mapWayPointController.setMarked(false);
        navOptionsForm.startAnimation(Animations.exitAnimation);
        navOptionsForm.setVisibility(View.GONE);
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
        String[] navOptionsTypes = context.getResources().getStringArray(R.array.navOptions_type);
        String[] navOptionTypeColors = context.getResources().getStringArray(R.array.navOptions_type_colors);
        String[] navOptionTypeIcons = context.getResources().getStringArray(R.array.navOptions_type_icons);

        if (navOptionsTypes.length == navOptionTypeColors.length &&
                navOptionTypeIcons.length == navOptionsTypes.length) {
            for (int i = 0; i < navOptionsTypes.length; i++) {
                Button button = IncidentTypeButton.getIncidentTypeButton(
                        context,
                        navOptionsTypes[i].toLowerCase(),
                        Color.parseColor((String) navOptionTypeColors[i]),
                        navOptionTypeIcons[i]);
                setIncidentButtonAction(button);
                transportationTypesContainer.addView(button);
            }
        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }
    }


    /**
     * This is a getter method to Incident form layout.
     * @return Return a layout.
     */
    public RelativeLayout getNavOptionsForm() {
        return navOptionsForm;
    }

    /**
     * This method change the title of the type incident, based in a string.
     * @param title new title
     */
    public void changeTypeTitle(String title) {
        navOptionsText.setText(title);
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
        return chooseTextField.getEditText().getText().toString();
    }

    /**

     This method uploads an incident to the database by generating the necessary data and invoking the appropriate methods.
     @return The HTTP response code indicating the status of the upload.
     */
    private int uploadIncidentDataBase(){
        String id = IncidentsUtils.getInstance().generateObjectId();
        String dateCreated = IncidentsUtils.getInstance().generateCurrentDateCreated();
        String deathDate = IncidentsUtils.getInstance().addTimeToCurrentDate(getIncidentTime());
        String coordinates = IncidentsUploader.getInstance().getCoordinates();
        String JsonString = IncidentsUploader.getInstance().generateJsonIncident(id,getIncidentType(),"Reason",dateCreated, deathDate , GeometryType.POINT.getName(),coordinates);
        return IncidentsUploader.getInstance().uploadIncident(JsonString);
    }

    /**
     * This method clear the fields of the form.
     */
    private void clearForm() {
        incidentType = null;
        changeTypeTitle("Incident Type: ");
        Objects.requireNonNull(chooseTextField.getEditText()).setText("");
    }
}
