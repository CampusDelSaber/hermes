package com.isc.hermes.database;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.model.CurrentLocationModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IncidentsUploader {
    private final String API_URL = "https://api-rest-hermes.onrender.com/";
    private static IncidentsUploader instance;

    public void uploadIncident(String incidentJsonString){
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the necessary headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the JSON payload
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(incidentJsonString);
            outputStream.flush();
            outputStream.close();

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Incident uploaded successfully.");
            } else {
                System.out.println("Failed to upload incident. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  String createRandomIncidentId() {
        List<Character> characters = new ArrayList<>();
        Random random = new Random();
        for (char c = '0'; c <= '9'; c++) {
            characters.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
        }
        StringBuilder final_id = new StringBuilder();
        int length = 15;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.size());
            char randomCharacter = characters.get(randomIndex);
            final_id.append(randomCharacter);
        }
        return final_id.toString();
    }

    public double getCurrentLatitude(){
        return CurrentLocationController.getInstance().getLatitude();
    }
    public double getCurrentLongitude(){
        return CurrentLocationController.getInstance().getLongitude();
    }

    public String createJSONString(String id, String type, String reason, String time, String geometryType, String longitud, String latitud) {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        String currentDateString = dateFormat.format(currentDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        if (time.contains("minute")) {
            int minutes = Integer.parseInt(time.split(" ")[0]);
            calendar.add(Calendar.MINUTE, minutes);
        } else if (time.contains("hour")) {
            int hours = Integer.parseInt(time.split(" ")[0]);
            calendar.add(Calendar.HOUR, hours);
        } else if (time.contains("day")) {
            int days = Integer.parseInt(time.split(" ")[0]);
            calendar.add(Calendar.DAY_OF_MONTH, days);
        }
        Date deathDate = calendar.getTime();
        String deathDateString = dateFormat.format(deathDate);

        String jsonString = "{\"_id\": \"" + id + "\",\"type\": \"" + type + "\",\"reason\": \"" + reason + "\",\"dateCreated\": \"" +
                currentDateString + "\",\"deathDate\": \"" + deathDateString + "\",\"geometry\": {\"type\": \"" + geometryType +
                "\",\"coordinates\": [" + latitud + ", " + longitud + "]}}";

        return jsonString;
    }

    public static IncidentsUploader getInstance() {
        if (instance == null) instance = new IncidentsUploader();
        return instance;
    }
}
