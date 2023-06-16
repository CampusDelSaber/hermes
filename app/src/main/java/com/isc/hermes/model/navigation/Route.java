package com.isc.hermes.model.navigation;

import com.isc.hermes.model.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Node> path;
    private double totalDistance;

    public Route(Node initialNode) {
        path = new ArrayList<>();
        path.add(initialNode);
        totalDistance = 0.0;
    }

    public Route(Route other) {
        path = new ArrayList<>(other.path);
        totalDistance = other.totalDistance;
    }

    public Route(List<Node> path){
        this.path = path;
        totalDistance = 0.0;
    }

    public void addNode(Node node, double distance) {
        path.add(node);
        totalDistance += distance;
    }

    public List<Node> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public Node getCurrentNode() {
        return path.get(path.size() - 1);
    }

    public boolean visitedNode(Node node) {
        return path.contains(node);
    }
}

