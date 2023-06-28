package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.isc.hermes.MainActivity;
import com.isc.hermes.model.CurrentLocationModel;
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

import androidx.appcompat.app.AppCompatActivity;

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
    private LatLng startPoint;
    private Context context;

    public PolylineUpdater(MapboxMap mapboxMap, LatLng startPoint, Context context) {
        this.mapboxMap = mapboxMap;
        polylinePoints = new ArrayList<>();
        polyline = null;
        this.startPoint = startPoint;
        this.context = context;
    }

    public void updatePoints(LatLng startPoint, LatLng currentPoint) {
        startPoint = startPoint==null ? this.startPoint : startPoint;
        polylinePoints.add(startPoint);
        polylinePoints.add(currentPoint);
        UpdatePolylineTask updatePolylineTask = new UpdatePolylineTask();
        updatePolylineTask.execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startPoint = currentPoint;
        currentPoint = CurrentLocationModel.getInstance().getLatLng();
        updatePoints(startPoint, currentPoint);

    }

    private class UpdatePolylineTask extends AsyncTask<Void, Void, Polyline> {

        @SuppressLint("WrongThread")
        @Override
        protected Polyline doInBackground(Void... voids) {
            // No realices operaciones en el mapa aquí
            return null;
        }

        @Override
        protected void onPostExecute(Polyline updatedPolyline) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(polylinePoints)
                    .color(Color.BLUE)
                    .width(5);

            if (polyline != null) {
                mapboxMap.removePolyline(polyline);
            }
            System.out.println("polyline updated");

            polyline = mapboxMap.addPolyline(polylineOptions);
        }
    }

}
