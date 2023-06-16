package com.isc.hermes.database;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TrafficUploader {

    private static TrafficUploader instance;
    private LatLng lastClickedPoint;
    private final String URL_INCIDENTS_API= "https://api-rest-hermes.onrender.com/incidents";

    /**
     * This method uploads an incident in JSON format to the remote server.
     *
     * @param incidentJsonString The JSON representation of the incident.
     * @return The HTTP response code indicating the status of the upload.
     */
    public int uploadTraffic(String incidentJsonString){
        try {
            URL url = new URL(URL_INCIDENTS_API);
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
    /**
     * This method generates a JSON String representation of an incident.
     *
     * @param id          The incident ID.
     * @param type        The type of incident.
     * @param reason      The reason for the incident.
     * @param dateCreated The creation date of the incident.
     * @param deathDate   The death date of the incident.
     * @param coordinates The coordinates of the incident location.
     * @return The JSON representation of the incident.
     */
    public String generateJsonTraffic(String id, String type, String reason, String dateCreated, String deathDate, String coordinates, String coordinate2) {
        return "{\"_id\": \"" + id + "\",\"level\": \"" + type + "\",\"reason\": \"" + reason + "\",\"dateCreated\": \"" + dateCreated + "\",\"deathDate\": \"" + deathDate + "\",\"geometry\": {\"type\": \"Point\",\"coordinates\": " + coordinates + coordinate2 + " + }}";
    }
    /**
     * This method returns the last clicked point on the map.
     *
     * @return The last clicked point on the map as a LatLng object.
     */
    public LatLng getLastClickedPoint() {
        return lastClickedPoint;
    }

    /**
     * This method sets the last clicked point on the map.
     *
     * @param point The LatLng object representing the last clicked point on the map.
     */
    public void setLastClickedPoint(LatLng point) {
        lastClickedPoint = point;
    }

    /**
     * This method retrieves the coordinates of the last clicked point as a string.
     *
     * @return The coordinates of the last clicked point in the format "[latitude, longitude]".
     */
    public String getCoordinates(){
        String[] parts = lastClickedPoint.toString().split("[=,]");
        String latitude = parts[1].trim();
        String longitude = parts[3].trim();
        System.out.println("ESto mas"+latitude);
        return "[" + latitude + ", " + longitude + "]";
    }

    public String getCoordinates2(){
        String[] parts = lastClickedPoint.toString().split("[=,]");
        String latitude = parts[1].trim();
        String longitude = parts[3].trim();
        System.out.println("ESto mas"+latitude);
        return "[" + latitude + ", " + longitude + "]";
    }
    /**
     * This method retrieves the instance of the TrafficUploader class.
     *
     * @return The instance of the TrafficUploader class.
     */
    public static TrafficUploader getInstance() {
        if (instance == null) instance = new TrafficUploader();
        return instance;
    }
}


