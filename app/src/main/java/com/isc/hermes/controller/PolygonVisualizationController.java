package com.isc.hermes.controller;

import com.isc.hermes.utils.MapClickEventsManager;
import com.isc.hermes.view.MapPolygonStyle;
import com.mapbox.geojson.Point;

import java.util.List;

import android.os.Handler;
import android.os.Looper;

public class PolygonVisualizationController {
    private static PolygonVisualizationController instance;

    public void displayPointsPolygonOnMap(List<List<Point>> allPolygonsPoints,String hexCodeColor)  {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                new MapPolygonStyle(MapClickEventsManager.getInstance().getMapboxMap(), allPolygonsPoints,hexCodeColor);
            }
        });
    }

    public static PolygonVisualizationController getInstance(){
        if (instance==null){
            instance = new PolygonVisualizationController();
        }
        return instance;
    }
}
