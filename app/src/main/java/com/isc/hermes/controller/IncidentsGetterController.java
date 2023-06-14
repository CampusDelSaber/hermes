package com.isc.hermes.controller;

import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.utils.ISO8601Converter;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncidentsGetterController {
    private static final double BASE_RADIUS = 1000.0; // Base radius in meters
    private static final int MAX_ZOOM = 18; // Maximum supported zoom level
    private static final double MIN_DISTANCE = 10.0; // Minimum distance in kilometers
    private final ISO8601Converter iso8601Converter;

    public IncidentsGetterController() {
        this.iso8601Converter = ISO8601Converter.getInstance();
    }

    public List<Incident> getIncidentsWithinRadius(MapboxMap mapboxMap) throws IOException {
        LatLng cameraFocus = mapboxMap.getCameraPosition().target;
        int zoom = (int) mapboxMap.getCameraPosition().zoom;

        double radius = calculateRadiusFromZoom(zoom);

        // Construct the URL for the API request
        String apiUrl = buildApiUrl(cameraFocus, radius);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Parse the response and retrieve the incidents
        List<Incident> incidents = parseIncidentResponse("{}");

        return incidents;
    }

    private double calculateRadiusFromZoom(int zoom) {
        double baseRadius = BASE_RADIUS;
        int maxZoom = MAX_ZOOM;

        return baseRadius * Math.pow(2, (maxZoom - zoom));
    }

    private Point toPoint(LatLng latLng) {
        Position position = new Position(latLng.getLongitude(), latLng.getLatitude());
        return new Point(position);
    }

    private String buildApiUrl(LatLng latLng, double radius) {
        // Build the URL for the API request based on the center point and radius
        String baseUrl = "https://example.com/incidents";
        String latitude = Double.toString(latLng.getLatitude());
        String longitude = Double.toString(latLng.getLongitude());
        String radiusString = Double.toString(radius / 1000.0); // Convert radius from meters to kilometers

        return String.format("%s?latitude=%s&longitude=%s&radius=%s", baseUrl, latitude, longitude, radiusString);
    }

    private List<Incident> parseIncidentResponse(String jsonResponse) throws JSONException {
        List<Incident> incidents = new ArrayList<>();

        JSONArray incidentsArray = new JSONArray(jsonResponse);
        for (int i = 0; i < incidentsArray.length(); i++) {
            JSONObject incidentObj = incidentsArray.getJSONObject(i);
            String id = incidentObj.getString("id");
            String type = incidentObj.getString("type");
            String reason = incidentObj.getString("reason");
            Date dateCreated = iso8601Converter.convertISO8601ToDate(incidentObj.getString("dateCreated"));
            Date deathDate = iso8601Converter.convertISO8601ToDate(incidentObj.getString("dateCreated"));
            JSONObject geometry = incidentObj.getJSONObject("geometry");

            Incident incident = new Incident(id, type, reason, dateCreated, deathDate, geometry);
            incidents.add(incident);
        }

        return incidents;
    }
}
