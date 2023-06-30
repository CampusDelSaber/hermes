package com.isc.hermes.controller;

import android.widget.Toast;

import com.isc.hermes.R;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.GraphManager;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.TransportationType;
import com.isc.hermes.requests.overpass.IntersectionRequest;
import com.isc.hermes.requests.overpass.WayRequest;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * This is the controller class for the graph.
 *
 * <p>
 *     Contains information of ways and intersection in a determinate area of the map.
 *     This class uses the requests to Overpass API to get this data.
 *     A midpoint is calculated between the start and destination points to determinate
 *     a round area.
 * </p>
 *
 * {@link CoordinatesDistanceCalculator} is used to calculate the radius of the area.<br>
 * {@link IntersectionRequest} is used for the Overpass API intersection request.<br>
 * {@link WayRequest} is used for the Overpass API ways request.<br>
 */
public class GraphController {
    private Graph graph;
    private LatLng start;
    private LatLng destination;
    private LatLng midpoint;
    private Node startNode;
    private Node destinationNode;
    private final CoordinatesDistanceCalculator calculator;
    private final IntersectionRequest intersectionRequest;
    private final WayRequest wayRequest;

    /**
     * Constructor method.
     *
     * @param start Is the route start point.
     * @param destination Is the route destination point.
     */
    public GraphController(LatLng start, LatLng destination) {
        this.graph = new Graph();
        this.start = start;
        this.destination = destination;
        this.midpoint = calculateMidpoint();
        this.calculator = new CoordinatesDistanceCalculator();
        this.startNode = new Node("start", start.getLatitude(), start.getLongitude());
        this.destinationNode = new Node("destination", destination.getLatitude(), destination.getLongitude());
        this.intersectionRequest = new IntersectionRequest();
        this.wayRequest = new WayRequest();
    }

