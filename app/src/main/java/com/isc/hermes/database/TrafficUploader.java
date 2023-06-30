package com.isc.hermes.database;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.controller.GraphController;
import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Graph;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.model.navigation.TransportationType;
import com.isc.hermes.utils.DijkstraAlgorithm;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Map;

/**
 * This class inherits from the incidents uplader to overwrite the coordinate generation.
 */
public class TrafficUploader extends IncidentsUploader {

    private static TrafficUploader instance;
    private DijkstraAlgorithm dijkstraAlgorithm;
    private Graph graph;
    private GraphController graph2;
    String routeSelected = "Route A";
    /**
     * This method generates the current traffic coordinates.
     * <p>
     * Based on the current coordinates and the coordinates of the destination,
     * a list of coordinates representing the traffic is generated based on the
     * navigation algorithm and a graph containing the current route.
     *
     * @return The coordinates of the last clicked point in the format "[latitude, longitude]".
     */
    public String getCoordinates() {
        if (dijkstraAlgorithm == null) {
            dijkstraAlgorithm = new DijkstraAlgorithm();
        }
        if (graph == null) {
            graph = new Graph();
        }
        //TODO: This line must be received as a parameter to see which line to take from the network to be generated.
        String routeSelected = "Route A";

        Node destiny = new Node("Point 2", lastClickedPoint.getLatitude(), lastClickedPoint.getLongitude());
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null).getCurrentLocationModel();
        Node location = new Node("Point 1", currentLocation.getLatitude(), currentLocation.getLongitude());

        //TODO: This part must be replaced by the network that will be generated with real data when it is ready.
        graph.addNode(destiny);
        graph.addNode(location);

        Map<String, String> trafficLine =
                dijkstraAlgorithm.getGeoJsonRoutes(graph, destiny, location, TransportationType.CAR);
        String route = trafficLine.get(routeSelected);
        int indexCoordinates = route.indexOf("coordinates");
        String coordinates = route.substring(indexCoordinates);
        coordinates = coordinates.substring(13,coordinates.length()-2);
        return coordinates;
    }

    public String getCoordinates2() {
        if (dijkstraAlgorithm == null) {
            dijkstraAlgorithm = new DijkstraAlgorithm();
        }

        //TODO: This line must be received as a parameter to see which line to take from the network to be generated.


        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null).getCurrentLocationModel();
        LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng destiny = new LatLng(lastClickedPoint.getLatitude(), lastClickedPoint.getLongitude());

        graph2 = new GraphController(location,destiny);

        Map<String, String> trafficLine =
                dijkstraAlgorithm.getGeoJsonRoutes(
                        graph2.getGraph(), graph2.getStartNode(),
                        graph2.getDestinationNode(), TransportationType.CAR);

        String route = trafficLine.get(routeSelected);
        int indexCoordinates = route.indexOf("coordinates");
        String coordinates = route.substring(indexCoordinates);
        coordinates = coordinates.substring(13,coordinates.length()-2);
        return coordinates;
    }

    /**
     * This method retrieves the instance of the TrafficUploader class.
     *
     * @return The instance of the TrafficUploader class.
     */
    public static TrafficUploader getInstance() {
        if (instance == null) instance = new TrafficUploader();
        return instance;
    }
}


