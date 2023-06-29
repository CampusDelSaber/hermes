package com.isc.hermes.model.navigation;

import android.annotation.SuppressLint;

import com.isc.hermes.model.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRouteBuilder {
    private ArrayList<String> geoJson;
    private Route routeA;

    public void cookRoute() {
        Map<String, String> route = new HashMap<>();

        route.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"time\":10,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        route.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"time\":12,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        route.put("Route C", "{\"type\":\"Feature\",\"distance\":1.6061126697414532,\"time\":15,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.159019, -17.398311],[-66.154399, -17.397043],[-66.151315, -17.398656],[-66.147585, -17.400585],[-66.142978, -17.401595]]}}");
        String jsonA = route.get("Route A");
        String jsonB = route.get("Route B");
        String jsonC = route.get("Route C");

        geoJson = new ArrayList<>();
        geoJson.add(jsonA);
        geoJson.add(jsonB);
        geoJson.add(jsonC);

        routeA = createRoute(cookRoutePoints());
    }

    private ArrayList<Double[]> cookRoutePoints() {
        ArrayList<Double[]> handMadeRoute = new ArrayList<>();
        handMadeRoute.add(new Double[]{-66.156338, -17.394251});
        handMadeRoute.add(new Double[]{-66.155208, -17.394064});
        handMadeRoute.add(new Double[]{-66.154149, -17.393858});
        handMadeRoute.add(new Double[]{-66.15306, -17.393682});
        handMadeRoute.add(new Double[]{-66.15291, -17.394716});
        handMadeRoute.add(new Double[]{-66.153965, -17.394903});

        return handMadeRoute;
    }

    @SuppressLint("DefaultLocale")
    private Route createRoute(ArrayList<Double[]> points) {
        List<Node> nodes = new ArrayList<>();
        for (int index = 0; index < points.size(); index++) {
            Double[] coords = points.get(index);
            nodes.add(new Node(String.format("%d", index), coords[0], coords[1]));
        }

        return new Route(nodes, TransportationType.CAR);
    }

    public ArrayList<String> exportToGeoJSON() {
        return geoJson;
    }

    public Route getRouteA() {
        return routeA;
    }
}
