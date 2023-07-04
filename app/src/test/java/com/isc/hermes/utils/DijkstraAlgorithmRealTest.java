package com.isc.hermes.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.controller.GraphController;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.Route;
import com.isc.hermes.model.navigation.TransportationType;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DijkstraAlgorithmRealTest {
    DijkstraAlgorithm dijkstraAlgorithm;
    @Before()
    public void setUp(){
        dijkstraAlgorithm = DijkstraAlgorithm.getInstance();
    }

    @Test
    public void getShortestPathWithRealStreetCoordinates(){
        Graph graph = new Graph();

        // estebanArze_Sucre source, jordan_sanMartin destination
        Node estebanArze_Sucre = new Node("estebanArze_Sucre", -17.394251, -66.156338);
        Node sucre_25mayo = new Node("sucre_25mayo", -17.394064, -66.155208);
        Node sucre_sanMartin = new Node("sucre_sanMartin", -17.393858, -66.154149);
        Node jordan_25mayo = new Node("jordan_25mayo", -17.395030, -66.155045);
        Node jordan_estebanArze = new Node("jordan_estebanArze", -17.395276, -66.156104);
        Node calama_estebanArze = new Node("calama_estebanArze", -17.396376, -66.155970);
        Node calama_25mayo = new Node("calama_25mayo", -17.396151, -66.154875);
        Node calama_sanMartin = new Node("calama_sanMartin", -17.395951, -66.153754);
        Node jordan_sanMartin = new Node("jordan_sanMartin", -17.394903, -66.153965);
        Node sucre_Lanza = new Node("sucre_Lanza", -17.393682, -66.153060);
        Node lanza_jordan = new Node("lanza_Jordan", -17.394716, -66.152910);


        estebanArze_Sucre.addUnidirectionalEdge(sucre_25mayo);
        sucre_25mayo.addUnidirectionalEdge(sucre_sanMartin);
        sucre_sanMartin.addUnidirectionalEdge(sucre_Lanza);
        sucre_Lanza.addUnidirectionalEdge(lanza_jordan);
        lanza_jordan.addUnidirectionalEdge(jordan_sanMartin);
        sucre_25mayo.addUnidirectionalEdge(jordan_25mayo);
        jordan_25mayo.addUnidirectionalEdge(jordan_estebanArze);
        jordan_25mayo.addUnidirectionalEdge(calama_25mayo);
        jordan_estebanArze.addUnidirectionalEdge(estebanArze_Sucre);
        jordan_sanMartin.addUnidirectionalEdge(jordan_25mayo);
        calama_estebanArze.addUnidirectionalEdge(jordan_estebanArze);
        calama_estebanArze.addUnidirectionalEdge(calama_25mayo);
        calama_25mayo.addUnidirectionalEdge(calama_sanMartin);
        calama_sanMartin.addUnidirectionalEdge(jordan_sanMartin);
        jordan_sanMartin.addUnidirectionalEdge(sucre_sanMartin);

        graph.addNode(estebanArze_Sucre);
        graph.addNode(sucre_25mayo);
        graph.addNode(sucre_sanMartin);
        graph.addNode(jordan_25mayo);
        graph.addNode(jordan_estebanArze);
        graph.addNode(calama_estebanArze);
        graph.addNode(calama_25mayo);
        graph.addNode(calama_sanMartin);
        graph.addNode(jordan_sanMartin);
        graph.addNode(sucre_Lanza);
        graph.addNode(lanza_jordan);

        Map<String, Route> routesMap = dijkstraAlgorithm
                .getPathAlternatives(graph, estebanArze_Sucre, jordan_sanMartin, TransportationType.CAR);

        List<Node> routeA = Objects.requireNonNull(routesMap.get("Route A")).getPath();
        assertNotNull(routeA);
        assertEquals(estebanArze_Sucre, routeA.get(0));
        assertEquals(sucre_25mayo, routeA.get(1));
        assertEquals(sucre_sanMartin, routeA.get(2));
        assertEquals(sucre_Lanza, routeA.get(3));
        assertEquals(lanza_jordan, routeA.get(4));
        assertEquals(jordan_sanMartin, routeA.get(5));

        List<Node> routeB = Objects.requireNonNull(routesMap.get("Route B")).getPath();
        assertNotNull(routeB);
        assertEquals(estebanArze_Sucre, routeB.get(0));
        assertEquals(sucre_25mayo, routeB.get(1));
        assertEquals(jordan_25mayo, routeB.get(2));
        assertEquals(calama_25mayo, routeB.get(3));
        assertEquals(calama_sanMartin, routeB.get(4));
        assertEquals(jordan_sanMartin, routeB.get(5));

        System.out.println(
                dijkstraAlgorithm.getGeoJsonRoutes(
                        graph, estebanArze_Sucre, jordan_sanMartin, TransportationType.CAR
                )
        );
    }

    @Test
    public void getRoutesRealGraph() throws JSONException {
        LatLng start = new LatLng(-17.376973, -66.179360);
        LatLng destination = new LatLng(-17.377594, -66.181011);
        GraphController graphController = new GraphController(start, destination);
        graphController.buildGraph(TransportationType.CAR);
        Graph graph = graphController.getGraph();

        graph.printGraph();
        System.out.println(
                dijkstraAlgorithm.getGeoJsonRoutes(graph, graphController.getStartNode(), graphController.getDestinationNode(), TransportationType.CAR));
    }
}
