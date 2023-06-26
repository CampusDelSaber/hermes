package com.isc.hermes.trafficAutomatic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.controller.TrafficAutomaticFormController;
import com.isc.hermes.database.TrafficUploader;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.Route;
import com.isc.hermes.model.navigation.TransportationType;
import com.isc.hermes.utils.DijkstraAlgorithm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TrafficAutomaticTest {
    MapWayPointController mapWayPointController;
    Context context;
    DijkstraAlgorithm dijkstraAlgorithm;
    TrafficUploader trafficUploader ;
    TrafficAutomaticFormController trafficAutomaticFormController = new TrafficAutomaticFormController(context,mapWayPointController);

    @Before()
    public void setUp(){
        dijkstraAlgorithm = DijkstraAlgorithm.getInstance();
        trafficUploader = TrafficUploader.getInstance();
    }

    @Test
    public void testGetCoordinates(){
        Graph graph = new Graph();

        Node destiny = new Node("estebanArze_Sucre", -17.394251, -66.156338);

        Node sucre_25mayo = new Node("sucre_25mayo", -17.394064, -66.155208);
        Node sucre_sanMartin = new Node("sucre_sanMartin", -17.393858, -66.154149);
        Node jordan_sanMartin = new Node("jordan_sanMartin", -17.394903, -66.153965);
        Node sucre_Lanza = new Node("sucre_Lanza", -17.393682, -66.153060);

        Node location = new Node("lanza_Jordan", -17.394716, -66.152910);

        destiny.addUnidirectionalEdge(sucre_25mayo);
        sucre_25mayo.addUnidirectionalEdge(sucre_sanMartin);
        sucre_sanMartin.addUnidirectionalEdge(sucre_Lanza);
        sucre_Lanza.addUnidirectionalEdge(location);
        location.addUnidirectionalEdge(jordan_sanMartin);
        jordan_sanMartin.addUnidirectionalEdge(sucre_sanMartin);

        graph.addNode(destiny);
        graph.addNode(sucre_25mayo);
        graph.addNode(sucre_sanMartin);
        graph.addNode(jordan_sanMartin);
        graph.addNode(sucre_Lanza);
        graph.addNode(location);

        Map<String, Route> routesMap = dijkstraAlgorithm
                .getPathAlternatives(graph, destiny, jordan_sanMartin, TransportationType.CAR);

        List<Node> routeA = Objects.requireNonNull(routesMap.get("Route A")).getPath();

        assertNotNull(routeA);
        assertEquals(destiny, routeA.get(0));

        assertEquals(location, routeA.get(4));



    }
    @Test
    public void testEstimateTimeHigh(){
        int stimate = trafficAutomaticFormController.calculateEstimateTime(10,20);
        assertEquals(30,stimate);
    }
    @Test
    public void testEstimateTimeLow(){
        int stimate = trafficAutomaticFormController.calculateEstimateTime(10,5);
        assertEquals(5,stimate);
    }
    @Test
    public void testEstimateTimeSame(){
        int stimate = trafficAutomaticFormController.calculateEstimateTime(50,50);
        assertEquals(50,stimate);
    }

    @Test
    public void testTypeTrafficHigh(){
        String type = trafficAutomaticFormController.getTrafficType(10,20);
        assertEquals("High Traffic",type);
    }
    @Test
    public void testTypeTrafficLow(){
        String type = trafficAutomaticFormController.getTrafficType(20,5);
        assertEquals("Low Traffic",type);
    }
    @Test
    public void testTypeTrafficNormal(){
        String type = trafficAutomaticFormController.getTrafficType(10,10);
        assertEquals("Normal Traffic",type);
    }
}
