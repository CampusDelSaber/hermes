package com.isc.hermes.controller;

import android.content.Context;
import android.widget.Toast;
import com.isc.hermes.R;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.GeometryType;


import java.net.HttpURLConnection;

/**
 * This is the controller class for "waypoints_options_fragment" view.
 */
public class TrafficAutomaticFormController {
    private final Context context;
    private final MapWayPointController mapController;
    private InfoRouteController infoRouteController ;

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapController Is the controller of the map.
     */
    public TrafficAutomaticFormController(Context context, MapWayPointController mapController,NavigationOptionsController navigationOptionsController) {

        this.context = context;
        this.mapController = mapController;
        this.infoRouteController = InfoRouteController.getInstance(context,  navigationOptionsController);

    }

    /**

     the point on the map selected by the user
     @return mapController.
     */
    public MapWayPointController getMapController() {
        return mapController;
    }


    /**
     * This method handles the response received after uploading the traffic to the database.
     *
     * @param responseCode the response code received after uploading the traffic
     */
    public void handleUploadResponse(Integer responseCode) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, R.string.traffic_uploaded, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.traffic_not_uploaded, Toast.LENGTH_SHORT).show();
        }
    }

    /**

     This method calculateEstimateTime: the selected estimate time from the Time on the navigation.
     @param time the estimated time of arrival at the destination set when displaying the navigation
     @param waitingTime Time in which the user is in the on the navigation
     @return The selected Traffic type as a string.
     */
    public int calculateEstimateTime(Integer time,Integer waitingTime ) {
        int timeEstimate;
        if (waitingTime <=time) {
            timeEstimate = waitingTime  ;
        }
        else {
            timeEstimate = time + waitingTime ;
        }
        return timeEstimate;
    }

    /**
     This method getTrafficType: the selected Traffic type from the Time on the navigation.
     @param time the estimated time of arrival at the destination set when displaying the navigation
     @param waitingTime Time in which the user is in the on the navigation
     @return The selected Traffic type as a string.
     */
    public String getTrafficType(Integer time, Integer waitingTime) {
        String trafficLevel;
        if (waitingTime > time) {
            trafficLevel = "High Traffic";
        } else if (waitingTime < time ) {
            trafficLevel = "Low Traffic";
        }
        else{
            trafficLevel = "Normal Traffic";
        }
        return trafficLevel;
    }

    /**
     This method retrieves the selected Traffic time from the number picker and time spinner.
     @return The selected Traffic time as a  string.
     */
    public String getTrafficTime(){
        int selectedTrafficTime = calculateEstimateTime(infoRouteController.getTimeEstimate(),infoRouteController.getElapsedSeconds());
        return selectedTrafficTime+ " " + "Minutes";
    }

    /**
     * This method uploads an incident to the database by generating the necessary data and invoking the appropriate methods.
     *
     * @return The HTTP response code indicating the status of the upload.
     */
    public int uploadTrafficDataBase() {
        String id = IncidentsUtils.getInstance().generateObjectId();
        String dateCreated = IncidentsUtils.getInstance().generateCurrentDateCreated();
        String deathDate = IncidentsUtils.getInstance().addTimeToCurrentDate(getTrafficTime());
        String coordinates = TrafficUploader.getInstance().getCoordinates();
        String coordinatesNavigation = infoRouteController.getRoutes();
        String JsonString;
        if (coordinatesNavigation == null) {
            JsonString = TrafficUploader.getInstance().generateJsonIncident(getTrafficType(infoRouteController.getTimeEstimate(), infoRouteController.getElapsedSeconds()), "Traffic", dateCreated, deathDate, GeometryType.LINE_STRING.getName(), coordinates);
        } else {
            JsonString = TrafficUploader.getInstance().generateJsonIncident(getTrafficType(infoRouteController.getTimeEstimate(), infoRouteController.getElapsedSeconds()), "Traffic", dateCreated, deathDate, GeometryType.LINE_STRING.getName(), coordinatesNavigation);
        }
        return TrafficUploader.getInstance().uploadIncident(JsonString);
    }



}
