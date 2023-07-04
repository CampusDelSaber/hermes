package com.isc.hermes.controller;

import com.isc.hermes.requests.incidents.PolygonRequester;
import com.isc.hermes.utils.MapManager;
import com.isc.hermes.view.PolygonViewer;
import com.mapbox.geojson.Point;

import java.util.List;

import android.os.Handler;
import android.os.Looper;

/**
 * Class to manage the visualization of all polygons passed as parameter
 */
public class PolygonVisualizationController {
    private PolygonRequester polygonRequester;
    private static PolygonVisualizationController instance;

    /**
     * Method to set the style for map about only display polygons
     *
     * @param polygon is a list of list of all polygons vertexes
     */
    public void displayPolygons(List<List<Point>> polygon, boolean withPolygonsDb) {
        Handler handler = new Handler(Looper.getMainLooper());
        List<List<List<Point>>> polygons = withPolygonsDb ?
                polygonRequester.getPolygons() : null;
        handler.post(() -> new PolygonViewer(
                MapManager.getInstance().getMapboxMap(), polygons, polygon));
    }

    /**
     * This method display a polygons using a style map loader.
     *
     * @param polygons to render on the map.
     */
    public void displayPolygons(List<List<List<Point>>> polygons) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> new PolygonViewer(
                MapManager.getInstance().getMapboxMap(), polygons, null));
    }

    /**
     * Instance of this singleton class
     *
     * @return Unique instance
     */
    public static PolygonVisualizationController getInstance() {
        if (instance == null) {
            instance = new PolygonVisualizationController();
        }
        return instance;
    }

    /**
     * This is the constructor method to initialize the polygon requester.
     */
    private PolygonVisualizationController() {
        this.polygonRequester = new PolygonRequester();
    }
}
