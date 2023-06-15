package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapClickEventsManager;

public class PolygonOptionsController {
    private final RelativeLayout polygonOptions;
    private final Button submitDisasterButton;
    private final Button cancelSubmitButton;
    private final Context context;

    public PolygonOptionsController(Context context, MapPolygonController polygonController){
        this.context = context;
        polygonOptions = ((AppCompatActivity) context).findViewById(R.id.natural_disaster_form);
        submitDisasterButton = ((AppCompatActivity) context).findViewById(R.id.submit_natural_disaster);
        cancelSubmitButton = ((AppCompatActivity) context).findViewById(R.id.cancel_submit_natural_disaster);
        setButtonsOnClick();
        polygonController.deleteMarks();
    }

    public void displayComponents(){
        polygonOptions.startAnimation(Animations.entryAnimation);
        polygonOptions.setVisibility(View.VISIBLE);
    }

    private void setButtonsOnClick() {
        cancelSubmitButton.setOnClickListener(v->{
            handleCancelButtonClick();
        });
        submitDisasterButton.setOnClickListener(v->{
            handleAcceptButtonClick();
        });
    }

    private void handleCancelButtonClick() {
        polygonOptions.startAnimation(Animations.exitAnimation);
        polygonOptions.setVisibility(View.GONE);
        resetMapConfiguration();
    }

    private void handleAcceptButtonClick(){
        handleCancelButtonClick();
    }

    private void resetMapConfiguration(){
        MapClickEventsManager.getInstance().removeCurrentClickController();
        MapClickEventsManager.getInstance().setMapClickConfiguration(new MapWayPointController(MapClickEventsManager.getInstance().getMapboxMap(), this.context));
        MapClickEventsManager.getInstance().getMapboxMap().setStyle(MapClickEventsManager.getInstance().getMapboxMap().getStyle().getUri());
    }
}
