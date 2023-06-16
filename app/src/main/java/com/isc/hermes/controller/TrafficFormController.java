package com.isc.hermes.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.utils.Animations;

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
    public String gettrafficType(){

        String selectedIncidentType = level;
        System.out.println("ESto" + selectedIncidentType);
        return selectedIncidentType;
    }
    /**

     This method retrieves the selected incident time from the number picker and time spinner.
     @return The selected incident time as a  string.
     */
    public String gettrafficTime(){

        int selectedIncidentTime = timeEstimate;

        String selectedIncidentTimeOption = time;
        System.out.println("ESto" + selectedIncidentTime);
        return selectedIncidentTime+ " " + selectedIncidentTimeOption;
    }

    /**

     This method uploads an incident to the database by generating the necessary data and invoking the appropriate methods.
     @return The HTTP response code indicating the status of the upload.
     */
    public int uploadtrafficDataBase(){
        String id = IncidentsUtils.getInstance().generateObjectId();
        String dateCreated = IncidentsUtils.getInstance().generateCurrentDateCreated();
        String deathDate = IncidentsUtils.getInstance().addTimeToCurrentDate(gettrafficTime());
        String coordinates = TrafficUploader.getInstance().getCoordinates();
        String coordinates2 = TrafficUploader.getInstance().getCoordinates2();
        String JsonString = TrafficUploader.getInstance().generateJsonTraffic(id,gettrafficType(),"Reason",dateCreated, deathDate ,coordinates,coordinates2);
        return TrafficUploader.getInstance().uploadTraffic(JsonString);
    }


}
