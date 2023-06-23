package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


/**
 * Class to represent the navigation controller options' class for the view
 * Setting methods to render and manage the different ui component's behaviour
 */
public class NavigationOptionsController {
    static boolean isActive, isLocationStartChosen;
    private final Context context;
    private final RelativeLayout navOptionsForm;
    private final Button cancelButton, startButton, chooseStartPointButton,
            startPointButton, finalPointButton, currentLocationButton;
    private final LinearLayout transportationTypesContainer;
    private final MapWayPointController mapWayPointController;
    private LatLng startPoint, finalPoint;
    private InfoRouteController infoRouteController;


    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public NavigationOptionsController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        isActive = false;
        isLocationStartChosen = true;
        this.mapWayPointController = mapWayPointController;
        navOptionsForm = ((AppCompatActivity)context).findViewById(R.id.navOptions_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_navOptions_button);
        startButton = ((AppCompatActivity) context).findViewById(R.id.start_button_nav);
        chooseStartPointButton = ((AppCompatActivity) context).findViewById(R.id.choose_startPoint_button);
        currentLocationButton = ((AppCompatActivity) context).findViewById(R.id.current_location_button);
        startPointButton = ((AppCompatActivity) context).findViewById(R.id.startPoint_button);;
        finalPointButton = ((AppCompatActivity) context).findViewById(R.id.finalPoint_Button);;
        transportationTypesContainer = ((AppCompatActivity) context).findViewById(R.id.transportationTypesContainer);
        infoRouteController = InfoRouteController.getInstance(context);
        setNavOptionsUiComponents();
        setButtons();
    }

    /**
     * Sets the buttons and their click listeners.
     */
    private void setButtons() {
        chooseStartPointButton.setOnClickListener(v -> handleChooseStartPointButton());
        currentLocationButton.setOnClickListener(v -> handleCurrentLocationChosen());
        manageCancelButton();
        startButton.setOnClickListener(v -> handleAcceptButtonClick());
    }

    /**
     * Method to manage the cancel button behavior
     */
    private void manageCancelButton() {
        cancelButton.setOnClickListener(v -> {
            isLocationStartChosen = true;
            handleHiddeItemsView();
            isActive = false;
            mapWayPointController.setMarked(false);
        });
    }

    /**
     * Handles the action when the current location button is chosen.
     */
    @SuppressLint("SetTextI18n")
    private void handleCurrentLocationChosen() {
        isLocationStartChosen = true;
        startPointButton.setText("Your Location");
    }

    /**
     * Handles the action when the choose start point button is clicked.
     */
    private void handleChooseStartPointButton() {
        isActive = true;
        isLocationStartChosen = false;
        handleHiddeItemsView();
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    public void handleHiddeItemsView() {
        mapWayPointController.setMarked(false);
        getNavOptionsForm().startAnimation(Animations.exitAnimation);
        getNavOptionsForm().setVisibility(View.GONE);
        mapWayPointController.deleteMarks();
    }


    /**
     * This method assigns values to the incident components.
     *
     * <p>
     *     This method assign values and views to the incident components such as the incident type
     *     spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setNavOptionsUiComponents() {
        String[] navOptionsTypes = context.getResources().getStringArray(R.array.navOptions_type);
        String[] navOptionTypeColors = context.getResources().getStringArray(R.array.navOptions_type_colors);
        String[] navOptionTypeIcons = context.getResources().getStringArray(R.array.navOptions_type_icons);

        if (navOptionsTypes.length == navOptionTypeColors.length &&
                navOptionTypeIcons.length == navOptionsTypes.length) {
            for (int i = 0; i < navOptionsTypes.length; i++) {
                Button button = IncidentTypeButton.getIncidentTypeButton(context, navOptionsTypes[i].toLowerCase(),
                        Color.parseColor((String) navOptionTypeColors[i]), navOptionTypeIcons[i]);
                transportationTypesContainer.addView(button);
            }
            transportationTypesContainer.removeViews(0,
                    transportationTypesContainer.getChildCount()-4);
        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }
    }

    /**
     * Sets the start point of the navigation.
     *
     * @param point The latitude and longitude coordinates of the start point.
     */
    public void setStartPoint(LatLng point) {
        isLocationStartChosen = false;
        startPoint = point;
        updateUiPointsComponents();
    }

    /**
     * Updates the UI components related to the navigation points.
     * This method is called internally after setting the start or final point.
     * It animates and shows the navigation options form and updates the text on the buttons.
     */
    private void updateUiPointsComponents() {
        if (isActive) {
            navOptionsForm.startAnimation(Animations.entryAnimation);
            navOptionsForm.setVisibility(View.VISIBLE);
        }
        setPointsButtonShownTexts();
    }

    /**
     * Formats the latitude and longitude values into a string.
     *
     * @param latitude  The latitude value.
     * @param longitude The longitude value.
     * @return The formatted string in the format "Lt: {latitude}\nLg: {longitude}".
     */
    private String formatLatLng(double latitude, double longitude) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        String formattedLatitude = decimalFormat.format(latitude);
        String formattedLongitude = decimalFormat.format(longitude);
        return "Lt: " + formattedLatitude + "\n" + "Lg: " + formattedLongitude;
    }

    /**
     * Returns the navigation options form layout.
     *
     * @return The RelativeLayout representing the navigation options form.
     */
    public RelativeLayout getNavOptionsForm() {
        return navOptionsForm;
    }

    /**
     * Sets the final navigation point.
     *
     * @param point The latitude and longitude coordinates of the final point.
     */
    public void setFinalNavigationPoint(LatLng point) {
        this.finalPoint = point;
        updateUiPointsComponents();
    }

    /**
     * Sets the text for the start and final point buttons based on the current selected points.
     * If the start point is not chosen, it displays the latitude and longitude of the start point.
     * If the final point is not null, it displays the latitude and longitude of the final point.
     * Otherwise, it displays a default text indicating that the final point is not selected.
     */
    private void setPointsButtonShownTexts() {
        startPointButton.setText((!isLocationStartChosen)?
                formatLatLng(startPoint.getLatitude(),startPoint.getLongitude()):"Your Location");
        finalPointButton.setText((finalPoint != null)?
                formatLatLng(finalPoint.getLatitude(),finalPoint.getLongitude()):"Not selected");
    }

    /**
     * This method handles the actions performed when the accept button is clicked.
     */
    private void handleAcceptButtonClick() {
        handleHiddeItemsView();
        isActive = false;
        Node startPointNode = (startPoint != null) ? new Node("01",startPoint.getLatitude(),
                startPoint.getLatitude()): null;
        Node finalPointNode = (startPoint != null) ? new Node("02",finalPoint.getLatitude(),
                finalPoint.getLatitude()): null;
        // TODO: Navigation Route between these two nodes
        navOptionsForm.setVisibility(View.GONE);
        showRoutes();

    }

    private void showRoutes(){
        Map<String, String> r = new HashMap<>();

        r.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"time\":10,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        r.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"time\":12,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        r.put("Route C", "{\"type\":\"Feature\",\"distance\":1.6061126697414532,\"time\":15,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.159019, -17.398311],[-66.154399, -17.397043],[-66.151315, -17.398656],[-66.147585, -17.400585],[-66.142978, -17.401595]]}}");
        String jsonA = r.get("Route A");
        String jsonB = r.get("Route B");
        String jsonC = r.get("Route C");

        ArrayList<String> geoJson = new ArrayList<>();
        geoJson.add(jsonA);
        geoJson.add(jsonB);
        geoJson.add(jsonC);

        MapPolyline mapPolyline = new MapPolyline(MapDisplay.getInstance(context,null,null).getMapView());
        infoRouteController.showInfoRoute(geoJson, mapPolyline);
    }
}
