package com.isc.hermes.utils;

import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.Route;

import java.util.List;

/**
 * Utility class for generating GeoJSON representations.
 */
public class GeoJsonUtils {
    private static GeoJsonUtils instance;

    /**
     * Generates a GeoJSON representation of a route.
     *
     * @param route the route for which to generate the GeoJSON
     * @return the GeoJSON string representation of the route
     */
    public String generateGeoJson(Route route) {
        List<Node> path = route.getPath();

        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"Feature\",\"distance\":")
                .append(route.getTotalEstimatedDistance())
                .append(",\"time\":")
                .append(route.getTotalEstimatedArrivalTime())
                .append(",\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[");

        for (int i = 0; i < path.size(); i++) {
            Node node = path.get(i);
            builder.append("[").append(node.getLongitude()).append(",").
                    append(node.getLatitude()).append("]");

            if (i < path.size() - 1) {
                builder.append(",");
            }
        }

        builder.append("]}}");
        return builder.toString();
    }

    /**
     * Retrieves the singleton instance of the GeoJsonUtils class.
     *
     * @return the GeoJsonUtils instance
     */
    public static GeoJsonUtils getInstance() {
        if (instance == null) instance = new GeoJsonUtils();
        return instance;
    }
}
