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
    public void searchNodesNearbyIncident() {
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

        System.out.println(graphManager.searchNodesNearby(Point.fromLngLat(-17.390904, -66.042559)));
    }
}
