package com.isc.hermes.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.isc.hermes.R;
/**
 * This class manages the navigation and functionality of the Incident View in the application.
 */
public class IncidentViewNavigation {
    private AppCompatActivity activity;
    private static IncidentViewNavigation instance;
    private ImageButton locationButton;
    private ConstraintLayout incidentView;
    private ConstraintLayout generateIncidentView;
    private ConstraintLayout displayIncidents;
    private boolean isOptionsVisible;

    /**
     * Constructs an IncidentViewNavigation object.
     * @param activity The activity in which the IncidentViewNavigation is used.
     */
    private IncidentViewNavigation(AppCompatActivity activity) {
        this.activity = activity;
        initializeViews();
        initLocationButtonFunctionality();
    }

    /**
     * Initializes the views used by the IncidentViewNavigation.
     */
    private void initializeViews() {
        locationButton = activity.findViewById(R.id.generateIncidentButton);
        incidentView = activity.findViewById(R.id.incident_view);
        generateIncidentView = activity.findViewById(R.id.GenerateIncidentView);
        displayIncidents = activity.findViewById(R.id.display_incidents);
    }
    /**
     * Initializes the functionality of the location button.
     *
     * Toggles the visibility of the options when the location button is clicked.
     */
    private void initLocationButtonFunctionality() {
        locationButton.setOnClickListener(v -> {
            if (!isOptionsVisible) {
                showOptions();
            } else {
                hideOptions();
            }
        });
    }
    /**
     * Shows the incident options.
     */
    private void showOptions() {
        incidentView.setVisibility(View.VISIBLE);
        isOptionsVisible = true;
    }
    /**
     * Hides the incident options.
     */
    private void hideOptions() {
        incidentView.setVisibility(View.GONE);
        generateIncidentView.setVisibility(View.GONE);
        displayIncidents.setVisibility(View.GONE);
        isOptionsVisible = false;
    }

    /**
     * Initializes the functionality of the incident button.
     *
     * @param button The incident button.
     * @param windows The layout that displays the incident windows.
     */
    public void initIncidentButtonFunctionality(Button button, ConstraintLayout windows) {
        button.setOnClickListener(v -> {
            windows.setVisibility(View.VISIBLE);
            incidentView.setVisibility(View.GONE);
            initLocationButtonFunctionality();
        });
    }
    /**
     * Returns the singleton instance of IncidentViewNavigation.
     *
     * @param activity The activity in which the IncidentViewNavigation is used.
     * @return The IncidentViewNavigation instance.
     */
    public static IncidentViewNavigation getInstance(AppCompatActivity activity) {
        if (instance == null || instance.isNewInstanceNeeded(activity)) {
            instance = new IncidentViewNavigation(activity);
        }
        return instance;
    }

    /**
     * This method checks if a new instance of IncidentViewNavigation should be created.
     *
     * @param appActivity The activity in which the IncidentViewNavigation is used.
     * @return True if a new instance should be created, false otherwise.
     */
    private boolean isNewInstanceNeeded(AppCompatActivity appActivity) {
        return activity == null || activity != appActivity;
    }
}
