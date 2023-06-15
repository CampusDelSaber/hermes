package com.isc.hermes.model.graph;

/**
 * This class represents an edge between two nodes in the graph.
 */
public class Edge {
    private Node source;
    private Node destination;
    private double weight;

    /**
     * Creates a new edge with the specified source, destination, and weight.
     *
     * @param source      the source node of the edge
     * @param destination the destination node of the edge
     * @param weight      the weight of the edge
     */
    public Edge(Node source, Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Returns the source node of the edge.
     *
     * @return the source node of the edge
     */
    public Node getSource() {
        return source;
    }

    /**
     * Returns the destination node of the edge.
     *
     * @return the destination node of the edge
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * Returns the weight of the edge.
     *
     * @return the weight of the edge
     */
    public double getWeight() {
        return weight;
    }
}
