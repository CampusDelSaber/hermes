package com.isc.hermes.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private static InfoRouteController instanceNavigationController;
    private ConstraintLayout layout;
    private boolean isActive;
    private Button cancelButton;
    private Button buttonRouteA;
    private Button buttonRouteB;
    private Button buttonRouteC;
    private Button startNavigationButton;
    private TextView timeText;
    private TextView distanceText;
    private ArrayList<Integer> colorsInfoRoutes;
    private MapPolyline mapPolyline;
    private ArrayList<JSONObject> jsonObjects;
    private NavigationOptionsController navigationOptionsController;
    private NavigationDirectionController navigationDirectionController;
    private boolean isRouteASelected, isRouteBSelected, isRouteCSelected;
    private int elapsedSeconds;
    private int timeEstimate;
    private String routes;

    /**
     * Constructs a new InfoRouteController object.
     *
     * @param context                     The context of the activity.
     * @param navigationOptionsController route navigation's options controller
     */
    private InfoRouteController(Context context, NavigationOptionsController navigationOptionsController) {
        setViewComponents(context);
        navigationDirectionController = new NavigationDirectionController(context);
        this.navigationOptionsController = navigationOptionsController;
        colorsInfoRoutes = new ArrayList<>();
        isActive = false;
        isRouteASelected = false;
        isRouteBSelected = false;
        isRouteCSelected = false;
        jsonObjects = new ArrayList<>();
        setActionButtons();
    }
    

    /**
     * Retrieves an instance of InfoRouteController.
     *
     * @param context The context of the activity.
     * @return The InfoRouteController instance.
     */
    public static InfoRouteController getInstance(Context context, NavigationOptionsController navigationOptionsController) {
        if (instanceNavigationController == null) {
            instanceNavigationController = new InfoRouteController(context, navigationOptionsController);
        }
        return instanceNavigationController;
    }

    /**
     * This method set the view of the components.
     *
     * @param context is the contex.
     */
    private void setViewComponents(Context context) {
        Activity activity = ((AppCompatActivity) context);
        layout = activity.findViewById(R.id.distance_time_view);
        cancelButton = activity.findViewById(R.id.cancel_navigation_button);
        timeText = activity.findViewById(R.id.timeText);
        distanceText = activity.findViewById(R.id.distanceText);
        buttonRouteC = activity.findViewById(R.id.ButtonRouteThree);
        buttonRouteB = activity.findViewById(R.id.ButtonRouteTwo);
        buttonRouteA = activity.findViewById(R.id.ButtonRouteOne);
        buttonRouteB.setAlpha(0.3f);
        buttonRouteC.setAlpha(0.3f);
        startNavigationButton = activity.findViewById(R.id.startNavegationButton);
    }

    /**
     * This method set the routes' color
     *
     * @param size is the size of the route.
     */
    private void setColorsInfoRoutes(int size) {
        colorsInfoRoutes.clear();
        colorsInfoRoutes.add(size > 1 ? 0XFF686C6C : 0XFFFF6E26);
        colorsInfoRoutes.add(0xFF2350A3);
        colorsInfoRoutes.add(size > 1 ? 0XFFFF6E26 : 0XFF686C6C);
    }

    public MapPolyline getMapPolyline(){
        return mapPolyline;
    }

    /**
     * Sets the action listeners for the buttons.
     */
    private void setActionButtons() {
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

        buttonRouteA.setOnClickListener(v -> setRouteInformation(jsonObjects.size() - 1,
                true, false, false));
        buttonRouteB.setOnClickListener(v -> setRouteInformation(1,
                false, true, false));
        buttonRouteC.setOnClickListener(v -> setRouteInformation(0,
                false, false, true));

        startNavigationButton.setOnClickListener(v -> {

            long startTime = System.currentTimeMillis();

            navigationDirectionController.getDirectionsForm().startAnimation(Animations.entryAnimation);
            navigationDirectionController.getDirectionsForm().setVisibility(View.VISIBLE);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            int elapsedSeconds2 = (int) (elapsedTime / 1000);
            setElapsedSeconds(elapsedSeconds2);
        });
    }



    /**
     * This method shows which route is selected
     */
    private void setRouteInformation(int index, boolean isRouteASelected, boolean isRouteBSelected, boolean isRouteCSelected) {
        setTimeAndDistanceInformation(jsonObjects.get(index));
        this.isRouteASelected = isRouteASelected;
        this.isRouteBSelected = isRouteBSelected;
        this.isRouteCSelected = isRouteCSelected;
        updateButtonVisibility();
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
     * Shows the route information view, including distance and time.
     *
     * @param jsonCoordinates The list of JSON coordinates representing the routes.
     * @param mapPolyline     The MapPolyline object for displaying the routes.
     */
    public void showInfoRoute(List<String> jsonCoordinates, MapPolyline mapPolyline) {
        this.mapPolyline = mapPolyline;
        isActive = true;
        layout.setVisibility(View.VISIBLE);
        jsonObjects = new ArrayList<>();
        try {
            for (String currentJson : jsonCoordinates)
                jsonObjects.add(new JSONObject(currentJson));
            setColorsInfoRoutes(jsonCoordinates.size());
            setTimeAndDistanceInformation(jsonObjects.get(jsonObjects.size() - 1));
            mapPolyline.displaySavedCoordinates(jsonCoordinates, colorsInfoRoutes);

            setButtonsVisibility();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will set the buttons visibility depending on the routes size
     */
    private void setButtonsVisibility() {
        if (jsonObjects.size() < 2) {
            buttonRouteB.setVisibility(View.GONE);
        } else buttonRouteB.setVisibility(View.VISIBLE);
        if (jsonObjects.size() < 2)
            buttonRouteC.setVisibility(View.GONE);
        else buttonRouteC.setVisibility(View.VISIBLE);
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
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the distance and time information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the distance and time information.
     */
    private void setTimeAndDistanceInformation(JSONObject jsonObject) {
        setDistanceInfo(jsonObject);
        setEstimatedTimeInfo(jsonObject);
        navigationDirectionController.processRoute(jsonObject);
    }

    /**
     * Sets the distance information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the distance information.
     */
    private void setDistanceInfo(JSONObject jsonObject) {
        try {
            double meters = jsonObject.getDouble("distance");
            double kilometers = 0;
            while (meters - 1 > 0) {
                meters -= 1;
                kilometers++;
            }
            int decimals = 3;
            DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimals));
            if (kilometers > 0) distanceText.setText(
                    kilometers + " km " + decimalFormat.format(meters).substring(1) + " m");
            else distanceText.setText(decimalFormat.format(meters).substring(1) + " m.");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the estimated time information based on the provided JSON object.
     *
     * @param jsonObject The JSON object containing the estimated time information.
     */
    private void setEstimatedTimeInfo(JSONObject jsonObject) {
        try {
            int timeInMinutes = jsonObject.getInt("time");
            int hours = 0;
            while (timeInMinutes - 60 > 0) {
                timeInMinutes -= 60;
                hours++;
            }
            if (hours > 0) timeText.setText(hours + " h " + timeInMinutes + " min");
            else timeText.setText(timeInMinutes + " min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a given distance[m] to the view.
     *
     * @param meters The distance amount
     */
    public void setDistanceInfo(double meters) {
        double kilometers = 0;
        while (meters - 1 > 0) {
            meters -= 1;
            kilometers++;
        }
        int decimals = 3;
        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(decimals));
        if (kilometers > 0) distanceText.setText(
                kilometers + " km " + decimalFormat.format(meters).substring(1) + " m");
        else distanceText.setText(decimalFormat.format(meters).substring(1) + " m.");
    }

    /**
     * Sets a given time[min] to the view.
     *
     * @param timeInMinutes The amount of time to be set.
     */
    public void setEstimatedTimeInfo(int timeInMinutes) {
        int hours = 0;
        while (timeInMinutes - 60 > 0) {
            timeInMinutes -= 60;
            hours++;
        }
        if (hours > 0) timeText.setText(hours + " h " + timeInMinutes + " min");
        else timeText.setText(timeInMinutes + " min");
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public void setTimeEstimate(int timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public int getTimeEstimate() {
        return timeEstimate;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