    /**
     * Method to build the graph with the intersections and the ways.
     *
     * @throws JSONException If there is an issue with parsing the JSON data.
     */
    public void buildGraph(TransportationType transportationType) throws JSONException {
        int radius = (int) (getRadius() * 1000) + 200;
        loadIntersections(intersectionRequest.getIntersections(midpoint.getLatitude(), midpoint.getLongitude(), radius));
        loadEdges(wayRequest.getEdges(midpoint.getLatitude(), midpoint.getLongitude(), radius), transportationType);
        GraphManager graphManager = GraphManager.getInstance();
        graphManager.setGraph(graph);
        try {
            graphManager.disconnectBlockedStreets(start, destination);
            graph = graphManager.getGraph();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
       
    }

    /**
     * This method calculate the mid point between the start and destination points.
     *
     * @return A LatLng object.
     */
    private LatLng calculateMidpoint() {
        double lat1Rad = Math.toRadians(start.getLatitude());
        double lon1Rad = Math.toRadians(start.getLongitude());

        double lat2Rad = Math.toRadians(destination.getLatitude());
        double lon2Rad = Math.toRadians(destination.getLongitude());

        double x1 = Math.cos(lat1Rad) * Math.cos(lon1Rad);
        double y1 = Math.cos(lat1Rad) * Math.sin(lon1Rad);
        double z1 = Math.sin(lat1Rad);

        double x2 = Math.cos(lat2Rad) * Math.cos(lon2Rad);
        double y2 = Math.cos(lat2Rad) * Math.sin(lon2Rad);
        double z2 = Math.sin(lat2Rad);

        double xAvg = (x1 + x2) / 2;
        double yAvg = (y1 + y2) / 2;
        double zAvg = (z1 + z2) / 2;

        double latAvgRad = Math.atan2(zAvg, Math.sqrt(xAvg * xAvg + yAvg * yAvg));
        double lonAvgRad = Math.atan2(yAvg, xAvg);

        double latAvg = Math.toDegrees(latAvgRad);
        double lonAvg = Math.toDegrees(lonAvgRad);


        return new LatLng(latAvg, lonAvg);
    }

    /**
     * Method to calculated the radius of the area in kilometers.
     *
     * @return A double.
     */
    private double getRadius() {
        Node midpointNode = new Node("midpoint", midpoint.getLatitude(), midpoint.getLongitude());
        return calculator.calculateDistance(startNode, midpointNode);
    }

    /**
     * This method load the intersections into the round area.
     *
     * @param intersectionsJson Is a Json with the information of streets intersections.
     * @throws JSONException If there is an issue with parsing the JSON data.
     */
    public void loadIntersections(String intersectionsJson) throws JSONException {
        if (intersectionsJson != null) {
            JSONObject json = new JSONObject(intersectionsJson);
            JSONArray intersections = json.getJSONArray("elements");

            for (int i = 0; i < intersections.length(); i++) {
                Node node = new Node(String.valueOf(intersections.getJSONObject(i).get("id")),
                        (Double) intersections.getJSONObject(i).getDouble("lat"),
                        (Double) intersections.getJSONObject(i).getDouble("lon"));
                graph.addNode(node);
            }

            addStartNode();
            addDestinationNode();
        }

    }

    /**
     * This method load the intersection edges into the round area.
     *
     * @param edgesJson Contain the ways data.
     * @throws JSONException If there is an issue with parsing the JSON data.
     */
    private void loadEdges(String edgesJson, TransportationType transportationType) throws JSONException {
        if (edgesJson != null) {
            JSONObject json = new JSONObject(edgesJson);
            JSONArray edges = json.getJSONArray("elements");
            Node lastNode;
            Node currentNode;

            for (int i = 0; i < edges.length(); i++) {
                for (int j = 1 ; j< edges.getJSONObject(i).getJSONArray("nodes").length() ; j++) {
                    lastNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j-1)));
                    currentNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j)));
                    if (currentNode != null && lastNode != null) {
                        if (transportationType.equals(TransportationType.WALK))
                            lastNode.addBidirectionalEdge(
                                currentNode, calculator.calculateDistance(lastNode, currentNode)
                            );
                        else
                            lastNode.addUnidirectionalEdge(
                                currentNode, calculator.calculateDistance(lastNode, currentNode)
                            );
                    }

                }
            }
        }
    }

    /**
     * Method to add  the start node to graph with an edge.
     *
     * @throws JSONException If there is an issue with parsing the JSON data.
     */
    private void addStartNode() throws JSONException {
        String response = intersectionRequest.getIntersections(start.getLatitude(), startNode.getLongitude(), 210);
        if (response != null) {
            JSONObject json = new JSONObject(response);
            JSONArray intersection = json.getJSONArray("elements");
            Node node;

            for (int i = 0; i < intersection.length(); i++) {
                for (int j = 1 ; j< intersection.length() ; j++) {
                    node = graph.getNode(String.valueOf(intersection.getJSONObject(i).get("id")));
                    if(node != null) {
                        startNode.addBidirectionalEdge(node, calculator.calculateDistance(startNode, node));
                        graph.addNode(startNode);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Method to add the destination node to graph with one edge
     *
     * @throws JSONException If there is an issue with parsing the JSON data.
     */
    private void addDestinationNode() throws JSONException {
        String response = intersectionRequest.getIntersections(destinationNode.getLatitude(), destinationNode.getLongitude(), 150);
        if (response != null) {
            JSONObject json = new JSONObject(response);
            JSONArray intersection = json.getJSONArray("elements");
            Node node;

            for (int i = 0; i < intersection.length(); i++) {
                for (int j = 1 ; j< intersection.length() ; j++) {
                    node = graph.getNode(String.valueOf(intersection.getJSONObject(i).get("id")));
                    if(node != null) {
                        destinationNode.addBidirectionalEdge(node, calculator.calculateDistance(destinationNode, node));
                        graph.addNode(destinationNode);
                        return;
                    }
                }
            }
        }
    }

    /**
     * This is a getter method to the Graph.
     *
     * @return A graph object with the intersections and ways data.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * This is a getter method to the start node.
     *
     * @return A node object.
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * This is a getter method to the destination node.
     *
     * @return A node object.
     */
    public Node getDestinationNode() {
        return destinationNode;
    }

}