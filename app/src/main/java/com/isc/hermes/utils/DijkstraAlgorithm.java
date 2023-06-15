package com.isc.hermes.utils;

import com.isc.hermes.model.graph.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class implements Dijkstra's algorithm for
 * finding the shortest path between two nodes in a graph.
 */
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

        return calculateShortestPath(destination, distances, parents, queue);
    }

    /**
     * Initializes the distances and parents maps for each node in the graph.
     *
     * @param graph     The graph containing the nodes.
     * @param source    The source node from which the shortest path is calculated.
     * @param distances A map to store the distances from the source node to each node.
     * @param parents   A map to store the parent node of each node in the shortest path.
     * @param queue       The queue with the nodes
     */
    public void initializeDistancesAndParents(
            Graph graph, Node source, Map<Node, Double> distances, Map<Node, Node> parents,
            PriorityQueue<Node> queue
    ) {
        for (Node node : graph.getNodes().values()) {
            if (node == source) distances.put(node, 0.0);
            else distances.put(node, Double.MAX_VALUE);
            parents.put(node, null);
            queue.add(node);
        }
    }

    /**
     * Calculates the shortest path in the given graph from the source node to the destination node.
     *
     * @param destination The destination node.
     * @param distances   A map of distances from the source node to each node.
     * @param parents     A map of parent nodes for each node in the shortest path.
     * @param queue       The queue with the nodes
     * @return The list of nodes representing the shortest path from source to destination.
     */
    public List<Node> calculateShortestPath(
            Node destination, Map<Node, Double> distances, Map<Node, Node> parents,
            PriorityQueue<Node> queue
    ) {
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current == destination) break;
            assert current != null;
            for (Edge edge : current.getEdges()) {
                Node adjacent = edge.getDestination();
                double newDistance = distances.get(current) + edge.getWeight();

                if (newDistance < distances.get(adjacent)) {
                    distances.put(adjacent, newDistance);
                    parents.put(adjacent, current);

                    queue.remove(adjacent);
                    queue.add(adjacent);
                }
            }
        }
        return retrieveShortestPath(destination, parents);
    }

    /**
     * Retrieves the shortest path from the destination node to the source node based on the parents map.
     *
     * @param destination The destination node.
     * @param parents     A map of parent nodes for each node in the shortest path.
     * @return The list of nodes representing the shortest path from source to destination.
     */
    public List<Node> retrieveShortestPath(Node destination, Map<Node, Node> parents) {
        List<Node> shortestPath = new ArrayList<>();
        Node current = destination;

        while (current != null) {
            shortestPath.add(0, current);
            current = parents.get(current);
        }

        return shortestPath;
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


    /**
     * Gets the singleton instance of the DijkstraAlgorithm class.
     *
     * @return the instance of DijkstraAlgorithm
     */
    public static DijkstraAlgorithm getInstance() {
        if (instance == null) instance = new DijkstraAlgorithm();
        return instance;
    }
}
