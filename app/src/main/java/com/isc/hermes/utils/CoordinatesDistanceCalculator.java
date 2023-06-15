package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Node;

public class CoordinatesDistanceCalculator {

    private static CoordinatesDistanceCalculator instance;

    public double calculateDistance(Node node1, Node node2) {
        double lat1 = Math.toRadians(node1.getLatitude());
        double lon1 = Math.toRadians(node1.getLongitude());
        double lat2 = Math.toRadians(node2.getLatitude());
        double lon2 = Math.toRadians(node2.getLongitude());

        final double R = 6371.0; // Earth's radius in km

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static CoordinatesDistanceCalculator getInstance() {
        if (instance == null) instance = new CoordinatesDistanceCalculator();
        return instance;
    }
}
