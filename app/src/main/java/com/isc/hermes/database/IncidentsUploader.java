package com.isc.hermes.database;

import com.mapbox.mapboxsdk.geometry.LatLng;
import org.bson.types.ObjectId;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class IncidentsUploader {
    private final String API_URL = "https://api-rest-hermes.onrender.com/incidents";
    private static IncidentsUploader instance;
    private LatLng lastClickedPoint;

    public int uploadIncident(String incidentJsonString){
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] payloadBytes = incidentJsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(payloadBytes, 0, payloadBytes.length);
            }

            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String generateCurrentDateCreated() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(currentDate);
    }

    public String addTimeToCurrentDate(String timeDuration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String[] parts = timeDuration.split(" ");
        int amount = Integer.parseInt(parts[0]);
        String unit = parts[1].toLowerCase();

        if (unit.equals("minute") || unit.equals("minutes")) {
            calendar.add(Calendar.MINUTE, amount);
        } else if (unit.equals("hour") || unit.equals("hours")) {
            calendar.add(Calendar.HOUR, amount);
        } else if (unit.equals("day") || unit.equals("days")) {
            calendar.add(Calendar.DAY_OF_YEAR, amount);
        } else if (unit.equals("month") || unit.equals("months")) {
            calendar.add(Calendar.MONTH, amount);
        }

        Date modifiedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(modifiedDate);
    }

    public String generateObjectId() {
        ObjectId objectId = new ObjectId();
        return objectId.toHexString();
    }
    public String generateJsonIncident(String id, String type, String reason, String dateCreated, String deathDate, String coordinates) {
        return "{\"_id\": \"" + id + "\",\"type\": \"" + type + "\",\"reason\": \"" + reason + "\",\"dateCreated\": \"" + dateCreated + "\",\"deathDate\": \"" + deathDate + "\",\"geometry\": {\"type\": \"Point\",\"coordinates\": " + coordinates + "}}";
    }

    public LatLng getLastClickedPoint() {
        return lastClickedPoint;
    }

    public void setLastClickedPoint(LatLng point) {
        lastClickedPoint = point;
    }
    public String getCoordinates(){
        String[] parts = lastClickedPoint.toString().split("[=,]");
        String latitude = parts[1].trim();
        String longitude = parts[3].trim();
        return "[" + latitude + ", " + longitude + "]";
    }
    public static IncidentsUploader getInstance() {
        if (instance == null) instance = new IncidentsUploader();
        return instance;
    }
}
