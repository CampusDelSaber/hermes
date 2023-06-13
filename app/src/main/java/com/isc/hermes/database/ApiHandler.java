package com.isc.hermes.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import timber.log.Timber;

public class ApiHandler {
    private String API_URL = "https://api-rest-hermes.onrender.com/incidents";

    public String fetchDataFromApi(String apiUrl) {
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

    public void handleApiResponse(String result) {
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
