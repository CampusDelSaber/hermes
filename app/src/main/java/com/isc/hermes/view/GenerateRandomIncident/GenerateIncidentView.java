package com.isc.hermes.view.GenerateRandomIncident;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;

public class GenerateIncidentView {
    private AppCompatActivity activity;
    private RelativeLayout relativeLayout;



    public GenerateIncidentView(AppCompatActivity activity){
        this.activity = activity;

    }

    public void initIncidentGeneratorView() {
        ImageButton locationButton = activity.findViewById(R.id.generateIncidentButton);
        locationButton.setOnClickListener(v -> // Imprimir un log con nivel de información
                Log.i("INFOMAU", "Este es un mensaje de log de nivel información ---- pppp"));

        //relativeLayout = activity.findViewById(R.id.generateIncidentButton);
        //relativeLayout.setVisibility(View.GONE);
        //NumberPicker numberPicker = activity.findViewById(R.id.numberIncidentsPicker);
        //numberPicker.setMinValue(1);
        //numberPicker.setMaxValue(10);
    }


}
