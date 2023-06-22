package com.isc.hermes.controller;

import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;

public class ViewIncidentsController extends AppCompatActivity {
    private final LinearLayout incidentsVisualizationForm;
    private  Button viewIncidentsFormButton;
    private  Button viewTrafficButton;
    private  Button viewSocialEventButton;
    private  Button viewNaturalDisasterButton;

    public ViewIncidentsController(){
        this.incidentsVisualizationForm = findViewById(R.id.viewIncidentsForm);

    }
}
