package com.isc.hermes.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.isc.hermes.R;
import android.view.MotionEvent;

public class IncidentView {
    private AppCompatActivity activity;
    private ImageButton locationButton;
    private ConstraintLayout incidentView;
    private ConstraintLayout generateIncidentView;
    private ConstraintLayout displayIncidents;

    private Button generateIncidentButton;
    private Button displayIncidentsButton;

    private boolean isOptionsVisible;

    public IncidentView(AppCompatActivity activity){
        this.activity = activity;
        initializeViews();
        initLocationButtonFunctionality();
        initGenerateIncidentButtonFunctionality();
        initDisplayIncidentsButtonFunctionality();
    }

    private void initializeViews() {
        locationButton = activity.findViewById(R.id.generateIncidentButton);
        incidentView = activity.findViewById(R.id.incident_view);
        generateIncidentView = activity.findViewById(R.id.GenerateIncidentView);

        generateIncidentView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && isOptionsVisible) {
                hideOptions();
                return true; // Indica que el evento fue manejado
            }
            return false; // Indica que el evento no fue manejado
        });

        displayIncidents = activity.findViewById(R.id.display_incidents);

        generateIncidentButton = activity.findViewById(R.id.generateIncidentsButton);
        displayIncidentsButton = activity.findViewById(R.id.displayIncidentsButton);
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

    private void initGenerateIncidentButtonFunctionality() {
        generateIncidentButton.setOnClickListener(v -> {
            generateIncidentView.setVisibility(View.VISIBLE);
            incidentView.setVisibility(View.GONE);
            initLocationButtonFunctionality();
        });
    }

    private void initDisplayIncidentsButtonFunctionality() {
        displayIncidentsButton.setOnClickListener(v -> {
            displayIncidents.setVisibility(View.VISIBLE);
            incidentView.setVisibility(View.GONE);
            initLocationButtonFunctionality();
        });
    }
}
