package com.isc.hermes.database;

import com.mapbox.mapboxsdk.geometry.LatLng;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is for uploading incidents to the server.
 */
public class IncidentsUploader {
    private static IncidentsUploader instance;
    protected LatLng lastClickedPoint;
    protected final String URL_INCIDENTS_API= "https://api-rest-hermes.onrender.com/incidents";

    /**
     * This method uploads an incident in JSON format to the remote server.
     *
     * @param incidentJsonString The JSON representation of the incident.
     * @return The HTTP response code indicating the status of the upload.
     */
    public int uploadIncident(final String incidentJsonString) {
        final AtomicInteger responseCode = new AtomicInteger(-1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

                    responseCode.set(connection.getResponseCode());
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    responseCode.set(-1);
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseCode.get();
    }
    /**
     * This method generates a JSON String representation of an incident.
     *
     *
     * @param type        The type of incident.
     * @param reason      The reason for the incident.
     * @param dateCreated The creation date of the incident.
     * @param deathDate   The death date of the incident.
     * @param typeFigure        The type of geometry figure.
     * @param coordinates The coordinates of the incident location.
     * @return The JSON representation of the incident.
     */
    public String generateJsonIncident(String type, String reason, String dateCreated, String deathDate, String typeFigure, String coordinates) {
        return "{\"type\": \"" + type + "\",\"reason\": \"" + reason + "\",\"dateCreated\": \"" + dateCreated + "\",\"deathDate\": \"" + deathDate + "\",\"geometry\": {\"type\": \""+typeFigure+"\",\"coordinates\": " + coordinates + "}}";
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
        return "[" + latitude + ", " + longitude + "]";
    }
    /**
     * This method retrieves the instance of the IncidentsUploader class.
     *
     * @return The instance of the IncidentsUploader class.
     */
    public static IncidentsUploader getInstance() {
        if (instance == null) instance = new IncidentsUploader();
        return instance;
    }
}
