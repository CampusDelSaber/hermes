package com.isc.hermes.model.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a graph.
 */
public class Graph {
    private Map<String, Node> nodes;

    /**
     * Creates a new graph.
     */
    public Graph() {
        nodes = new HashMap<>();
    }

    /**
     * Adds a node to the graph.
     *
     * @param node the node to add
     */
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    /**
     * Returns the node with the specified ID.
     *
     * @param id the ID of the node
     * @return the node with the specified ID, or null if not found
     */
    public Node getNode(String id) {
        return nodes.get(id);
    }

    /**
     * Returns the whole nodes of the graph stored in the map
     * @return the nodes map
     */
    public Map<String, Node> getNodes() {
        return nodes;
    }

    /**
     * Prints the relationships between nodes in the graph.
     */
    public void printGraph() {
        System.out.println("Graph:");
        for (Node node : nodes.values()) {
            System.out.println("Node: " + node.getId());
            List<Edge> edges = node.getEdges();
            if (edges.isEmpty()) {
                System.out.println("  No edges");
            } else {
                System.out.println("  Edges:");
                for (Edge edge : edges) {
                    System.out.println("    --");
                    System.out.println("    Source: " + edge.getSource().getId());
                    System.out.println("    Destination: " + edge.getDestination().getId());
                    System.out.println("    Weight: " + edge.getWeight());
                }
            }
        }
    }
}