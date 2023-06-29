package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.model.navigation.TestRouteBuilder;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.Route;
import com.isc.hermes.model.navigation.RouteEstimatesManager;
import com.isc.hermes.model.navigation.UserRouteTracker;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.DecimalFormat;

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
     * @param context               Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public NavigationOptionsController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        isActive = false;
        isLocationStartChosen = true;
        this.mapWayPointController = mapWayPointController;
        navOptionsForm = ((AppCompatActivity) context).findViewById(R.id.navOptions_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_navOptions_button);
        startButton = ((AppCompatActivity) context).findViewById(R.id.start_button_nav);
        chooseStartPointButton = ((AppCompatActivity) context).findViewById(R.id.choose_startPoint_button);
        currentLocationButton = ((AppCompatActivity) context).findViewById(R.id.current_location_button);
        startPointButton = ((AppCompatActivity) context).findViewById(R.id.startPoint_button);
        ;
        finalPointButton = ((AppCompatActivity) context).findViewById(R.id.finalPoint_Button);
        ;
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
     * This method assign values and views to the incident components such as the incident type
     * spinner, incident estimated time spinner and incident estimated time number picker.
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
                    transportationTypesContainer.getChildCount() - 4);
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
        startPointButton.setText((!isLocationStartChosen) ?
                formatLatLng(startPoint.getLatitude(), startPoint.getLongitude()) : "Your Location");
        finalPointButton.setText((finalPoint != null) ?
                formatLatLng(finalPoint.getLatitude(), finalPoint.getLongitude()) : "Not selected");
    }

    /**
     * This method handles the actions performed when the accept button is clicked.
     */
    private void handleAcceptButtonClick() {
        handleHiddeItemsView();
        isActive = false;
        Node startPointNode = (startPoint != null) ? new Node("01", startPoint.getLatitude(),
                startPoint.getLatitude()) : null;
        Node finalPointNode = (startPoint != null) ? new Node("02", finalPoint.getLatitude(),
                finalPoint.getLatitude()) : null;
        // TODO: Navigation Route between these two nodes
        navOptionsForm.setVisibility(View.GONE);

        startTravel(startPointNode, finalPointNode);

    }

    private void startTravel(Node source, Node destiny) {
        TestRouteBuilder testRouteBuilder = new TestRouteBuilder();
        MapPolyline mapPolyline = new MapPolyline();

        testRouteBuilder.cookRoute();
        Route routeA = testRouteBuilder.getRouteA();
        UserRouteTracker userRouteTracker = new UserRouteTracker(routeA);

        startRouteEstimatesManager(routeA, userRouteTracker);
        infoRouteController.showInfoRoute(testRouteBuilder.exportToGeoJSON(), mapPolyline);
    }

    private void startRouteEstimatesManager(Route route, UserRouteTracker tracker){
        Runnable routeEstimatesManager = () -> new RouteEstimatesManager(route, tracker, infoRouteController).startEstimatesTrack();
        Thread estimatesThread = new Thread(routeEstimatesManager, "Estimates Thread");
        estimatesThread.start();
    }
}
