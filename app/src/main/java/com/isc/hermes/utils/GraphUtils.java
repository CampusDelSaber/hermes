package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Edge;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;

import java.util.List;
import java.util.Map;
/**
 * This is a utility class for working with graphs.
 */
public class GraphUtils {
    private static GraphUtils instance;
    /**
     * This method traverses the given graph
     * and returns it as a string
     *
     * @param graph The graph to traverse
     * @return A string representation of the graph
     */
    public String traverseGraph(Graph graph) {
        Map<String, Node> nodes = graph.getNodes();
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes.values()) {
            System.out.println("Street names in " + node.getId() + ":");
            stringBuilder.append("Street names in " + node.getId() + ":");
            List<Edge> edges = node.getEdges();
            for (Edge edge : edges) {
                System.out.println(edge.getSource().getId() + " -> " + edge.getDestination().getId());
                stringBuilder.append(edge.getSource().getId() + " -> " + edge.getDestination().getId());
            }
            System.out.println("----*-----");
            stringBuilder.append("----*-----");
        }
        return stringBuilder.toString();
    }

    /**
     * This method creates a new bidirectional
     * graph based on the original graph
     *
     * @param originalGraph The original graph
     * @return A new graph with bidirectional edges
     */
    public Graph createBidirectionalGraph(Graph originalGraph) {
        Graph newGraph = copyNodes(originalGraph);
        addBidirectionalEdges(newGraph, originalGraph);
        return newGraph;
    }
    /**
     * This method copies the nodes from the original
     * graph to a new graph
     *
     *@param originalGraph The original graph
     * @return A new graph with copied nodes
     */
    private Graph copyNodes(Graph originalGraph) {
        Graph newGraph = new Graph();
        Map<String, Node> originalNodes = originalGraph.getNodes();
        for (Node originalNode : originalNodes.values()) {
            Node newNode = new Node(originalNode.getId(), originalNode.getLatitude(), originalNode.getLongitude());
            newGraph.addNode(newNode);
        }
        return newGraph;
    }
    /**
     * This method adds bidirectional edges to the new
     * graph based on the original graph's edges
     *
     * @param newGraph      The new graph to add bidirectional edges to
     * @param originalGraph The original graph to get edges from
     */
    private void addBidirectionalEdges(Graph newGraph, Graph originalGraph) {
        Map<String, Node> originalNodes = originalGraph.getNodes();
        for (Node originalNode : originalNodes.values()) {
            Node newNode = newGraph.getNode(originalNode.getId());
            List<Edge> originalEdges = originalNode.getEdges();
            for (Edge originalEdge : originalEdges) {
                Node originalDestination = originalEdge.getDestination();
                double originalWeight = originalEdge.getWeight();
                Node newDestination = newGraph.getNode(originalDestination.getId());
                newNode.addBidirectionalEdge(newDestination, originalWeight);
            }
        }
    }
    /**
     * This method retrieves the singleton instance of GraphUtils.
     *
     * @return The singleton instance of GraphUtils.
     */
    public static GraphUtils getInstance(){
        if (instance == null){
            instance = new GraphUtils();
        }
        return instance;
    }

}
