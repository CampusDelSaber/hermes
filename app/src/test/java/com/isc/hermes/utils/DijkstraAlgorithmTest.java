package com.isc.hermes.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DijkstraAlgorithmTest {
    DijkstraAlgorithm dijkstraAlgorithm;
    @Before()
    public void setUp(){
        dijkstraAlgorithm = DijkstraAlgorithm.getInstance();
    }

    @Test
    public void shortestPathShouldFindShortestPathWhenGraphHasValidPath() {
        // Create a sample graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", 0, 0);
        Node nodeB = new Node("B", 1, 0);
        Node nodeC = new Node("C", 1, 1);
        Node nodeD = new Node("D", 0, 1);
        // A -(1)-  B  -(3)-  D
        // |(2)  (1)|         |
        // C  -  -  -  -  -(1)-

        // Distance From A to B = 1
        // Distance From A to C = 2
        // Distance From B to C = 1
        // Distance From B to D = 3
        // Distance From C to D = 1
        nodeA.addUnidirectionalEdge(nodeB, 1.0);
        nodeA.addUnidirectionalEdge(nodeC, 2.0);
        nodeB.addUnidirectionalEdge(nodeC, 1.0);
        nodeB.addUnidirectionalEdge(nodeD, 3.0);
        nodeC.addUnidirectionalEdge(nodeD, 1.0);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Find the shortest path from nodeA to nodeD
        List<Node> shortestPath = dijkstraAlgorithm.getShortestPath(graph, nodeA, nodeD);

        // The shortest path should be [nodeA, nodeC, nodeD]
        assertEquals(3, shortestPath.size());
        assertEquals(nodeA, shortestPath.get(0));
        assertEquals(nodeC, shortestPath.get(1));
        assertEquals(nodeD, shortestPath.get(2));
    }

    @Test
    public void shortestPathShouldFindShortestPathWhenGraphHasValidPathWithBidirectionalEdges() {
        // Create a sample graph with bidirectional edges
        Graph graph = new Graph();
        Node nodeA = new Node("A", 0, 0);
        Node nodeB = new Node("B", 1, 0);
        Node nodeC = new Node("C", 1, 1);
        Node nodeD = new Node("D", 0, 1);
        nodeA.addBidirectionalEdge(nodeB, 1.0);
        nodeA.addBidirectionalEdge(nodeC, 2.0);
        nodeB.addBidirectionalEdge(nodeC, 1.0);
        nodeB.addBidirectionalEdge(nodeD, 3.0);
        nodeC.addBidirectionalEdge(nodeD, 1.0);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Find the shortest path from nodeA to nodeD
        List<Node> shortestPath = dijkstraAlgorithm.getShortestPath(graph, nodeD, nodeA);
        for (Node node:shortestPath) {
            System.out.println(node.getId());
        }

        // The shortest path should be [nodeD, nodeC, nodeA]
        assertEquals(3, shortestPath.size());
        assertEquals(nodeD, shortestPath.get(0));
        assertEquals(nodeC, shortestPath.get(1));
        assertEquals(nodeA, shortestPath.get(2));
    }

    @Test
    public void shortestPathShouldFindShortestPathWhenGraphHasValidPathWithRealCoordinates() {
        // Create a sample graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", -17.393237, -66.157380);
        Node nodeB = new Node("B", -17.376119, -66.154946);
        Node nodeC = new Node("C", -17.385014, -66.148501);
        Node nodeD = new Node("D", -17.393460, -66.156804);
        nodeA.addUnidirectionalEdge(nodeB);
        nodeA.addUnidirectionalEdge(nodeC);
        nodeB.addUnidirectionalEdge(nodeC);
        nodeB.addUnidirectionalEdge(nodeD);
        nodeC.addUnidirectionalEdge(nodeD);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Find the shortest path from Plaza Principal to Plaza 14 de Septiembre
        List<Node> shortestPath = dijkstraAlgorithm.getShortestPath(graph, nodeA, nodeD);

        for (Node node : shortestPath) {
            System.out.println(node.getId());
        }

        // The shortest path should be [A, C, D]
        assertEquals(3, shortestPath.size());
        assertEquals(nodeA, shortestPath.get(0));
        assertEquals(nodeC, shortestPath.get(1));
        assertEquals(nodeD, shortestPath.get(2));
    }

    @Test
    public void getRoutesShouldReturnFastestRoute() {
        // Create a sample graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", 0, 0);
        Node nodeB = new Node("B", 1, 0);
        Node nodeC = new Node("C", 1, 1);
        Node nodeD = new Node("D", 0, 1);
        nodeA.addUnidirectionalEdge(nodeB, 1.0);
        nodeA.addUnidirectionalEdge(nodeC, 2.0);
        nodeB.addUnidirectionalEdge(nodeC, 1.0);
        nodeB.addUnidirectionalEdge(nodeD, 3.0);
        nodeC.addUnidirectionalEdge(nodeD, 1.0);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Calculate the routes
        Map<String, List<Node>> routes = dijkstraAlgorithm.getPathAlternatives(graph, nodeA, nodeD);

        // Check the fastest route
        List<Node> fastestRoute = routes.get("A");
        System.out.println(fastestRoute);

        // The fastest route should be [nodeA, nodeC, nodeD]
        assertEquals(3, fastestRoute.size());
        assertEquals(nodeA, fastestRoute.get(0));
        assertEquals(nodeC, fastestRoute.get(1));
        assertEquals(nodeD, fastestRoute.get(2));
    }
}
