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
public class TrafficFormController {
    private final Context context;

    private final MapWayPointController mapController;

    int timeEstimate = 10;
    String level = "Normal";
    String time = "Minute";

    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapController Is the controller of the map.
     */
    public TrafficFormController(Context context, MapWayPointController mapController) {
        this.context = context;
        this.mapController = mapController;


    }

    public MapWayPointController getMapController() {
        return mapController;
    }


    /**
     * This method handles the actions performed when the cancel button is clicked.
     */


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

     This method etrieves the selected incident type from the incident spinner.
     @return The selected incident type as a string.
     */
    public String getTrafficType(){
        String selectedIncidentType = level;
        return selectedIncidentType;
    }
    /**

     This method retrieves the selected incident time from the number picker and time spinner.
     @return The selected incident time as a  string.
     */
    public String getTrafficTime(){
        int selectedIncidentTime = timeEstimate;
        String selectedIncidentTimeOption = time;
        return selectedIncidentTime+ " " + selectedIncidentTimeOption;
    }

    /**
     * This method uploads an incident to the database by generating the necessary data and invoking the appropriate methods.
     *
     * @return The HTTP response code indicating the status of the upload.
     *
     */
    public int uploadTrafficDataBase(){
        String id = IncidentsUtils.getInstance().generateObjectId();
        String dateCreated = IncidentsUtils.getInstance().generateCurrentDateCreated();
        String deathDate = IncidentsUtils.getInstance().addTimeToCurrentDate(getTrafficTime());
        String coordinates = TrafficUploader.getInstance().getCoordinates();
        String JsonString = TrafficUploader.getInstance().generateJsonIncident(id,"Traffic","Test Traffic",dateCreated, deathDate , GeometryType.LINE_STRING.getName(), coordinates);
        return TrafficUploader.getInstance().uploadIncident(JsonString);
    }


}
