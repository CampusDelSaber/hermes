package com.isc.hermes.model.graph;

import androidx.annotation.NonNull;

import com.isc.hermes.utils.CoordinatesDistanceCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a node in a graph with its associated latitude, longitude, and edges.
 */
public class Node {
    private String id;
    private double latitude;
    private double longitude;
    private List<Edge> edges;

    /**
     * Constructs a Node object with the specified id, latitude, and longitude.
     *
     * @param id        the identifier of the node
     * @param latitude  the latitude of the node's location
     * @param longitude the longitude of the node's location
     */
    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
    }

    /**
     * Retrieves the identifier of the node.
     *
     * @return the node's identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the latitude of the node's location.
     *
     * @return the latitude value
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Retrieves the longitude of the node's location.
     *
     * @return the longitude value
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Retrieves the list of edges connected to the node.
     *
     * @return the list of edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Adds a unidirectional edge from the current node to the specified destination node with the given weight.
     *
     * @param destination the destination node of the edge
     * @param weight      the weight of the edge
     */
    public void addUnidirectionalEdge(Node destination, double weight) {
        Edge edge = new Edge(this, destination, weight);
        edges.add(edge);
    }

    /**
     * Adds a bidirectional edge between the current node and the specified destination node with the given weight.
     * It creates two unidirectional edges: from the current node to the destination, and vice versa.
     *
     * @param destination the destination node of the edge
     * @param weight      the weight of the edge
     */
    public void addBidirectionalEdge(Node destination, double weight) {
        addUnidirectionalEdge(destination, weight);
        destination.addUnidirectionalEdge(this, weight);
    }

    /**
     * Adds a unidirectional edge from the current node to the specified destination node. The weight of the edge
     * is automatically calculated based on the geographic coordinates of the nodes using the Haversine formula.
     *
     * @param destination the destination node of the edge
     */
    public void addUnidirectionalEdge(Node destination) {
        double weight = CoordinatesDistanceCalculator.getInstance()
                .calculateDistance(this, destination);
        Edge edge = new Edge(this, destination, weight);
        edges.add(edge);
    }

    /**
     * Adds a bidirectional edge between the current node and the specified destination node.
     * The weight of the edge is automatically calculated based on the geographic coordinates of the nodes
     * using the Haversine formula. It creates two unidirectional edges: from the current node to the destination,
     * and vice versa.
     *
     * @param destination the destination node of the edge
     */
    public void addBidirectionalEdge(Node destination) {
        addUnidirectionalEdge(destination);
        destination.addUnidirectionalEdge(this);
    }

    /**
     * Removes the edge from the current node to the specified destination node.
     *
     * @param destination the destination node of the edge to be removed
     * @return true if the edge is successfully removed, false otherwise
     */
    public boolean removeEdgeTo(Node destination) {
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getDestination() == destination) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return id;
    }
}
