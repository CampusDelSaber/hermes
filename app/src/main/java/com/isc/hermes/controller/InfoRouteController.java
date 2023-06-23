package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.isc.hermes.R;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.utils.Animations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InfoRouteController {
    private ConstraintLayout layout;
    private boolean isActive;
    private Button cancelButton;
    private Button buttonRouteA;
    private Button buttonRouteB;
    private Button buttonRouteC;
    private TextView timeText;
    private TextView distanceText;
    private static InfoRouteController instanceNavigationController;ArrayList<Integer> colorsInfoRoutes = new ArrayList<>();
    ArrayList<String> geoJsonRoutes;
    MapPolyline mapPolyline;
    ArrayList<JSONObject> jsonObjects;

    private InfoRouteController(Context context){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
        cancelButton =  ((AppCompatActivity)context).findViewById(R.id.cancel_navigation_button);
        timeText = ((AppCompatActivity)context).findViewById(R.id.timeText);
        distanceText = ((AppCompatActivity)context).findViewById(R.id.distanceText);
        buttonRouteA = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteOne);
        buttonRouteB = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteTwo);
        buttonRouteC = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteThree);
        isActive = false;
        colorsInfoRoutes.add(0XFFFF6E26);
        colorsInfoRoutes.add(0xFF2867DC);
        colorsInfoRoutes.add(0XFFC5D9FD);
        jsonObjects = new ArrayList<>();
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

        buttonRouteA.setOnClickListener(v -> setTimeAndDistanceInformation(jsonObjects.get(0)));
        buttonRouteB.setOnClickListener(v -> setTimeAndDistanceInformation(jsonObjects.get(1)));
        buttonRouteC.setOnClickListener(v -> setTimeAndDistanceInformation(jsonObjects.get(2)));
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
        jsonObjects = new ArrayList<>();
        try {
            for(String currentJson : jsonCoordinates){
                jsonObjects.add(new JSONObject(currentJson));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        setTimeAndDistanceInformation(jsonObjects.get(0));
        mapPolyline.displaySavedCoordinates(jsonCoordinates, colorsInfoRoutes);
    }
    public ConstraintLayout getLayout() {
        return layout;
    }

    public boolean isActive(){
        return isActive;
    }

    private void setTimeAndDistanceInformation(JSONObject jsonObject){
        setDistanceInfo(jsonObject);
        setEstimatedTimeInfo(jsonObject);
    }

    private void setDistanceInfo(JSONObject jsonObject){
        try {
            double meters = jsonObject.getDouble("distance");
            double kilometers = 0;
            while( meters - 1 > 0){
                meters -= 1;
                kilometers ++;
            }

            int decimals = 3;
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimals));
            if(kilometers > 0) distanceText.setText(
                kilometers+ " km " + decimalFormat.format(meters).substring(1)+ " m");
            else distanceText.setText(decimalFormat.format(meters).substring(1)+ " m.");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setEstimatedTimeInfo(JSONObject jsonObject){
        try {
            int timeInMinutes = jsonObject.getInt("time");
            int hours = 0;
            while(timeInMinutes - 60 > 0){
                timeInMinutes -= 60;
                hours++;
            }
            if(hours > 0) timeText.setText(hours+" h "+ timeInMinutes+" min" );
            else timeText.setText(timeInMinutes+" min" );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
