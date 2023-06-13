package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapClickEventsManager;

public class IncidentFormController {
    private final Context context;
    private final RelativeLayout incidentForm;
    private final Button cancelButton;
    private final Button acceptButton;
    private final MapController mapController;

    public IncidentFormController(Context context, MapController mapController) {
        this.context = context;
        this.mapController = mapController;
        incidentForm = ((AppCompatActivity)context).findViewById(R.id.incident_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_button);
        acceptButton = ((AppCompatActivity) context).findViewById(R.id.accept_button);
        setButtonsOnClick();
    }

    private void setButtonsOnClick(){
        cancelButton.setOnClickListener(v -> {
            MapClickEventsManager.isOnReportIncidentMode = false;
            mapController.setMarked(false);
            incidentForm.startAnimation(Animations.exitAnimation);
            incidentForm.setVisibility(View.GONE);
            mapController.deleteMarks();
        });

        acceptButton.setOnClickListener(v -> {
            MapClickEventsManager.isOnReportIncidentMode = false;
            mapController.setMarked(false);
            incidentForm.startAnimation(Animations.exitAnimation);
            incidentForm.setVisibility(View.GONE);
            mapController.deleteMarks();
            Toast.makeText(context, "Incident Saved Correctly.", Toast.LENGTH_SHORT).show();
        });
    }

    public RelativeLayout getIncidentForm() {
        return incidentForm;
    }
}
