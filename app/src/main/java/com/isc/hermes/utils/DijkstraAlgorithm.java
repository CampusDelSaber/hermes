package com.isc.hermes.utils;

import com.isc.hermes.model.graph.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
    private static DijkstraAlgorithm instance;

    /**
     * Calculates the shortest path from the source node to the destination node in the given graph.
     *
     * @param graph       The graph in which to find the shortest path.
     * @param source      The source node.
     * @param destination The destination node.
     * @return The list of nodes representing the shortest path from the source to the destination.
     */
    public List<Node> getShortestPath(Graph graph, Node source, Node destination) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> parents = new HashMap<>();
        PriorityQueue<Node> queue =
                new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        initializeDistancesAndParents(graph, source, distances, parents, queue);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current == destination) {
                break; // Reached the destination, stop the algorithm
            }

            assert current != null;
            for (Edge edge : current.getEdges()) {
                Node adjacent = edge.getDestination();
                double newDistance = distances.get(current) + edge.getWeight();

                if (newDistance < distances.get(adjacent)) {
                    distances.put(adjacent, newDistance);
                    parents.put(adjacent, current);

                    // Update the priority queue with the new distance
                    queue.remove(adjacent);
                    queue.add(adjacent);
                }
            }
        }

        // Backtrack from the destination to the source to retrieve the shortest path
        List<Node> shortestPath = new ArrayList<>();
        Node current = destination;

        while (current != null) {
            shortestPath.add(0, current);
            current = parents.get(current);
        }

        return shortestPath;
    }

    /**
     * Initializes the distances and parents maps for each node in the graph.
     *
     * @param graph     The graph containing the nodes.
     * @param source    The source node from which the shortest path is calculated.
     * @param distances A map to store the distances from the source node to each node.
     * @param parents   A map to store the parent node of each node in the shortest path.
     */
    public void initializeDistancesAndParents(
            Graph graph, Node source, Map<Node, Double> distances, Map<Node, Node> parents,
            PriorityQueue<Node> queue
    ) {
        for (Node node : graph.getNodes().values()) {
            if (node == source) {
                distances.put(node, 0.0);
            } else {
                distances.put(node, Double.MAX_VALUE);
            }
            parents.put(node, null);
            queue.add(node);
        }
    }

    /**
     * Calculates the shortest path and alternative routes from the source node
     * to the destination node in the given graph.
     *
     * @param graph       The graph in which to find the routes.
     * @param source      The source node.
     * @param destination The destination node.
     * @return A map of routes, where the key represents the route name and the value
     * is the list of nodes representing the route. Key "A" represents the shortest route
     */
    public Map<String, List<Node>> getRoutes(Graph graph, Node source, Node destination) {

        Map<String, List<Node>> routes = new HashMap<>();
        routes.put("A", getShortestPath(graph, source, destination));

        List<Node> alternativeRoute1 = findAlternativeRoute(graph, source, destination);
        if (alternativeRoute1 != null) routes.put("B", alternativeRoute1);
        List<Node> alternativeRoute2 = findAlternativeRoute(graph, source, destination);
        if (alternativeRoute2 != null) routes.put("C", alternativeRoute2);

        return routes;
    }

    /**
     * Finds an alternative route from the source node to the destination node using the given parents map.
     *
     * @param graph       The graph.
     * @param source      The source node.
     * @param destination The destination node.
     * @return The list of nodes representing the alternative route, or null if no alternative route is found.
     */
    private List<Node> findAlternativeRoute(Graph graph, Node source, Node destination) {
        // TODO: Logic to find alternative routes
        return null;
    }


    public static DijkstraAlgorithm getInstance() {
        if (instance == null) instance = new DijkstraAlgorithm();
        return instance;
    }
}
