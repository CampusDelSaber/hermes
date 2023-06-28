package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Node;

/**
 * This class is responsible for calculating the distance between two nodes using their coordinates.
 */
public class CoordinatesDistanceCalculator {

    private static CoordinatesDistanceCalculator instance;
    private final double EARTH_RADIUS = 6371.0;

    /**
     * This method calculates the distance between two nodes (using their coordinates).
     * It applies Haversine formula:
     * a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
     * c = 2 * atan2(sqrt(a), sqrt(1 - a))
     * distance = R * c
     *
     * lat1 and lat2 are the latitudes of the two points in radians.
     * lon1 and lon2 are the longitudes of the two points in radians.
     * Δlat is the difference between the latitudes: lat2 - lat1.
     * Δlon is the difference between the longitudes: lon2 - lon1.
     * R represents the radius of the sphere (e.g., the Earth's radius in km).
     *
     * @param node1 the first node
     * @param node2 the second node
     * @return the distance between the nodes in kilometers
     */
    public double calculateDistance(Node node1, Node node2) {
        double lat1 = Math.toRadians(node1.getLatitude());
        double lon1 = Math.toRadians(node1.getLongitude());
        double lat2 = Math.toRadians(node2.getLatitude());
        double lon2 = Math.toRadians(node2.getLongitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double angularDistance = getAngularDistance(lat1, lat2, dlon, dlat);
        double centralAngle = getCentralAngle(angularDistance);

        return EARTH_RADIUS * centralAngle;
    }

    /**
     * This method calculates the distance between two points overloading the
     * calculateDistance(Node, Node) method.
     *
     * @param source coords of the source point
     * @param target coords of the target point
     * @return the distance between the nodes in kilometers
     */
    public double calculateDistance(MapCoordsRecord source, MapCoordsRecord target) {
        double dlon = target.getLongitudeAsRadians() - source.getLongitudeAsRadians();
        double dlat = target.getLatitudeAsRadians() - source.getLatitudeAsRadians();

        double angularDistance = getAngularDistance(source.getLatitudeAsRadians(), target.getLatitudeAsRadians(), dlon, dlat);
        double centralAngle = getCentralAngle(angularDistance);

        return EARTH_RADIUS * centralAngle;
    }

    /**
     * Calculates the angular distance based on the given latitude and longitude differences.
     *
     * @param lat1  the latitude of the first point in radians
     * @param lat2  the latitude of the second point in radians
     * @param dlon  the difference in longitude between the points
     * @param dlat  the difference in latitude between the points
     * @return the angular distance
     */
    private double getAngularDistance(double lat1, double lat2, double dlon, double dlat){
        return Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);
    }

    /**
     * Calculates the central angle based on the given angular distance.
     *
     * @param angularDistance  the angular distance between the points
     * @return the central angle
     */
    private double getCentralAngle(double angularDistance){
        return 2 * Math.atan2(Math.sqrt(angularDistance), Math.sqrt(1 - angularDistance));
    }

    /**
     * Gets the singleton instance of the CoordinatesDistanceCalculator class.
     *
     * @return the instance of CoordinatesDistanceCalculator
     */
    public static CoordinatesDistanceCalculator getInstance() {
        if (instance == null) instance = new CoordinatesDistanceCalculator();
        return instance;
    }
}
