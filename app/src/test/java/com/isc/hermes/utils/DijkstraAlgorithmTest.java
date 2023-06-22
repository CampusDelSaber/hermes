package com.isc.hermes.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.Route;
import com.isc.hermes.model.navigation.TransportationType;

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
        List<Node> shortestPath =
                dijkstraAlgorithm.getPathAlternatives(graph, nodeA, nodeD, TransportationType.CAR).get("Route A")
                        .getPath();

        System.out.println(dijkstraAlgorithm.getGeoJsonRoutes(graph, nodeA, nodeD, TransportationType.CAR));

        assertNotNull(shortestPath);
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
        nodeB.addBidirectionalEdge(nodeC, 2.0);
        nodeB.addBidirectionalEdge(nodeD, 3.0);
        nodeC.addBidirectionalEdge(nodeD, 1.0);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Find the shortest path from nodeA to nodeD
        List<Node> shortestPath =
                dijkstraAlgorithm.getPathAlternatives(graph, nodeD, nodeA, TransportationType.CAR).get("Route A")
                        .getPath();

        assertNotNull(shortestPath);
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
        List<Node> shortestPath =
                dijkstraAlgorithm.getPathAlternatives(graph, nodeA, nodeD, TransportationType.CAR).get("Route A")
                        .getPath();

        System.out.println(shortestPath);
        assertNotNull(shortestPath);
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
        Map<String, Route> routes =
                dijkstraAlgorithm.getPathAlternatives(graph, nodeA, nodeD, TransportationType.CAR);

        System.out.println(routes);

        // Check the fastest route
        List<Node> fastestRoute = routes.get("Route A").getPath();

        // The fastest route should be [nodeA, nodeC, nodeD]
        assertNotNull(fastestRoute);
        assertEquals(3, fastestRoute.size());
        assertEquals(nodeA, fastestRoute.get(0));
        assertEquals(nodeC, fastestRoute.get(1));
        assertEquals(nodeD, fastestRoute.get(2));
    }

    @Test
    public void testGetShortestPathAlternatives() {
        Graph graph = createGraph();
        Node source = graph.getNode("A");
        Node destination = graph.getNode("F");

        Map<String, Route> shortestPathAlternatives =
            dijkstraAlgorithm.getPathAlternatives(graph, source, destination, TransportationType.CAR);
        assertNotNull(shortestPathAlternatives);
        assertEquals(3, shortestPathAlternatives.size());
        System.out.println(shortestPathAlternatives);

        // Assert the shortest alternative paths
        List<Node> alternative1 = shortestPathAlternatives.get("Route A").getPath();
        assertNotNull(alternative1);
        assertEquals(4, alternative1.size());
        assertEquals(source, alternative1.get(0));
        assertEquals(graph.getNode("B"), alternative1.get(1));
        assertEquals(graph.getNode("D"), alternative1.get(2));
        assertEquals(destination, alternative1.get(3));

        List<Node> alternative2 = shortestPathAlternatives.get("Route B").getPath();
        assertNotNull(alternative2);
        assertEquals(4, alternative2.size());
        assertEquals(source, alternative2.get(0));
        assertEquals(graph.getNode("C"), alternative2.get(1));
        assertEquals(graph.getNode("D"), alternative2.get(2));
        assertEquals(destination, alternative2.get(3));

        List<Node> alternative3 = shortestPathAlternatives.get("Route C").getPath();
        assertNotNull(alternative3);
        assertEquals(5, alternative3.size());
        assertEquals(source, alternative3.get(0));
        assertEquals(graph.getNode("B"), alternative3.get(1));
        assertEquals(graph.getNode("D"), alternative3.get(2));
        assertEquals(graph.getNode("E"), alternative3.get(3));
        assertEquals(destination, alternative3.get(4));
    }

    private Graph createGraph() {
        Graph graph = new Graph();

        Node nodeA = new Node("A", 0.0, 0.0);
        Node nodeB = new Node("B", 1.0, 1.0);
        Node nodeC = new Node("C", 2.0, 2.0);
        Node nodeD = new Node("D", 3.0, 3.0);
        Node nodeE = new Node("E", 4.0, 4.0);
        Node nodeF = new Node("F", 5.0, 5.0);

        nodeA.addUnidirectionalEdge(nodeB, 1.0);
        nodeA.addUnidirectionalEdge(nodeC, 2.0);
        nodeB.addUnidirectionalEdge(nodeD, 2.0);
        nodeC.addUnidirectionalEdge(nodeD, 1.0);
        nodeC.addUnidirectionalEdge(nodeE, 3.0);
        nodeD.addUnidirectionalEdge(nodeE, 2.0);
        nodeD.addUnidirectionalEdge(nodeF, 1.0);
        nodeE.addUnidirectionalEdge(nodeF, 2.0);

        // POSSIBLE ROUTES
        // A - B - D - F
        // A - C - D - F
        // A - B - D - E - F
        // A - C - D - E - F

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        return graph;
    }
}
