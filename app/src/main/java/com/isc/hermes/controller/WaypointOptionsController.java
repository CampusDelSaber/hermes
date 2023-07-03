package com.isc.hermes.controller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.controller.PopUp.PopUpWarningUpdateUserType;
import com.isc.hermes.model.User.TypeUser;
import com.isc.hermes.model.User.User;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Utils.DataAccountOffline;
import com.isc.hermes.requests.geocoders.StreetValidator;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapManager;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class WaypointOptionsController {

    private StreetValidator streetValidator;
    private final RelativeLayout waypointOptions;
    private final IncidentFormController incidentFormController;
    private final NavigationOptionsController navigationOptionsFormController;
    private final LinearLayout reportIncidentsView;
    private final Button navigateButton;
    private TrafficAutomaticFormController trafficAutomaticFormController;
    private final Button reportIncidentButton;
    private final Button reportTrafficButton;
    private final Button reportNaturalDisasterButton;
    private final Context context;
    private TextView placeName;
    private static WaypointOptionsController waypointOptionsController;


    /**
     * This is the constructor method. Init all the components of UI.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public WaypointOptionsController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        streetValidator = new StreetValidator();
        waypointOptions = ((AppCompatActivity)context).findViewById(R.id.waypoint_options);
        incidentFormController = IncidentFormController.getInstance(context,mapWayPointController);
        navigationOptionsFormController = new NavigationOptionsController(context, mapWayPointController);
        navigateButton = ((AppCompatActivity) context).findViewById(R.id.navigate_button);
        trafficAutomaticFormController = new TrafficAutomaticFormController(context, mapWayPointController, navigationOptionsFormController);
        reportIncidentButton = ((AppCompatActivity) context).findViewById(R.id.report_incident_button);
        reportTrafficButton = ((AppCompatActivity) context).findViewById(R.id.report_traffic_button);
        reportNaturalDisasterButton = ((AppCompatActivity) context).findViewById(R.id.report_natural_disaster_button);
        placeName = ((AppCompatActivity) context).findViewById(R.id.place_name);
        reportIncidentsView = ((AppCompatActivity) context).findViewById(R.id.report_incidents);
        setButtonsOnClick();
    }

    /**
     * Method to assign functionality to the buttons of the view.
     */
    private void setButtonsOnClick(){
        navigateButton.setOnClickListener(v -> {
            waypointOptions.startAnimation(Animations.exitAnimation);
            navigationOptionsFormController.getNavOptionsForm().startAnimation(Animations.entryAnimation);
            navigationOptionsFormController.getNavOptionsForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
        });

        reportIncidentButton.setOnClickListener(v -> {
            waypointOptions.startAnimation(Animations.exitAnimation);
            incidentFormController.getIncidentForm().startAnimation(Animations.entryAnimation);
            incidentFormController.getIncidentForm().setVisibility(View.VISIBLE);
            waypointOptions.setVisibility(View.GONE);
        });

        reportTrafficButton.setOnClickListener(v -> {

            waypointOptions.startAnimation(Animations.exitAnimation);
            System.out.println("MIO "+navigationOptionsFormController.getJson());
            AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {

                    return trafficAutomaticFormController.uploadTrafficDataBase();
                }
                @Override
                protected void onPostExecute(Integer responseCode) {
                    trafficAutomaticFormController.handleUploadResponse(responseCode);
                }
            };

            task.execute();
            waypointOptions.setVisibility(View.GONE);
        });

        reportNaturalDisasterButton.setOnClickListener(v->{
            MapManager.getInstance().removeCurrentClickController();
            MapManager.getInstance().setMapClickConfiguration( MapPolygonController.getInstance(MapManager.getInstance().getMapboxMap(), this.context));
            waypointOptions.startAnimation(Animations.exitAnimation);
            waypointOptions.setVisibility(View.GONE);
        });
    }

    /**
     * This is a getter method to waypoint options layout.
     * @return Return a layout.
     */
    public RelativeLayout getWaypointOptions() {
        return waypointOptions;
    }

    /**
     * This is the getter method to incident form controller.
     * @return Return the controller class of incident form view.
     */
    public IncidentFormController getIncidentFormController() {
        return incidentFormController;
    }

    /**
     * This is the getter method to get the navigation options controller instance.
     * @return Return the navigation options controller form view.
     */
    public NavigationOptionsController getNavOptionsFormController() {
        return navigationOptionsFormController;
    }

    /**
     * Generates a pop-up depending on the type of user.
     * If the user's type is GENERAL, a warning pop-up for updating the user type is shown.
     */
    private void generatePopUpDependingOnTypeUser() {
        if (UserRepository.getInstance().getUserContained().getTypeUser().equals(
                TypeUser.GENERAL.getTypeUser())) {
            new PopUpWarningUpdateUserType((AppCompatActivity) context).show();
        }
    }

    /**
     * This method set the report incident status view if the point market is a street.
     *
     * @param point is the coordinate point market.
     */
    public void setReportIncidentStatus(LatLng point) {
        if (!hasValidStreetContext(point)) {
            System.out.println("no es street");
            hideReportIncidentsView();
        } else {
            System.out.println("pregunta si es admin");
            if(UserRepository.getInstance().getUserContained().isAdministrator()){
                System.out.println("si es administrador");
                System.out.println(UserRepository.getInstance().getUserContained().getTypeUser());
                showReportIncidentsView();
            }
        }
    }

    /**
     * This method validates if the street has a valid context
     *
     * @param point is the coordinate point market.
     * @return a boolean type.
     */
    private boolean hasValidStreetContext(LatLng point) {
        return streetValidator.hasStreetContext(point);
    }

    /**
     * This method validates if the user is a general type
     *
     * @return a boolean type
     */
    private boolean isGeneralUser() {
        return UserRepository.getInstance().getUserContained().getTypeUser().equals(TypeUser.GENERAL.getTypeUser());
    }

    /**
     *  This method hides the Incident Report
     */
    private void hideReportIncidentsView() {
        reportIncidentsView.setVisibility(View.GONE);
    }

    /**
     * This method shows the Incident Report
     */
    private void showReportIncidentsView() {
        reportIncidentsView.setVisibility(View.VISIBLE);
    }

}
