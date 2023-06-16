package com.isc.hermes.model.graph;

import java.util.HashMap;
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


}