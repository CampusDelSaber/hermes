package com.isc.hermes.controller;

import android.os.Handler;
import android.os.Looper;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.model.location.LocationIntervals;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// ...

public class PolylineDrawer {
    private final MapPolyline mapPolyline;
    private final Handler handler;
    private LatLng startPoint, finalPoint, destination;
    private InfoRouteController infoRouteController;

    public PolylineDrawer(LatLng startPoint, LatLng destination, InfoRouteController infoRouteController,
                          MapPolyline mapPolyline) {
        handler = new Handler(Looper.getMainLooper());
        this.startPoint = startPoint==null ? CurrentLocationModel.getInstance().getLatLng() : startPoint;
        this.finalPoint = CurrentLocationModel.getInstance().getLatLng();
        this.destination = destination;
        this.infoRouteController = infoRouteController;
        this.mapPolyline = mapPolyline;
    }


    public void drawPolylineEverySecond() {
//        final String[] geoJson = new String[1];
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(startPoint.equals(destination)) timer.cancel();
//                        geoJson[0] = createGeoJson(startPoint, finalPoint);
//                        infoRouteController.showInfoRoute(Arrays.asList(geoJson[0]), mapPolyline);
                        mapPolyline.displayPolyline(startPoint, finalPoint);
                        startPoint = finalPoint;
                        finalPoint = CurrentLocationModel.getInstance().getLatLng();
                        System.out.println("polyline drawed");
                    }
                });
            }

        };

        timer.schedule(task, 0, (long)LocationIntervals.UPDATE_INTERVAL_MS.getValue());
    }

    public synchronized void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }
}
