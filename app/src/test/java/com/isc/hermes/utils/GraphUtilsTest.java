package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;

import junit.framework.TestCase;

import org.junit.Test;

public class GraphUtilsTest extends  TestCase{
    @Test
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
        assertEquals("Street names in sucre_25mayo:sucre_25mayo -> sucre_sanMartinsucre_25mayo -> jordan_25mayo----*-----Street names in jordan_estebanArze:jordan_estebanArze -> estebanArze_Sucre----*-----Street names in jordan_25mayo:jordan_25mayo -> jordan_estebanArzejordan_25mayo -> calama_25mayo----*-----Street names in calama_estebanArze:calama_estebanArze -> jordan_estebanArzecalama_estebanArze -> calama_25mayo----*-----Street names in calama_25mayo:calama_25mayo -> calama_sanMartin----*-----Street names in estebanArze_Sucre:estebanArze_Sucre -> sucre_25mayo----*-----Street names in lanza_Jordan:lanza_Jordan -> jordan_sanMartin----*-----Street names in sucre_Lanza:sucre_Lanza -> lanza_Jordan----*-----Street names in calama_sanMartin:calama_sanMartin -> jordan_sanMartin----*-----Street names in sucre_sanMartin:sucre_sanMartin -> sucre_Lanza----*-----Street names in jordan_sanMartin:jordan_sanMartin -> jordan_25mayojordan_sanMartin -> sucre_sanMartin----*-----",
                GraphUtils.getInstance().traverseGraph(graph));
    }

    @Test
    public void testCreateBidirectionalGraph(){

        Graph graph = new Graph();
        Node a = new Node("a", -17.394251, -66.156338);
        Node b = new Node("b", -17.394064, -66.155208);
        Node c = new Node("c", -17.393858, -66.154149);
        Node d = new Node("d", -17.395030, -66.155045);
        Node e = new Node("e", -17.395276, -66.156104);
        Node f = new Node("f", -17.396376, -66.155970);
        Node g = new Node("g", -17.396151, -66.154875);
        Node h = new Node("h", -17.395951, -66.153754);
        Node i = new Node("i", -17.394903, -66.153965);
        Node j = new Node("j", -17.393682, -66.153060);
        Node k = new Node("k", -17.394716, -66.152910);


        a.addUnidirectionalEdge(b);
        b.addUnidirectionalEdge(c);
        c.addUnidirectionalEdge(j);
        j.addUnidirectionalEdge(k);
        k.addUnidirectionalEdge(i);
        b.addUnidirectionalEdge(d);
        d.addUnidirectionalEdge(e);
        d.addUnidirectionalEdge(g);
        e.addUnidirectionalEdge(a);
        i.addUnidirectionalEdge(d);
        f.addUnidirectionalEdge(e);
        f.addUnidirectionalEdge(g);
        g.addUnidirectionalEdge(h);
        h.addUnidirectionalEdge(i);
        i.addUnidirectionalEdge(c);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);
        graph.addNode(h);
        graph.addNode(i);
        graph.addNode(j);
        graph.addNode(k);


        Graph hello = GraphUtils.getInstance().createBidirectionalGraph(graph);
        assert hello.getNode("a").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("b"));
        assert hello.getNode("a").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("e"));
        assert hello.getNode("b").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("a"));
        assert hello.getNode("b").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("c"));
        assert hello.getNode("b").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("d"));
        assert hello.getNode("c").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("b"));
        assert hello.getNode("c").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("i"));
        assert hello.getNode("d").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("b"));
        assert hello.getNode("d").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("e"));
        assert hello.getNode("d").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("g"));
        assert hello.getNode("d").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("i"));
        assert hello.getNode("e").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("a"));
        assert hello.getNode("e").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("d"));
        assert hello.getNode("f").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("e"));
        assert hello.getNode("f").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("g"));
        assert hello.getNode("g").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("d"));
        assert hello.getNode("g").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("f"));
        assert hello.getNode("g").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("h"));
        assert hello.getNode("h").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("g"));
        assert hello.getNode("h").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("i"));
        assert hello.getNode("i").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("c"));
        assert hello.getNode("i").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("d"));
        assert hello.getNode("i").getEdges().stream().anyMatch(edge -> edge.getDestination().getId().equals("h"));
    }
}