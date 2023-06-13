package com.isc.hermes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.isc.hermes.controller.SearcherController;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timber.log.Timber;

/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 * Handles current user location functionality.
 */
public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapDisplay mapDisplay;
    private String mapStyle = "default";
    private CurrentLocationController currentLocationController;

    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapbox();
        setContentView(R.layout.activity_main);
        initMapView();
        mapDisplay = new MapDisplay(this, mapView, new MapConfigure());
        mapDisplay.onCreate(savedInstanceState);
        addMapboxSearcher();
        mapStyleListener();
        initCurrentLocationController();

        // Execute the API call asynchronously
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> fetchDataFromApi("https://test-rest-hermes.onrender.com/incidents"));

        try {
            String result = future.get();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            handleApiResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            Timber.tag("sss").d("sss");
        }
    }

    /**
     * Method to add the searcher to the main scene above the map
     */
    private void addMapboxSearcher() {
        Searcher searcher = new Searcher();
        SearcherController searcherController = new SearcherController(searcher,
                findViewById(R.id.searchResults),findViewById(R.id.searchView));
        searcherController.runSearcher();
    }

    /**
     * This method will init the current location controller to get the real time user location
     */
    private void initCurrentLocationController(){
        currentLocationController = new CurrentLocationController(this, mapDisplay);
        currentLocationController.initLocation();
    }


    /**
     * Method for initializing the Mapbox object instance.
     */
    private void initMapbox() {
        Mapbox.getInstance(this, getString(R.string.access_token));
    }

    /**
     * Method for initializing the MapView object instance.
     */
    private void initMapView() {
        mapView = findViewById(R.id.mapView);
    }

    /**
     * Method for initializing the MapDisplay object instance.
     */
    private void initMapDisplay() {
        mapDisplay = new MapDisplay(this, mapView, new MapConfigure());
    }

    /**
     * Method for starting the MapView object instance.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapDisplay.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapDisplay.onResume();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapDisplay.onPause();
    }

    /**
     * Method for stopping the MapView object instance.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mapDisplay.onStop();
    }

    /**
     * Method for handling low memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapDisplay.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapDisplay.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapDisplay.onSaveInstanceState(outState);
    }

    /**
     * Method for adding maps styles.xml listener
     */
    private void mapStyleListener(){
        ImageButton styleButton = findViewById(R.id.btn_change_style);
        styleButton.setOnClickListener(styleMap -> {
            if (mapStyle.equals("default")) {
                mapStyle = "satellite";
            } else if (mapStyle.equals("satellite")) {
                mapStyle = "dark";
            } else {
                mapStyle = "default";
            }
            mapDisplay.setMapStyle(mapStyle);
        });
    }

    private String fetchDataFromApi(String apiUrl) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            System.out.println(response);
            System.out.println("response");
            bufferedReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private void handleApiResponse(String result) {
        try {
            JSONArray incidentsArray = new JSONArray(result);
            Timber.tag("HOLA").d(result);

            // Process each incident object
            for (int i = 0; i < incidentsArray.length(); i++) {
                JSONObject incidentObj = incidentsArray.getJSONObject(i);
                String id = incidentObj.getString("_id");
                String type = incidentObj.getString("type");
                String reason = incidentObj.getString("reason");
                String dateCreated = incidentObj.getString("dateCreated");

                // Process the incident data as needed
                Timber.tag("Incident").d("ID: %s", id);
                Timber.tag("Incident").d("Type: %s", type);
                Timber.tag("Incident").d("Reason: %s", reason);
                Timber.tag("Incident").d("Date Created: %s", dateCreated);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}