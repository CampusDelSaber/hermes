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

/**
 * This class manage the actions that an geo graph can do on the application.
 * For example disconnect its edges or made itself an bidirectional graph.
 */
public class GraphManager {
    private static GraphManager graphManager;
    private Graph graph;

    private double radius = 100;

    /**
     * Constructor method private to apply singleton pattern.
     */
    private GraphManager() {
    }

    /**
     * This method obtain an instance of the graph manager to apply singleton pattern
     * and don't have much instances of the this class.
     * @return
     */
    public static GraphManager getInstance() {
        if (graphManager == null) {
            graphManager = new GraphManager();
        }
        return graphManager;
    }

    /**
     * This method change the graph being managed.
     * @param graph our own graph class.
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * This method search all incidents that are inside on the graph.
     *
     * @param origin the beginning of the graph on latitude and longitude point.
     * @param destination the final of the graph on latitude and longitude point
     *
     * @return an JSONArray with all incidents that are inside on the graph.
     */
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

    /**
     * This method verify if an point are inside the graph.
     *
     * @param origin the beginning of the graph on latitude and longitude point.
     * @param destination the final of the graph on latitude and longitude point
     * @param point the point that will verify if are inside.
     * @return an boolean according if the point are or not on the graph.
     */
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

    /**
     * This method search the nearby nodes based on an incident point (latitude, longitude)
     *
     * This method iterate each node of the graph to search the closest node,
     * with the closest node compare the edges destination,
     * if they are inside the radio is consider a nearby node
     * @param incident the point to search nodes nearby.
     * @return an list with the IDs of the nearby nodes.
     */
    public List<String> searchNodesNearby(Point incident) {
        Node referenceNode = null;
        double minDistance = Double.MAX_VALUE;
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
            nearbyNodesIDs.add(referenceNode.getId());
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

    public boolean isOnStreet(Node pointA, Node pointB, Point pointX) {
        double latA = pointA.getLatitude();
        double latB = pointB.getLatitude();
        double latX = pointX.latitude();
        double lngA = pointA.getLongitude();
        double lngB = pointB.getLongitude();
        double lngX = pointX.longitude();

        //relative position
        double fraction = ((latX - latA) * (latB - latA) + (lngX - lngA) * (lngB - lngA)) /
                ((latB - latA) * (latB - latA) + (lngB - lngA) * (lngB - lngA));

        return fraction >= 0 && fraction <= 1;
    }

    public void disconnectBlockedStreets(LatLng origin, LatLng destination) throws JSONException, ExecutionException, InterruptedException {
        JSONArray incidents = searchIncidentsOnTheGraph(origin, destination);
        IncidentsDataProcessor incidentsDataProcessor = IncidentsDataProcessor.getInstance();

        for (int i = 0; i <= incidents.length(); i++) {
            List<String> nearbyNodes = searchNodesNearby((Point) incidents.get(i));
            for (int j = 0; j < nearbyNodes.size(); j++) {
                for (int k = j + 1; k < nearbyNodes.size(); k++) {
                    if (isOnStreet(graph.getNode(nearbyNodes.get(j)), graph.getNode(nearbyNodes.get(k)), (Point) incidents.get(i))) {
                        graph.getNode(nearbyNodes.get(j)).removeEdgeTo(graph.getNode(nearbyNodes.get(k)));
                        graph.getNode(nearbyNodes.get(k)).removeEdgeTo(graph.getNode(nearbyNodes.get(j)));
                        System.out.println(nearbyNodes.get(j) + " disconnect from " + nearbyNodes.get(k));
                    }
                }
            }
        }
    }
}
