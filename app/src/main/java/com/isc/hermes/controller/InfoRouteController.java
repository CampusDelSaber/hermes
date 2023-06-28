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


/**
 * The InfoRouteController class is responsible for managing the information related to routes,
 * such as distance and estimated time, and displaying it in the user interface.
 */
public class InfoRouteController {
    private ConstraintLayout layout;
    private boolean isActive;
    private Button cancelButton;
    private Button buttonRouteA;
    private Button buttonRouteB;
    private Button buttonRouteC;
    private Button startNavigationButton;
    private TextView timeText;
    private TextView distanceText;
    private static InfoRouteController instanceNavigationController;
    private ArrayList<Integer> colorsInfoRoutes;
    private MapPolyline mapPolyline;
   private ArrayList<JSONObject> jsonObjects;
   private NavigationOptionsController navigationOptionsController;
   private NavigationDirectionController navigationDirectionController;
    private boolean isRouteASelected, isRouteBSelected, isRouteCSelected;


    /**
     * Constructs a new InfoRouteController object.
     *
     * @param context The context of the activity.
     * @param navigationOptionsController route navigation's options controller
     */
    private InfoRouteController(Context context, NavigationOptionsController navigationOptionsController){
        layout = ((AppCompatActivity)context).findViewById(R.id.distance_time_view);
        cancelButton =  ((AppCompatActivity)context).findViewById(R.id.cancel_navigation_button);
        timeText = ((AppCompatActivity)context).findViewById(R.id.timeText);
        distanceText = ((AppCompatActivity)context).findViewById(R.id.distanceText);
        buttonRouteC = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteThree);
        buttonRouteB = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteTwo);
        buttonRouteA = ((AppCompatActivity)context).findViewById(R.id.ButtonRouteOne);
        startNavigationButton = ((AppCompatActivity)context).findViewById(R.id.startNavegationButton);
        navigationDirectionController = new NavigationDirectionController(context);
        this.navigationOptionsController = navigationOptionsController;
        colorsInfoRoutes = new ArrayList<>();
        isActive = false;
        isRouteASelected = false;
        isRouteBSelected = false;
        isRouteCSelected = false;
        colorsInfoRoutes.add(0XFF686C6C);
        colorsInfoRoutes.add(0xFF2350A3);
        colorsInfoRoutes.add(0XFFFF6E26);
        jsonObjects = new ArrayList<>();
        setActionButtons();
    }

    /**
     * Sets the action listeners for the buttons.
     */
    private void setActionButtons(){
        isActive = false;
        cancelButton.setOnClickListener(v -> {
            mapPolyline.hidePolylines();
            layout.startAnimation(Animations.exitAnimation);
            layout.setVisibility(View.GONE);
            navigationDirectionController.getDirectionsForm().startAnimation(Animations.exitAnimation);
            navigationDirectionController.getDirectionsForm().setVisibility(View.GONE);
            navigationOptionsController.getMapWayPointController().deleteMarks();
            isActive = false;
        });

        buttonRouteA.setOnClickListener(v -> {
            setTimeAndDistanceInformation(jsonObjects.get(2));
            isRouteASelected = true;
            isRouteBSelected = false;
            isRouteCSelected = false;
            updateButtonVisibility();
        });
        buttonRouteB.setOnClickListener(v -> {
            setTimeAndDistanceInformation(jsonObjects.get(1));
            isRouteASelected = false;
            isRouteBSelected = true;
            isRouteCSelected = false;
            updateButtonVisibility();
        });
        buttonRouteC.setOnClickListener(v -> {
            setTimeAndDistanceInformation(jsonObjects.get(0));
            isRouteASelected = false;
            isRouteBSelected = false;
            isRouteCSelected = true;
            updateButtonVisibility();
        });
        startNavigationButton.setOnClickListener(v -> {
            navigationDirectionController.getDirectionsForm().startAnimation(Animations.entryAnimation);
            navigationDirectionController.getDirectionsForm().setVisibility(View.VISIBLE);
        });
    }

    /**
     * This method sets the visibility of the button.
     */
    private void updateButtonVisibility() {
        buttonRouteA.setAlpha(isRouteASelected ? 1f : 0.3f);
        buttonRouteB.setAlpha(isRouteBSelected ? 1f : 0.3f);
        buttonRouteC.setAlpha(isRouteCSelected ? 1f : 0.3f);
    }

    /**
     * Retrieves an instance of InfoRouteController.
     *
     * @param context The context of the activity.
     * @return The InfoRouteController instance.
     */
    public static InfoRouteController getInstance(Context context, NavigationOptionsController navigationOptionsController){
        if(instanceNavigationController == null){
            instanceNavigationController = new InfoRouteController(context, navigationOptionsController);
        }
        return instanceNavigationController;
    }

    /**
     * Shows the route information view, including distance and time.
     *
     * @param jsonCoordinates The list of JSON coordinates representing the routes.
     * @param mapPolyline The MapPolyline object for displaying the routes.
     */
    public void showInfoRoute(List<String> jsonCoordinates, MapPolyline mapPolyline){
        this.mapPolyline = mapPolyline;
        isActive = true;
        layout.setVisibility(View.VISIBLE);
        jsonObjects = new ArrayList<>();
        try {
            for(String currentJson : jsonCoordinates)
                jsonObjects.add(new JSONObject(currentJson));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        setTimeAndDistanceInformation(jsonObjects.get(2));
        mapPolyline.displaySavedCoordinates(jsonCoordinates, colorsInfoRoutes);
    }

    /**
     * Retrieves the layout of the route information view.
     *
     * @return The ConstraintLayout object representing the layout.
     */
    public ConstraintLayout getLayout() {
        return layout;
    }

    /**
     * Checks if the route information view is active.
     *
     * @return true if the view is active, false otherwise.
     */
    public boolean isActive(){
        return isActive;
    }

    /**
     * Sets the distance and time information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the distance and time information.
     */
    private void setTimeAndDistanceInformation(JSONObject jsonObject){
        setDistanceInfo(jsonObject);
        setEstimatedTimeInfo(jsonObject);
        navigationDirectionController.processRoute(jsonObject);
    }

    /**
     * Sets the distance information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the distance information.
     */
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

    /**
     * Sets the estimated time information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the estimated time information.
     */
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
