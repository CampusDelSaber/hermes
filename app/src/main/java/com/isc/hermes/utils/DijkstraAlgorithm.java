package com.isc.hermes.utils;

import com.isc.hermes.model.graph.*;
import com.isc.hermes.model.navigation.Route;

import java.util.*;

/**
 * This class implements Dijkstra's algorithm for
 * finding the shortest path between two nodes in a graph.
 */
public class DijkstraAlgorithm {
    private static DijkstraAlgorithm instance;
    private static final int MAX_ROUTE_ALTERNATIVES = 3;

    /**
     * Calculates the shortest path from the source node to the destination node in the given graph.
     *
     * @param graph       The graph in which to find the shortest path.
     * @param source      The source node.
     * @param destination The destination node.
     * @return A map with route alternatives as keys and their corresponding list of nodes as values.
     */
    public Map<String, List<Node>> getPathAlternatives(Graph graph, Node source, Node destination) {
        Map<String, List<Node>> routeAlternatives = new HashMap<>();
        PriorityQueue<Route> routeQueue =
                new PriorityQueue<>(Comparator.comparingDouble(Route::getTotalDistance));
        routeQueue.add(new Route(getShortestPath(graph, source, destination)));
        routeQueue.add(new Route(source));

        while (!routeQueue.isEmpty() && routeAlternatives.size() < MAX_ROUTE_ALTERNATIVES) {
            Route currentRoute = routeQueue.poll();

            if (isDestinationReached(destination, currentRoute, routeAlternatives)) {
                routeAlternatives.put(getRouteKey(routeAlternatives.size()), currentRoute.getPath());
                continue;
            }
            exploreNeighbors(currentRoute, routeQueue);
        }

        return routeAlternatives;
    }

    /**
     * Checks if the current route has reached the destination node and if the route is not already present in the
     * routeAlternatives map.
     *
     * @param destination        The destination node.
     * @param currentRoute       The current route being evaluated.
     * @param routeAlternatives  The map of route alternatives.
     * @return True if the destination is reached and the route is not already present, false otherwise.
     */
    private boolean isDestinationReached(
            Node destination, Route currentRoute, Map<String, List<Node>> routeAlternatives
    ) {
        Node currentNode = currentRoute.getCurrentNode();
        List<Node> currentRoutePath = currentRoute.getPath();
        return currentNode == destination && !routeAlternatives.containsValue(currentRoutePath);
    }

    /**
     * Explores the neighbors of the current node in the graph and adds new routes to the routeQueue.
     *
     * @param currentRoute  The current route being evaluated.
     * @param routeQueue    The priority queue of routes.
     */
    private void exploreNeighbors(
            Route currentRoute, PriorityQueue<Route> routeQueue
    ) {
        Node currentNode = currentRoute.getCurrentNode();

        for (Edge edge : currentNode.getEdges()) {
            Node nextNode = edge.getDestination();
            double edgeWeight = edge.getWeight();

            if (!currentRoute.visitedNode(nextNode)) {
                Route newRoute = new Route(currentRoute);
                newRoute.addNode(nextNode, edgeWeight);
                routeQueue.add(newRoute);
            }
        }
    }

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
     * @param queue     The queue with the nodes.
     */
    private void initializeDistancesAndParents(
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
     * Calculates the shortest path in the given graph from the source node to the destination node.
     *
     * @param destination The destination node.
     * @param distances   A map of distances from the source node to each node.
     * @param parents     A map of parent nodes for each node in the shortest path.
     * @param queue       The queue with the nodes.
     * @return The list of nodes representing the shortest path from source to destination.
     */
    private List<Node> calculateShortestPath(
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
    private List<Node> retrieveShortestPath(Node destination, Map<Node, Node> parents) {
        List<Node> shortestPath = new ArrayList<>();
        Node current = destination;

        while (current != null) {
            shortestPath.add(0, current);
            current = parents.get(current);
        }

        return shortestPath;
    }

    /**
     * Removes the edges of the nodes in the given shortest path from the graph.
     *
     * @param graph       The graph from which to remove the edges.
     * @param shortestPath The shortest path with nodes whose edges should be removed.
     */
    private void removeEdgesFromGraph(Graph graph, List<Node> shortestPath) {
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Node currentNode = shortestPath.get(i);
            Node nextNode = shortestPath.get(i + 1);
            currentNode.removeEdgeTo(nextNode);
        }
    }

    /**
     * Gets the singleton instance of the DijkstraAlgorithm class.
     *
     * @return the instance of DijkstraAlgorithm.
     */
    public static DijkstraAlgorithm getInstance() {
        if (instance == null) {
            instance = new DijkstraAlgorithm();
        }
        return instance;
    }

    /**
     * Generates a route key based on the index of the route.
     *
     * @param index The index of the route.
     * @return The route key.
     */
    private String getRouteKey(int index) {
        return "Route " + (char) ('A' + index);
    }
}