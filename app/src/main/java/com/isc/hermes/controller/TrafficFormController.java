package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class TrafficFormController {
    private final Context context;
    private final RelativeLayout incidentForm;
    private final Button cancelButton;
    private final Button acceptButton;
    private final MapController mapController;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context       Is the context application.
     * @param mapController Is the controller of the map.
     */
    public TrafficFormController(Context context, MapController mapController) {
        this.context = context;
        this.mapController = mapController;
        incidentForm = ((AppCompatActivity) context).findViewById(R.id.traffic_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        setButtonsOnClick();
        setTrafficComponents();
    }


    /**
     * Method to assign functionality to the buttons of the view.
     */
    private void setButtonsOnClick() {
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
            Toast.makeText(context, "Traffic Saved Correctly.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Method assign values to the incident components.
     *
     * <p>
     * This method assign values and views to the incident components such as the incident type
     * spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setTrafficComponents() {
        Spinner incidentType = ((AppCompatActivity) context).findViewById(R.id.traffic_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.traffic_level, R.layout.incident_spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentType.setAdapter(adapter);

        Spinner incidentEstimatedTime = ((AppCompatActivity) context).findViewById(R.id.incident_time_spinner);
        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(context, R.array.traffic_estimated_time, R.layout.incident_spinner_items);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_item);
        incidentEstimatedTime.setAdapter(adapterTime);

        NumberPicker incidentTimePicker = ((AppCompatActivity) context).findViewById(R.id.numberPicker);
        incidentTimePicker.setMinValue(1);
        incidentTimePicker.setMaxValue(100);
    }

    /**
     * This is a getter method to Incident form layout.
     *
     * @return Return a layout.
     */
    public RelativeLayout getTrafficForm() {
        return incidentForm;
    }
}
