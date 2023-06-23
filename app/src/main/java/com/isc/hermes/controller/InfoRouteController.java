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

import java.util.ArrayList;
import java.util.List;

public class InfoRouteController {
    private ConstraintLayout layout;
    private boolean isActive;
    private Button cancelButton;
    private static InfoRouteController instanceNavigationController;
    ArrayList<Integer> colorsInfoRoutes = new ArrayList<>();
    ArrayList<String> geoJsonRoutes;
    MapPolyline mapPolyline;

    private InfoRouteController(Context context){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
        cancelButton =  ((AppCompatActivity)context).findViewById(R.id.cancel_navigation_button);
        isActive = false;
        colorsInfoRoutes.add(0xFF2867DC);
        colorsInfoRoutes.add(0XFFC5D9FD);
        colorsInfoRoutes.add(0XFFC5D9FD);
        setActionButtons();
    }

    private void setActionButtons(){
        isActive = false;
        cancelButton.setOnClickListener(v -> {
            mapPolyline.hidePolylines();
            layout.startAnimation(Animations.exitAnimation);
            layout.setVisibility(View.GONE);
            isActive = false;
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

    public void showInfoRoute(List<String> jsonCoordinates, MapPolyline mapPolyline){
        geoJsonRoutes = (ArrayList<String>) jsonCoordinates;
        this.mapPolyline = mapPolyline;
        isActive = true;
        layout.setVisibility(View.VISIBLE);
        mapPolyline.displaySavedCoordinates(jsonCoordinates, colorsInfoRoutes);
    }
    public ConstraintLayout getLayout() {
        return layout;
    }

    public boolean isActive(){
        return isActive;
    }
}
