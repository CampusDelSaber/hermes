package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.model.navigation.TransportationType;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.DijkstraAlgorithm;
import com.isc.hermes.view.IncidentTypeButton;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONException;
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
    static boolean isActive, isCurrentLocationSelected;
    private final Context context;
    private RelativeLayout navOptionsForm;
    private Button cancelButton, startButton, chooseStartPointButton, startPointButton,
            finalPointButton, currentLocationButton;
    private LinearLayout transportationTypesContainer;
    private final MapWayPointController mapWayPointController;
    private LatLng startPoint, finalPoint;
    private InfoRouteController infoRouteController;
    private DijkstraAlgorithm dijkstraAlgorithm;
    private Map<String, String> routeOptions;
    private TransportationType transportationType;
    private Map<String, TransportationType> transportationTypeMap;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context               Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public NavigationOptionsController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        isCurrentLocationSelected = true;
        this.mapWayPointController = mapWayPointController;
        transportationType = TransportationType.CAR;
        setNavigationViewComponents();
        infoRouteController = InfoRouteController.getInstance(context, this);
        setNavOptionsUiComponents();
        setButtons();
        dijkstraAlgorithm = DijkstraAlgorithm.getInstance();
        setTransportationTypeOptions();
    }

    /**
     * Sets the transportations types map to get the one selected later
     */
    private void setTransportationTypeOptions(){
        transportationTypeMap = new HashMap<>();
        transportationTypeMap.put(TransportationType.CAR.getName(), TransportationType.CAR);
        transportationTypeMap.put(TransportationType.BIKE.getName(), TransportationType.BIKE);
        transportationTypeMap.put(TransportationType.WALK.getName(), TransportationType.WALK);
        transportationTypeMap.put(
                TransportationType.MOTORCYCLE.getName(), TransportationType.MOTORCYCLE
        );
    }

    /**
     * Sets the navigation view ui components by its id
     */
    private void setNavigationViewComponents(){
        AppCompatActivity activity = ((AppCompatActivity) context);
        navOptionsForm = activity.findViewById(R.id.navOptions_form);
        cancelButton = activity.findViewById(R.id.cancel_navOptions_button);
        startButton = activity.findViewById(R.id.start_button_nav);
        chooseStartPointButton = activity.findViewById(R.id.choose_startPoint_button);
        currentLocationButton = activity.findViewById(R.id.current_location_button);
        startPointButton = activity.findViewById(R.id.startPoint_button);
        finalPointButton = activity.findViewById(R.id.finalPoint_Button);
        transportationTypesContainer = activity.findViewById(R.id.transportationTypesContainer);

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
            handleHiddeItemsView();
            isActive = false;
            mapWayPointController.setMarked(false);
            mapWayPointController.deleteMarks();
        });
    }

    /**
     * Handles the action when the current location button is chosen.
     */
    @SuppressLint("SetTextI18n")
    private void handleCurrentLocationChosen() {
        isCurrentLocationSelected = true;
        startPointButton.setText("Your Location");
    }

    /**
     * Handles the action when the choose start point button is clicked.
     */
    private void handleChooseStartPointButton() {
        isActive = true;
        isCurrentLocationSelected = false;
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
                        Color.parseColor(navOptionTypeColors[i]), navOptionTypeIcons[i]);
                setTransportationButtonBehavior(button);
                transportationTypesContainer.addView(button);
            }
            transportationTypesContainer.removeViews(0,
                    transportationTypesContainer.getChildCount() - 4);
        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }
    }

    /**
     * Method to manage the transportation type button behavior to select a type
     * @param button transport type button
     */
    private void setTransportationButtonBehavior(Button button) {
        if (button != null) {
            button.setOnClickListener(v -> {
                transportationType = transportationTypeMap.getOrDefault(
                        button.getText(), TransportationType.CAR
                );
            });
        }
    }

    /**
     * Sets the start point of the navigation.
     *
     * @param point The latitude and longitude coordinates of the start point.
     */
    public void setStartPoint(LatLng point) {
        startPoint = point;
        isCurrentLocationSelected = false;
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
        startPointButton.setText((!isCurrentLocationSelected) ?
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
        if (isCurrentLocationSelected) startPoint = CurrentLocationModel.getInstance().getLatLng();
        LatLng start = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
        LatLng destination = new LatLng(finalPoint.getLatitude(), finalPoint.getLongitude());
        GraphController graphController = new GraphController(start, destination);

        markStartEndPoint(start, destination);
        executeGraphBuild(graphController);
    }

    /**
     * Method to mark the start and destination point on map
     * @param start start point to init route
     * @param destination destination final point's route
     */
    private void markStartEndPoint(LatLng start, LatLng destination) {
        mapWayPointController.setMarkerOnMap(start);
        mapWayPointController.setMarkerOnMap(destination);
    }

    /**
     * Executes the graph build async to load the graph before render it
     * @param graphController the graph controller to build the graph
     */
    @SuppressLint("StaticFieldLeak")
    private void executeGraphBuild(GraphController graphController){
        new AsyncTask<Void, Void, Map<String, String>>() {
            @Override
            protected Map<String, String> doInBackground(Void... voids) {
                getRouteOptionsFromAlgorithm(graphController);
                return null;
            }
            @Override
            protected void onPostExecute(Map<String, String> routes) {
                showRoutes();
            }
        }.execute();
    }

    /**
     * Method to get the routes' options from the algorithm for the route's suggestions
     * @param graphController  the graph's controller class
     */
    private void getRouteOptionsFromAlgorithm(GraphController graphController) {
        try {
            graphController.buildGraph();
            routeOptions = dijkstraAlgorithm.getGeoJsonRoutes(
                    graphController.getGraph(), graphController.getStartNode(),
                    graphController.getDestinationNode(), transportationType
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the routes on the map
     */
    private void showRoutes() {
        String jsonA = routeOptions.getOrDefault("Route A", "{coordinates: []}");
        String jsonB = routeOptions.getOrDefault("Route B", "{coordinates: []}");
        String jsonC = routeOptions.getOrDefault("Route C", "{coordinates: []}");
        ArrayList<String> geoJson = new ArrayList<>(List.of(jsonC, jsonB, jsonA));
        MapPolyline mapPolyline = new MapPolyline();
        infoRouteController.showInfoRoute(geoJson, mapPolyline);
    }

    /**
     * Method to get the waypoint controller
     * @return map waypoint controller
     */
    public MapWayPointController getMapWayPointController() {
        return mapWayPointController;
    }
}
