package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;

import junit.framework.TestCase;

public class GraphUtilsTest extends TestCase {

    public void testTraverseGraph() {
        Graph graph = new Graph();
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
        System.out.println("start");
        GraphUtils.getInstance().traverseGraph(graph);
        System.out.println("end");
    }
}