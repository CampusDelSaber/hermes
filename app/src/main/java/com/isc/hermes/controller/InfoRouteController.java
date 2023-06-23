package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.JsonArray;
import com.isc.hermes.R;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.utils.Animations;

public class InfoRouteController {
    private ConstraintLayout layout;
    private boolean isActive;
    private Button cancelButton;
    private static InfoRouteController instanceNavigationController;
    private InfoRouteController(Context context){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
        cancelButton =  ((AppCompatActivity)context).findViewById(R.id.cancel_navigation_button);
        isActive = false;
        setActionButtons();
    }

    private void setActionButtons(){
        isActive = false;
        cancelButton.startAnimation(Animations.exitAnimation);
        cancelButton.setOnClickListener(v -> {
            layout.setVisibility(View.GONE);

        });

    }

    public void showDistanceAndTimeView(){
        isActive = true;
        layout.setVisibility(View.VISIBLE);
    }

    public static InfoRouteController getInstance(Context context){
        if(instanceNavigationController == null){
            instanceNavigationController = new InfoRouteController(context);
        }
        return instanceNavigationController;
    }

    public void showInfoRoute(MapPolyline mapPolyline, String jsonArray){
        isActive = true;
        layout.setVisibility(View.VISIBLE);
        mapPolyline.displaySavedCoordinates(jsonArray, Color.BLUE);
    }
    public ConstraintLayout getLayout() {
        return layout;
    }

    public boolean isActive(){
        return isActive;
    }
}
