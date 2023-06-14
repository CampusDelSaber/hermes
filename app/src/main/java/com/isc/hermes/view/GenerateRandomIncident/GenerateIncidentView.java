package com.isc.hermes.view.GenerateRandomIncident;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.model.CurrentLocationModel;

public class GenerateIncidentView {
    private AppCompatActivity activity;
    private RelativeLayout relativeLayout;



    public GenerateIncidentView(AppCompatActivity activity){
        this.activity = activity;

    }

    public void initIncidentGeneratorView() {
        ImageButton locationButton = activity.findViewById(R.id.generateIncidentButton);
        CurrentLocationModel currentLocation = CurrentLocationController.getCurrentLocationModel();
        locationButton.setOnClickListener(v -> // Imprimir un log con nivel de información
                Log.i("INFOMAU", currentLocation.getLatitude()+" , "+currentLocation.getLongitude()));
    }


}
