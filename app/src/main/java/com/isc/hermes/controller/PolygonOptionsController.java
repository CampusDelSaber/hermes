package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.IncidentsUploader;
import com.isc.hermes.model.Utils.IncidentsUtils;
import com.isc.hermes.model.incidents.GeometryType;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapManager;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Class on controller function to initialise all components in natural disaster form
 */
public class PolygonOptionsController {
    private final RelativeLayout polygonOptions;
    private final Button submitDisasterButton;
    private final Button cancelSubmitButton;
    private final Context context;
    private MapPolygonController polygonController;

    public PolygonOptionsController(Context context, MapPolygonController polygonController){
        this.context = context;
        polygonOptions = ((AppCompatActivity) context).findViewById(R.id.natural_disaster_form);
        submitDisasterButton = ((AppCompatActivity) context).findViewById(R.id.submit_natural_disaster);
        cancelSubmitButton = ((AppCompatActivity) context).findViewById(R.id.cancel_submit_natural_disaster);
        setButtonsOnClick();
        polygonController.deleteMarks();
        this.polygonController = polygonController;
    }

    /**
     * Method to display hidden components from form
     */
    public void displayComponents(){
        polygonOptions.startAnimation(Animations.entryAnimation);
        polygonOptions.setVisibility(View.VISIBLE);
    }

    /**
     * Method to perform click action to submit and cancel buttons
     */
    private void setButtonsOnClick() {
        cancelSubmitButton.setOnClickListener(v->{
            handleCancelButtonClick();
        });
        submitDisasterButton.setOnClickListener(v->{
            handleAcceptButtonClick();

        });
    }

    /**
     * Method to perform an action when cancel button is pressed
     */
    private void handleCancelButtonClick() {
        polygonOptions.startAnimation(Animations.exitAnimation);
        polygonOptions.setVisibility(View.GONE);
        resetMapConfiguration();
    }

    /**
     * Method to perform an action when submit button is pressed
     */
    private void handleAcceptButtonClick(){
        handleCancelButtonClick();
        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return uploadNaturalDisasterDataBase();
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Integer responseCode) {
                handleUploadResponse(responseCode);
            }
        };
        task.execute();
    }
    private void handleUploadResponse(Integer responseCode) {
        System.out.println(responseCode);
        System.out.println("EL PEPEPE");
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, R.string.natural_disaster_uploaded, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, R.string.natural_disaster_not_uploaded, Toast.LENGTH_SHORT).show();
        }
    }

    private Integer uploadNaturalDisasterDataBase(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateCreated= simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String deathDate =  simpleDateFormat.format(calendar.getTime());;
        String coordinates = MapPolygonController.getInstance(this.polygonController.getMapboxMap(), context).getStringWithPolygonPoint();
        String JsonString = IncidentsUploader.getInstance().generateJsonIncident("Natural Disaster","Natural Disaster",dateCreated, deathDate , GeometryType.POLYGON.getName(),coordinates);
        return IncidentsUploader.getInstance().uploadIncident(JsonString);
    }

    /**
     * Method to change map controller to its original configuration with waypoints
     */
    private void resetMapConfiguration(){
        MapManager.getInstance().removeCurrentClickController();
        MapManager.getInstance().setMapClickConfiguration(new MapWayPointController(MapManager.getInstance().getMapboxMap(), this.context));
        MapManager.getInstance().getMapboxMap().setStyle(MapManager.getInstance().getMapboxMap().getStyle().getUri());
    }

    /**
     * Method to advice the user to select an amount of vertexes to complete a polygon
     * @param vertexes is vertexes that user have to select
     */
    public void showPolygonVertexesMessage(int vertexes){
        Toast.makeText(context,"Select "+vertexes+" points more", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to advice user he must select at least 3 points to build a valid polygon
     */
    public void showPolygonMessage(){
        Toast.makeText(context,"Select at least 3 points", Toast.LENGTH_SHORT).show();
    }
}
