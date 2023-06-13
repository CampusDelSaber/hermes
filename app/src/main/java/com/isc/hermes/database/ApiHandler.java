package com.isc.hermes.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timber.log.Timber;

public class ApiHandler {
    private final String API_URL = "https://api-rest-hermes.onrender.com/incidents";
    private final String INCIDENTS_COLLECTION_NAME = "incidents";

    private ExecutorService executorService;

    public JSONArray getAllIncidents() throws ExecutionException, InterruptedException {
        Future<String> future = executeService(API_URL + "/" + INCIDENTS_COLLECTION_NAME);
        return getJSONArrayOnResult(future.get());
    }

    private Future<String> executeService(String apiUrl){
        executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(() -> fetchDataFromApi(apiUrl));
    }

    public String fetchDataFromApi(String apiUrl) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                response.append(line);

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public JSONArray getJSONArrayOnResult(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);

            handleIncidentsDataExample(jsonArray);
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void handleIncidentsDataExample(JSONArray incidentsArray) throws JSONException {
        // Example of processing the data
        for (int i = 0; i < incidentsArray.length(); i++) {
            JSONObject incidentObj = incidentsArray.getJSONObject(i);
            String id = incidentObj.getString("_id");
            String type = incidentObj.getString("type");
            String reason = incidentObj.getString("reason");
            String dateCreated = incidentObj.getString("dateCreated");

            // Process the incident data as needed example to log:
            Timber.tag("Incident").d("ID: %s", id);
            Timber.tag("Incident").d("Type: %s", type);
            Timber.tag("Incident").d("Reason: %s", reason);
            Timber.tag("Incident").d("Date Created: %s", dateCreated);
        }
    }
}
