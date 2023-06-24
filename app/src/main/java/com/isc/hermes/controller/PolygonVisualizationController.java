package com.isc.hermes.controller;

import com.isc.hermes.utils.MapClickEventsManager;
import com.isc.hermes.view.MapPolygonStyle;
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
     * @param allPolygonsPoints Is a list of list of all polygons vertexes
     * @param hexCodeColor Is polygons' color converted as hexadecimal code
     */
    public void displayPointsPolygonOnMap(List<List<Point>> allPolygonsPoints,String hexCodeColor)  {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                new MapPolygonStyle(MapClickEventsManager.getInstance().getMapboxMap(), allPolygonsPoints,hexCodeColor);
            }
        });
    }

    /**
     * Instance of this singleton class
     * @return Unique instance
     */
    public static PolygonVisualizationController getInstance(){
        if (instance==null){
            instance = new PolygonVisualizationController();
        }
        return instance;
    }
}
