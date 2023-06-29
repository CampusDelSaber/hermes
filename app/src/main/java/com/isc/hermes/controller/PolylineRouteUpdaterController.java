package com.isc.hermes.controller;

import android.os.Handler;
import android.os.Looper;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.Utils.MapPolyline;
import com.isc.hermes.model.location.LocationIntervals;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for updating the breadcrumb polyline taking into account the user's path.
 *
 * @see NavigationOptionsController
 * @see MapPolyline
 */
public class PolylineRouteUpdaterController {
    private final MapPolyline mapPolyline;
    private final Handler handler;
    private LatLng startPoint;
    private LatLng finalPoint;
    private LatLng destination;

    /**
     * Constructs a PolylineDrawer object.
     *
     * @param startPoint  The starting point of the polyline. If null, the current location is used.
     * @param destination The destination point of the polyline.
     * @param mapPolyline The MapPolyline object for drawing the polyline on the map.
     */
    public PolylineRouteUpdaterController(LatLng startPoint, LatLng destination, MapPolyline mapPolyline) {
        handler = new Handler(Looper.getMainLooper());
        this.startPoint = startPoint == null ? CurrentLocationModel.getInstance().getLatLng() : startPoint;
        this.finalPoint = CurrentLocationModel.getInstance().getLatLng();
        this.destination = destination;
        this.mapPolyline = mapPolyline;
    }

    /**
     * Draws the polyline on the map every second.
     */
    public void drawPolylineEverySecond() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    mapPolyline.displayPolyline(startPoint, finalPoint);
                    startPoint = finalPoint;
                    finalPoint = CurrentLocationModel.getInstance().getLatLng();

                    if (startPoint.equals(destination)) timer.cancel();
                });
            }
        };

        timer.schedule(task, 0, (long) LocationIntervals.UPDATE_INTERVAL_MS.getValue());
    }

    /**
     * Sets the starting point of the polyline.
     *
     * @param startPoint The starting point to set.
     */
    public synchronized void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }
}
