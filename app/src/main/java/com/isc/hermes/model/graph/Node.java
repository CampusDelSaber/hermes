package com.isc.hermes.model.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private double latitude;
    private double longitude;
    private List<Edge> edges;

    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addUnidirectionalEdge(Node destination, double weight) {
        Edge edge = new Edge(this, destination, weight);
        edges.add(edge);
    }

    public void addBidirectionalEdge(Node destination, double weight) {
        addUnidirectionalEdge(destination, weight);
        destination.addUnidirectionalEdge(this, weight);
    }
}
