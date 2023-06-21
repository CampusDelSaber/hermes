package com.isc.hermes.controller;

import android.annotation.SuppressLint;

import com.isc.hermes.model.graph.Edge;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.requests.overpass.IntersectionRequest;
import com.isc.hermes.requests.overpass.WayRequest;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphController {
    private Graph graph;
    private LatLng start;
    private LatLng destination;
    private LatLng midpoint;
    private Node startNode;
    private Node destinationNode;
    private CoordinatesDistanceCalculator calculator;
    IntersectionRequest intersectionRequest;
    WayRequest wayRequest;

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

    public void buildGraph() throws JSONException {
        int radius = (int) (getRadius() * 1000);
        loadIntersections(intersectionRequest.getIntersections(midpoint.getLatitude(), midpoint.getLongitude(), radius));
        loadEdges(wayRequest.getEdges(midpoint.getLatitude(), midpoint.getLongitude(), radius));
    }

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

    private double getRadius() {
        Node midpointNode = new Node("midpoint", midpoint.getLatitude(), midpoint.getLongitude());
        return calculator.calculateDistance(startNode, midpointNode);
    }

    public Graph loadIntersections(String intersectionsJson) throws JSONException {
        if (intersectionsJson != null) {
            JSONObject json = new JSONObject(intersectionsJson);
            JSONArray intersections = json.getJSONArray("elements");

            for (int i = 0; i < intersections.length(); i++) {
                Node node = new Node(String.valueOf(intersections.getJSONObject(i).get("id")),
                        (Double) intersections.getJSONObject(i).get("lat"),
                        (Double) intersections.getJSONObject(i).get("lon"));
                graph.addNode(node);
            }

            graph.addNode(startNode);
            graph.addNode(destinationNode);
        }

        System.out.println(graph.getNodes().size());

        return graph;
    }

    private void loadEdges(String edgesJson) throws JSONException {
        if (edgesJson != null) {
            JSONObject json = new JSONObject(edgesJson);
            JSONArray edges = json.getJSONArray("elements");
            Node lastNode;
            Node currentNode;

            for (int i = 0; i < edges.length(); i++) {
                for (int j = 1 ; j< edges.getJSONObject(i).getJSONArray("nodes").length() ; j++) {
                    lastNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j-1)));
                    currentNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j)));
                    if(currentNode != null && lastNode != null) {
                        lastNode.addBidirectionalEdge(currentNode);
                    }
                }
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }
}