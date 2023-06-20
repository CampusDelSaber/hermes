package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Edge;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;

import java.util.List;
import java.util.Map;

public class GraphUtils {
    private static GraphUtils instance;
    public void traverseGraph(Graph graph) {
        Map<String, Node> nodes = graph.getNodes();

        for (Node node : nodes.values()) {
            System.out.println("Street names in " + node.getId() + ":");

            List<Edge> edges = node.getEdges();
            for (Edge edge : edges) {
                System.out.println(edge.getSource().getId() + " -> " + edge.getDestination().getId());
            }
            System.out.println();
        }
    }
    public static GraphUtils getInstance(){
        if (instance == null){
            instance = new GraphUtils();
        }
        return instance;
    }

}
