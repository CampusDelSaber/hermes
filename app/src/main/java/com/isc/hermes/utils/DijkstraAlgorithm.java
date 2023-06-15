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
    public double calculateDistance(Node node1, Node node2) {
        double lat1 = Math.toRadians(node1.getLatitude());
        double lon1 = Math.toRadians(node1.getLongitude());
        double lat2 = Math.toRadians(node2.getLatitude());
        double lon2 = Math.toRadians(node2.getLongitude());

        final double R = 6371.0; // Earth's radius in kilometers

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public List<Node> getShortestPath(Graph graph, Node source, Node destination) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> parents = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (Node node : graph.getNodes().values()) {
            if (node == source) {
                distances.put(node, 0.0);
            } else {
                distances.put(node, Double.MAX_VALUE);
            }
            parents.put(node, null);
            queue.add(node);
        }

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

    public static DijkstraAlgorithm getInstance() {
        if (instance == null) instance = new DijkstraAlgorithm();
        return instance;
    }
}
