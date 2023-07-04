package com.isc.hermes.controller;

import com.isc.hermes.utils.MapManager;
import com.isc.hermes.view.PolygonViewer;
import com.isc.hermes.view.PolygonsViewer;
import com.mapbox.geojson.Point;

import java.util.List;

import android.os.Handler;
import android.os.Looper;

/**
 * Class to manage the visualization of all polygons passed as parameter
 */
public class PolygonVisualizationController {

    private static PolygonVisualizationController instance;

    /**
     * Method to set the style for map about only display polygons
     *
     * @param polygon is a list of list of all polygons vertexes
     * @param polygonColor is polygons' color converted as hexadecimal code
     */
    public void displayPolygon(List<List<Point>> polygon, String polygonColor) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> new PolygonViewer(
                MapManager.getInstance().getMapboxMap(), polygon, polygonColor));
    }

    /**
     * This method display polygons on map.
     *
     * @param polygons polygons to render on map.
     * @param polygonColor is the color for each polygon.
     */
    public void displayPolygons(List<List<List<Point>>> polygons, String polygonColor) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> new PolygonsViewer(
                MapManager.getInstance().getMapboxMap(), polygons, polygonColor));
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
}
