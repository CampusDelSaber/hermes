package com.isc.hermes.model.graph;


import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraphManager {
    private static GraphManager graphManager;
    private Graph graph;

    private double radius = 100;
    private Node referenceNode = null;
    private double minDistance = Double.MAX_VALUE;

    private GraphManager() {
    }

    public static GraphManager getInstance() {
        if (graphManager == null) {
            graphManager = new GraphManager();
        }
        return graphManager;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public JSONArray searchIncidentsOnTheGraph(LatLng origin, LatLng destination) throws ExecutionException, InterruptedException, JSONException {
        IncidentsDataProcessor incidentsDataProcessor = IncidentsDataProcessor.getInstance();
        JSONArray allIncidents = incidentsDataProcessor.getAllIncidents();
        JSONArray incidentsOnBlock = new JSONArray();

        for (int i = 0; i < allIncidents.length(); i++) {
            JSONObject element = allIncidents.getJSONObject(i);
            JSONArray elementGeometry =
                    element.getJSONObject("geometry").getJSONArray("coordinates");

            try {
                Point incidentPoint = Point.fromLngLat(elementGeometry.getDouble(0),
                                                        elementGeometry.getDouble(1));
                if (polygonContainsPoint(origin, destination, incidentPoint)) {
                    incidentsOnBlock.put(element);
                }
            } catch (JSONException ignored) {
            }
        }
        return incidentsOnBlock;
    }

    public boolean polygonContainsPoint(LatLng origin, LatLng destination, Point point) {
        double highestLongitude = origin.getLongitude();
        double lowerLongitude = destination.getLongitude();
        double highestLatitude = origin.getLatitude();
        double lowerLatitude = destination.getLatitude();

        if (origin.getLongitude() < destination.getLongitude()) {
            highestLongitude = destination.getLongitude();
            lowerLongitude = origin.getLongitude();
        }
        if (origin.getLatitude() < destination.getLatitude()) {
            highestLatitude = destination.getLatitude();
            lowerLatitude = origin.getLatitude();
        }

        return (point.longitude() <= highestLongitude && point.longitude() >= lowerLongitude) &&
                (point.latitude() <= highestLatitude && point.latitude() >= lowerLatitude);
    }

    public List<String> searchNodesNearby(Point incident) {
        CoordinatesDistanceCalculator coordinatesDistanceCalculator = CoordinatesDistanceCalculator.getInstance();
        List<String> nearbyNodesIDs = new ArrayList<>();

        for (Node node : graph.getNodes().values()) {
            double distance = coordinatesDistanceCalculator.calculateDistance(node,
                    new Node("incidentPoint", incident.latitude(), incident.longitude()));
            if (distance <= radius && distance < minDistance) {
                referenceNode = node;
                minDistance = distance;
            }
        }

        if (referenceNode != null) {
            List<Edge> nodeReferencesEdges = referenceNode.getEdges();
            for (Edge edge : nodeReferencesEdges) {
                Node nodeIterated = edge.getDestination();
                double distance = coordinatesDistanceCalculator.calculateDistance(
                        referenceNode, nodeIterated);
                if (distance <= radius) nearbyNodesIDs.add(nodeIterated.getId());
            }
        }

        return nearbyNodesIDs;
    }
}
