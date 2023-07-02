package com.isc.hermes.database;

import android.content.Context;

import com.isc.hermes.controller.CurrentLocationController;
import com.isc.hermes.controller.GraphController;
import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.controller.NavigationOptionsController;
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
        CurrentLocationModel currentLocation = CurrentLocationController.getControllerInstance(null).getCurrentLocationModel();
        String coordinates = "["+currentLocation.getLatitude()+","+currentLocation.getLongitude()+"]";
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


