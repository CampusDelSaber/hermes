package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.isc.hermes.model.Utils.MapPolyline;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;

import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

public class PolylineUpdater {

    private MapboxMap mapboxMap;
    private List<LatLng> polylinePoints;
    private Polyline polyline;

    public PolylineUpdater(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        polylinePoints = new ArrayList<>();
        polyline = null;
    }

    public void updatePoints(LatLng startPoint, LatLng currentPoint) {
        polylinePoints.add(currentPoint);
        UpdatePolylineTask updatePolylineTask = new UpdatePolylineTask();
        updatePolylineTask.execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        updatePoints(startPoint, currentPoint);

    }

    private class UpdatePolylineTask extends AsyncTask<Void, Void, Polyline> {

        @SuppressLint("WrongThread")
        @Override
        protected Polyline doInBackground(Void... voids) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(polylinePoints)
                    .color(Color.BLUE)
                    .width(5);

            if (polyline != null) {
                mapboxMap.removePolyline(polyline);
            }

            return mapboxMap.addPolyline(polylineOptions);
        }

        @Override
        protected void onPostExecute(Polyline updatedPolyline) {
            polyline = updatedPolyline;
        }
    }
}
