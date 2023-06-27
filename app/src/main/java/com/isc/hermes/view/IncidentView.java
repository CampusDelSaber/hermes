package com.isc.hermes.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.isc.hermes.R;

public class IncidentView {
    private AppCompatActivity activity;
    private static IncidentView instance;
    private ImageButton locationButton;
    private ConstraintLayout incidentView;
    private ConstraintLayout generateIncidentView;
    private ConstraintLayout displayIncidents;
    private boolean isOptionsVisible;

    private IncidentView(AppCompatActivity activity) {
        this.activity = activity;
        initializeViews();
        initLocationButtonFunctionality();
    }

    private void initializeViews() {
        locationButton = activity.findViewById(R.id.generateIncidentButton);
        incidentView = activity.findViewById(R.id.incident_view);
        generateIncidentView = activity.findViewById(R.id.GenerateIncidentView);
        displayIncidents = activity.findViewById(R.id.display_incidents);
    }

    private void initLocationButtonFunctionality() {
        locationButton.setOnClickListener(v -> {
            if (!isOptionsVisible) {
                showOptions();
            } else {
                hideOptions();
            }
        });
    }

    private void showOptions() {
        incidentView.setVisibility(View.VISIBLE);
        isOptionsVisible = true;
    }

    private void hideOptions() {
        incidentView.setVisibility(View.GONE);
        generateIncidentView.setVisibility(View.GONE);
        displayIncidents.setVisibility(View.GONE);
        isOptionsVisible = false;
    }

    public void initIncidentButtonFunctionality(Button button, ConstraintLayout windows) {
        button.setOnClickListener(v -> {
            windows.setVisibility(View.VISIBLE);
            incidentView.setVisibility(View.GONE);
            initLocationButtonFunctionality();
        });
    }
    public static IncidentView getInstance(AppCompatActivity activity) {
        if (instance == null) {
            instance = new IncidentView(activity);
        }
        return instance;
    }
}
