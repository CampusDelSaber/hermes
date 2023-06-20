package com.isc.hermes.database;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.DijkstraAlgorithm;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TrafficUploader {

    private static TrafficUploader instance;
    private LatLng lastClickedPoint;
    private final String URL_INCIDENTS_API= "https://api-rest-hermes.onrender.com/incidents";
    private DijkstraAlgorithm dijkstraAlgorithm;
    private Graph graph;

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
    public String generateJsonTraffic(String id, String type, String reason, String dateCreated, String deathDate, String coordinates ) {
        return "{\"_id\": \"" + id + "\",\"type\": \"" + type + "\",\"reason\": \"" + reason + "\",\"dateCreated\": \"" + dateCreated + "\",\"deathDate\": \"" + deathDate + "\",\"geometry\": {\"type\": \"Point\",\"coordinates\": " + coordinates + "}}";
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


    public String getCoordinates() {
        if (dijkstraAlgorithm == null) {
            dijkstraAlgorithm = new DijkstraAlgorithm();
        }
        if (graph == null) {
            graph = new Graph();
        }

        Node node1 = new Node("Point 2", lastClickedPoint.getLatitude(), lastClickedPoint.getLongitude());

        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null, null).getCurrentLocationModel();

        Node node2 = new Node("Point 1", currentLocation.getLatitude(), currentLocation.getLongitude());
        graph.addNode(node1);
        graph.addNode(node2);
        System.out.println("Nodo 1 "+ node1.getLongitude());
        System.out.println("Nodo 2 " + node2.getLongitude());

        String trafficLine = String.valueOf(dijkstraAlgorithm.getGeoJsonRoutes(graph, node1, node2));
        System.out.println("ESto mas" + trafficLine);
        return "[" + trafficLine + "]";

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


