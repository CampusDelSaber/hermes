package com.isc.hermes.utils.offline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IncidentsOfflineDownloader {

    public IncidentsOfflineDownloader(){
        String url = "https://api-rest-hermes.onrender.com/incidents";
        String response = sendGetRequest(url);

        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("_id");
                    String type = jsonObject.getString("type");
                    String reason = jsonObject.getString("reason");
                    String dateCreated = jsonObject.getString("dateCreated");
                    String deathDate = jsonObject.getString("deathDate");
                    JSONObject geometry = jsonObject.getJSONObject("geometry");
                    String typeFigure = geometry.getString("type");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    String coordinatesString = coordinates.toString();


                    System.out.println("Incident " + (i + 1));
                    System.out.println("ID: " + id);
                    System.out.println("Type: " + type);
                    System.out.println("Reason: " + reason);
                    System.out.println("dateCreated: " + dateCreated);
                    System.out.println("deathDate: " + deathDate);
                    System.out.println("geometry: " + typeFigure);
                    System.out.println("coordinates: " + coordinatesString);
                    System.out.println();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private static String sendGetRequest(String url) {
        StringBuilder response = new StringBuilder();

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
            } else {
                System.out.println("RequestError. Answer Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
    }

