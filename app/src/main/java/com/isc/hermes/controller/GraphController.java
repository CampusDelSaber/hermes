package com.isc.hermes.controller;

import android.annotation.SuppressLint;

import com.isc.hermes.model.graph.Edge;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GraphController {
    private Graph graph;
    private LatLng start;
    private LatLng destination;
    private LatLng midpoint;
    private Node startNode;
    private Node destinationNode;
    private CoordinatesDistanceCalculator calculator;

    public GraphController(LatLng start, LatLng destination) {
        this.graph = new Graph();
        this.start = start;
        this.destination = destination;
        this.midpoint = calculateMidpoint();
        this.calculator = new CoordinatesDistanceCalculator();
        this.startNode = new Node("start", start.getLatitude(), start.getLongitude());
        this.destinationNode = new Node("destination", destination.getLatitude(), destination.getLongitude());
    }

    private LatLng calculateMidpoint() {
        double lat1Rad = Math.toRadians(start.getLatitude());
        double lon1Rad = Math.toRadians(start.getLongitude());

        double lat2Rad = Math.toRadians(destination.getLatitude());
        double lon2Rad = Math.toRadians(destination.getLongitude());

        // Calcular coordenadas cartesianas en el espacio tridimensional
        double x1 = Math.cos(lat1Rad) * Math.cos(lon1Rad);
        double y1 = Math.cos(lat1Rad) * Math.sin(lon1Rad);
        double z1 = Math.sin(lat1Rad);

        double x2 = Math.cos(lat2Rad) * Math.cos(lon2Rad);
        double y2 = Math.cos(lat2Rad) * Math.sin(lon2Rad);
        double z2 = Math.sin(lat2Rad);

        // Calcular el promedio de las coordenadas cartesianas
        double xAvg = (x1 + x2) / 2;
        double yAvg = (y1 + y2) / 2;
        double zAvg = (z1 + z2) / 2;

        // Convertir las coordenadas cartesianas promedio a latitud y longitud
        double latAvgRad = Math.atan2(zAvg, Math.sqrt(xAvg * xAvg + yAvg * yAvg));
        double lonAvgRad = Math.atan2(yAvg, xAvg);

        // Convertir latitud y longitud promedio de radianes a grados
        double latAvg = Math.toDegrees(latAvgRad);
        double lonAvg = Math.toDegrees(lonAvgRad);


        return new LatLng(latAvg, lonAvg);
    }

    private double getRadius() {
        Node midpointNode = new Node("midpoint", midpoint.getLatitude(), midpoint.getLongitude());
        double radius = calculator.calculateDistance(startNode, midpointNode);
        System.out.println(radius);
        return radius;
    }

    public String getIntersections() {
        double latitude = midpoint.getLatitude();
        double longitude = midpoint.getLongitude();
        int radio = (int) (getRadius() * 1000);

        try {
            @SuppressLint("DefaultLocale") String request = String.format(
                    "[out:json];way(around:%d,%f,%f)[highway~\"^(primary|secondary|tertiary)$\"];node(w);out center;",
                    radio, latitude, longitude
            );

            String codedRequest = URLEncoder.encode(request, "UTF-8");

            URL url = new URL("https://overpass-api.de/api/interpreter?data=" + codedRequest);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Graph buildGraphFromIntersections(String intersectionsJson) throws JSONException {
        if (intersectionsJson != null) {
            JSONObject json = new JSONObject(intersectionsJson);
            JSONArray intersections = json.getJSONArray("elements");

            for (int i = 0; i < intersections.length(); i++) {
                Node node = new Node(String.valueOf(intersections.getJSONObject(i).get("id")),
                        (Double) intersections.getJSONObject(i).get("lat"),
                        (Double) intersections.getJSONObject(i).get("lon"));
                graph.addNode(node);
            }

            graph.addNode(startNode);
            graph.addNode(destinationNode);
        }

        buildEdges(getEdges());

        System.out.println(graph.getNodes().size());

        return graph;
    }

    private String getEdges() {
        double latitude = midpoint.getLatitude();
        double longitude = midpoint.getLongitude();
        int radio = (int) (getRadius() * 1000);

        try {
            @SuppressLint("DefaultLocale") String consultaCaminos = String.format(
                    "[out:json];way(around:%d,%f,%f)[highway~\"^(primary|secondary|tertiary)$\"][\"junction\"!=\"roundabout\"];" +
                            "node(w)->.n1;node(w)->.n2;way(bn.n1)(bn.n2);out meta;",
                    radio, latitude, longitude
            );

            // Codifica la consulta de caminos
            String consultaCaminosCodificada = URLEncoder.encode(consultaCaminos, "UTF-8");

            // Construye la URL de la solicitud para los caminos
            String urlCaminosStr = "https://overpass-api.de/api/interpreter?data=" + consultaCaminosCodificada;
            System.out.println(urlCaminosStr);
            URL urlCaminos = new URL(urlCaminosStr);
            HttpURLConnection connCaminos = (HttpURLConnection) urlCaminos.openConnection();
            connCaminos.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connCaminos.getInputStream()));
            StringBuilder response = new StringBuilder();
            String lineCaminos;
            while ((lineCaminos = reader.readLine()) != null) {
                response.append(lineCaminos);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buildEdges(String edgesJson) throws JSONException {
        if (edgesJson != null) {
            JSONObject json = new JSONObject(edgesJson);
            JSONArray edges = json.getJSONArray("elements");
            Node lastNode;
            Node currentNode;

            for (int i = 0; i < edges.length(); i++) {
                for (int j = 1 ; j< edges.getJSONObject(i).getJSONArray("nodes").length() ; j++) {
                    lastNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j-1)));
                    currentNode = graph.getNode(String.valueOf(edges.getJSONObject(i).getJSONArray("nodes").get(j)));
                    if(currentNode != null && lastNode != null) {
                        lastNode.addBidirectionalEdge(currentNode);
                    }
                }
            }
        }
    }

    public LatLng getStart() {
        return start;
    }

    public LatLng getDestination() {
        return destination;
    }
}