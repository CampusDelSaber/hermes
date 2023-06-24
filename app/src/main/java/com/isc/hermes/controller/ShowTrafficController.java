package com.isc.hermes.controller;

import static com.mongodb.assertions.Assertions.assertNotNull;

import android.graphics.Color;

import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.view.MapDisplay;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShowTrafficController {
    private IncidentsDataProcessor incidentsDataProcessor = IncidentsDataProcessor.getInstance();
    private List<Polyline> trafficPolylines = new ArrayList<>();

    public void hideTraffic(MapDisplay mapDisplay) {
        for (Polyline polyline : trafficPolylines) {
            mapDisplay.getMapboxMap().removePolyline(polyline);
        }
        trafficPolylines.clear();
    }


    public void showTraffic(MapDisplay mapDisplay) throws JSONException {
        JSONArray incidentsArray = incidentsDataProcessor.getAllIncidentsByType("Traffic");
        assertNotNull(incidentsArray);

        for (int i = 0; i < incidentsArray.length(); i++) {
            Polyline polyline = createTrafficPolyline(mapDisplay, incidentsArray.getJSONObject(i));
            trafficPolylines.add(polyline);
        }
    }
    private Polyline createTrafficPolyline(MapDisplay mapDisplay, JSONObject incidentObject) throws JSONException {
        JSONObject geometryObject = incidentObject.getJSONObject("geometry");
        JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);

        for (int j = 0; j < coordinatesArray.length(); j++) {
            JSONArray coordinate = coordinatesArray.getJSONArray(j);
            double latitude = coordinate.getDouble(0);
            double longitude = coordinate.getDouble(1);
            polylineOptions.add(new LatLng(latitude, longitude));
        }

        Polyline polyline = mapDisplay.getMapboxMap().addPolyline(polylineOptions);
        return polyline;
    }

}
