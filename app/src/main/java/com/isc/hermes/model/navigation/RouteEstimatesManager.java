package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.isc.hermes.utils.MapCoordsRecord;

public class RouteEstimatesManager {
    private Route route;
    private final CoordinatesDistanceCalculator distanceCalculator;

    public RouteEstimatesManager(Route route) {
        this.route = route;
        distanceCalculator = CoordinatesDistanceCalculator.getInstance();
    }

    public void updateEstimates(CurrentLocationModel userLocation){
        Node currentNode = route.getCurrentNode();
        route.updateDistance(measurePartialDistance(currentNode, userLocation));
        route.updateEstimatedArrivalTime();
    }

    private double measurePartialDistance(Node node, CurrentLocationModel userLocation){
       return distanceCalculator.calculateDistance(
               new MapCoordsRecord(node.getLatitude(), node.getLongitude()),
               new MapCoordsRecord(userLocation.getLatitude(), userLocation.getLongitude())
       );
    }
}
