package com.isc.hermes.graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.model.graph.Edge;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.GraphManager;
import com.isc.hermes.model.graph.Node;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchIncidentsOnGraph {
    @Test
    public void searchIncidentsInsideGraphArea() {
        GraphManager graphManager = GraphManager.getInstance();
        try {
            assertTrue(graphManager.searchIncidentsOnTheGraph(
                    new LatLng(-17.330695, -66.196680),
                    new LatLng(-17.335127, -66.191737)).length() > 1);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void searchNodesNearbyIncidentTest1() {
        Graph graph = new Graph();
        Node street1 = new Node("Street1", -17.389952, -66.042269);
        Node street2 = new Node("Street2", -17.391262, -66.042731);
        Node street3 = new Node("Street3", -17.392687, -66.043222);
        Node street4 = new Node("Street4", -17.392521, -66.044531);
        Node street5 = new Node("Street5", -17.391274, -66.044114);
        Node street6 = new Node("Street6", -17.392479, -66.045409);
        Node street7 = new Node("Street7", -17.389075, -66.044147);

        graph.addNode(street1);
        graph.addNode(street2);
        graph.addNode(street3);
        graph.addNode(street4);
        graph.addNode(street5);
        graph.addNode(street6);
        graph.addNode(street7);

        graph.getNode("Street1").addBidirectionalEdge(graph.getNode("Street2"));
        graph.getNode("Street2").addBidirectionalEdge(graph.getNode("Street3"));
        graph.getNode("Street3").addBidirectionalEdge(graph.getNode("Street4"));
        graph.getNode("Street4").addBidirectionalEdge(graph.getNode("Street5"));
        graph.getNode("Street4").addBidirectionalEdge(graph.getNode("Street6"));
        graph.getNode("Street6").addBidirectionalEdge(graph.getNode("Street1"));

        GraphManager graphManager = GraphManager.getInstance();
        graphManager.setGraph(graph);

        List<String> streetsIds = new ArrayList<>();
        streetsIds.add(street1.getId());
        streetsIds.add(street3.getId());

        assertEquals(streetsIds, graphManager.searchNodesNearby(Point.fromLngLat(-66.042559, -17.390904)));
    }

    @Test
    public void searchNodesNearbyIncidentTest2() {
        Graph graph = new Graph();
        Node street1 = new Node("Street1", -17.371522, -66.178334);
        Node street2 = new Node("Street2", -17.371587, -66.177764);
        Node street3 = new Node("Street3", -17.371587, -66.177059);
        Node street4 = new Node("Street4", -17.371730, -66.175806);
        Node street5 = new Node("Street5", -17.371766, -66.174989);
        Node street6 = new Node("Street6", -17.373448, -66.175101);
        Node street7 = new Node("Street7", -17.373434, -66.175814);
        Node street8 = new Node("Street8", -17.373369, -66.176549);
        Node street9 = new Node("Street9", -17.373341, -66.177134);
        Node street10 = new Node("Street10", -17.373319, -66.177629);
        Node street11 = new Node("Street11", -17.373259, -66.178472);
        Node street12 = new Node("Street12", -17.374069, -66.177928);
        Node street13 = new Node("Street13", -17.374037, -66.177681);
        Node street14 = new Node("Street14", -17.374100, -66.177214);
        Node street15 = new Node("Street15", -17.374116, -66.176566);
        Node street16 = new Node("Street16", -17.374189, -66.175869);

        graph.addNode(street1);
        graph.addNode(street2);
        graph.addNode(street3);
        graph.addNode(street4);
        graph.addNode(street5);
        graph.addNode(street6);
        graph.addNode(street7);
        graph.addNode(street8);
        graph.addNode(street9);
        graph.addNode(street10);
        graph.addNode(street11);
        graph.addNode(street12);
        graph.addNode(street13);
        graph.addNode(street14);
        graph.addNode(street15);
        graph.addNode(street16);

        graph.getNode("Street1").addBidirectionalEdge(graph.getNode("Street2"));
        graph.getNode("Street2").addBidirectionalEdge(graph.getNode("Street3"));
        graph.getNode("Street3").addBidirectionalEdge(graph.getNode("Street4"));
        graph.getNode("Street4").addBidirectionalEdge(graph.getNode("Street5"));
        graph.getNode("Street5").addBidirectionalEdge(graph.getNode("Street6"));
        graph.getNode("Street6").addBidirectionalEdge(graph.getNode("Street7"));
        graph.getNode("Street7").addBidirectionalEdge(graph.getNode("Street8"));
        graph.getNode("Street8").addBidirectionalEdge(graph.getNode("Street9"));
        graph.getNode("Street9").addBidirectionalEdge(graph.getNode("Street10"));
        graph.getNode("Street10").addBidirectionalEdge(graph.getNode("Street11"));
        graph.getNode("Street13").addBidirectionalEdge(graph.getNode("Street12"));
        graph.getNode("Street13").addBidirectionalEdge(graph.getNode("Street14"));
        graph.getNode("Street10").addBidirectionalEdge(graph.getNode("Street13"));
        graph.getNode("Street14").addBidirectionalEdge(graph.getNode("Street15"));
        graph.getNode("Street15").addBidirectionalEdge(graph.getNode("Street16"));
        graph.getNode("Street1").addBidirectionalEdge(graph.getNode("Street11"));
        graph.getNode("Street2").addBidirectionalEdge(graph.getNode("Street10"));
        graph.getNode("Street3").addBidirectionalEdge(graph.getNode("Street9"));
        graph.getNode("Street4").addBidirectionalEdge(graph.getNode("Street7"));
        graph.getNode("Street9").addBidirectionalEdge(graph.getNode("Street14"));
        graph.getNode("Street8").addBidirectionalEdge(graph.getNode("Street15"));
        graph.getNode("Street7").addBidirectionalEdge(graph.getNode("Street16"));

        GraphManager graphManager = GraphManager.getInstance();
        graphManager.setGraph(graph);

        List<String> streetsIds = graphManager.searchNodesNearby(Point.fromLngLat(-66.177801, -17.374063));
        assertTrue(streetsIds.contains(street12.getId()));
        assertTrue(streetsIds.contains(street13.getId()));

        List<String> streetsIds2 = graphManager.searchNodesNearby(Point.fromLngLat(-66.175905, -17.372612));
        assertTrue(streetsIds2.contains(street4.getId()));
        assertTrue(streetsIds2.contains(street7.getId()));
    }
}
