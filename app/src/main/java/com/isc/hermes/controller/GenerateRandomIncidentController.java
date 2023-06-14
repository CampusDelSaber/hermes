package com.isc.hermes.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.view.GenerateRandomIncident.GenerateIncidentView;

public class GenerateRandomIncidentController {

    private GenerateIncidentView incidentButton;

    public GenerateRandomIncidentController(AppCompatActivity activity){
        incidentButton = new GenerateIncidentView(activity);
        incidentButton.initIncidentGeneratorView();
    }
}
