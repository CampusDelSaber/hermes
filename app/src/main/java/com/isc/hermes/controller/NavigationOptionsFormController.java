package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.text.DecimalFormat;
import timber.log.Timber;


/**
 * Class to represent the navigation controller options' class for the view
 * Setting methods to render and manage the different ui component's behaviour
 */
public class NavigationOptionsFormController {
    private final Context context;
    private final RelativeLayout navOptionsForm;
    private final Button cancelButton, startButton, chooseStartPointButton,
            startPointButton, finalPointButton, currentLocationButton;
    private final LinearLayout transportationTypesContainer;
    private final MapWayPointController mapWayPointController;
    private boolean isStartPointFromMapSelected;
    private LatLng startPoint, finalPoint;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context               Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public NavigationOptionsFormController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        this.isStartPointFromMapSelected = false;
        this.mapWayPointController = mapWayPointController;
        navOptionsForm = ((AppCompatActivity)context).findViewById(R.id.navOptions_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_navOptions_button);
        startButton = ((AppCompatActivity) context).findViewById(R.id.start_button);
        chooseStartPointButton = ((AppCompatActivity) context).findViewById(R.id.choose_startPoint_button);
        currentLocationButton = ((AppCompatActivity) context).findViewById(R.id.current_location_button);
        startPointButton = ((AppCompatActivity) context).findViewById(R.id.startPoint_button);
        finalPointButton = ((AppCompatActivity) context).findViewById(R.id.finalPoint_Button);
        transportationTypesContainer = ((AppCompatActivity) context).findViewById(R.id.transportationTypesContainer);
        setButtonsOnClick();
        setNavOptionsUiComponents();
    }

    /**
     * This method assigns functionality to the buttons of the view.
     */
    private void setButtonsOnClick() {
        cancelButton.setOnClickListener(v -> {
            startPoint = null;
            finalPoint = null;
            isStartPointFromMapSelected = false;
            handleHiddeItemsView();
        });
        startButton.setOnClickListener(v -> handleAcceptButtonClick());
        chooseStartPointButton.setOnClickListener(v -> handleChooseStartPointButton());
        currentLocationButton.setOnClickListener(v -> handleOnCurrentLocationOption());
        setPointsButtonShownTexts();
    }

    /**
     * Clears the current start point and updates the button text accordingly.
     */
    private void handleOnCurrentLocationOption() {
        startPoint = null;
        setPointsButtonShownTexts();
    }

    /**
     * Updates the button texts for the start and final points based on their values.
     */
    private void setPointsButtonShownTexts() {
        startPointButton.setText((startPoint != null) ?
                formatLatLng(startPoint.getLatitude(), startPoint.getLongitude()) : "Your Location");
        finalPointButton.setText((finalPoint != null) ?
                formatLatLng(finalPoint.getLatitude(), finalPoint.getLongitude()) : "Not selected");
    }

    /**
     * Formats the given latitude and longitude values into a string representation.
     *
     * @param latitude  The latitude value.
     * @param longitude The longitude value.
     * @return The formatted string with latitude and longitude.
     */
    private String formatLatLng(double latitude, double longitude) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        String formattedLatitude = decimalFormat.format(latitude);
        String formattedLongitude = decimalFormat.format(longitude);
        return "Lt: " + formattedLatitude + "\n" + "Lg: " + formattedLongitude;
    }

    /**
     * Handles the action when the choose start point button is clicked.
     * Sets a flag indicating that the start point is selected from the map and hides items view.
     */
    private void handleChooseStartPointButton() {
        isStartPointFromMapSelected = true;
        handleHiddeItemsView();
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    private void handleHiddeItemsView() {
        mapWayPointController.setMarked(false);
        navOptionsForm.startAnimation(Animations.exitAnimation);
        navOptionsForm.setVisibility(View.GONE);
        mapWayPointController.deleteMarks();
    }

    /**
     * This method handles the actions performed when the accept button is clicked.
     */
    private void handleAcceptButtonClick() {
        handleHiddeItemsView();
        isStartPointFromMapSelected = false;
        Node startPointNode = (startPoint != null) ? new Node("01",startPoint.getLatitude(),
                startPoint.getLatitude()): null;
        Node finalPointNode = new Node("02",finalPoint.getLatitude(), finalPoint.getLatitude());
        // TODO: Navigation Route between these two nodes
        startPoint = null;
        finalPoint = null;
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
        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }
    }

    /**
     * This is a getter method to Incident form layout.
     * @return Return a layout.
     */
    public RelativeLayout getNavOptionsForm() {
        return navOptionsForm;
    }

    /**
     * Method to receive if the choose point from map option is selected or not
     * @return true if the choose start point option from map is active
     */
    public boolean isStartPointFromMapSelected() {
        return isStartPointFromMapSelected;
    }

    /**
     * Method to set the start point from the button pressed
     * @param point the latLng point to set with
     */
    public void setStartPoint(LatLng point) {
        startPoint = point;
        updateUiPointsComponents();
    }


    /**
     * Updates the UI components related to the points based on the state of the start point selection.
     * If the start point is selected from the map, it animates the entry and makes the navigation options form visible.
     * It also updates the button texts for the start and final points.
     */
    private void updateUiPointsComponents() {
        if (isStartPointFromMapSelected) {
            navOptionsForm.startAnimation(Animations.entryAnimation);
            navOptionsForm.setVisibility(View.VISIBLE);
        }
        setPointsButtonShownTexts();
    }

    /**
     * Sets the final navigation point.
     *
     * @param point The LatLng object representing the final navigation point.
     */
    public void setFinalNavigationPoint(LatLng point) {
        this.finalPoint = point;
        updateUiPointsComponents();
    }
}
