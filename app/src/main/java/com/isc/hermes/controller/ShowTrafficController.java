package com.isc.hermes.controller;

import android.graphics.Color;

import com.isc.hermes.ActivitySelectRegion;
import com.isc.hermes.database.IncidentsDataProcessor;

import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class will be in charge of displaying and occluding the Traffic that you locate in the database.
 *
 */
public class ShowTrafficController {
    private IncidentsDataProcessor incidentsDataProcessor = IncidentsDataProcessor.getInstance();
    private List<Polyline> trafficPolylines = new ArrayList<>();
    private static ShowTrafficController instance;

    /**
     * This method will hide the incidents if the user does not want to see them.
     * @param mapDisplay It is the map which will be shown .
     */
    public void hideTraffic(ActivitySelectRegion mapDisplay) {
        for (Polyline polyline : trafficPolylines) {
            mapDisplay.getMapboxMap().removePolyline(polyline);
        }
        trafficPolylines.clear();
    }

    /**
     * This method will show the incidents if the user want to see them.
     * @param mapDisplay It is the map which will be shown .
     */
    public void showTraffic(MapboxMap mapDisplay) throws JSONException {
        JSONArray incidentsArrayNormal = incidentsDataProcessor.getAllIncidentsByType("Normal Traffic");
        JSONArray incidentsArrayLow = incidentsDataProcessor.getAllIncidentsByType("Low Traffic");
        JSONArray incidentsArrayHigh = incidentsDataProcessor.getAllIncidentsByType("High Traffic");
        createNormalTraffic(incidentsArrayNormal, mapDisplay, "Normal Traffic");
        createNormalTraffic(incidentsArrayLow, mapDisplay, "Low Traffic");
        createNormalTraffic(incidentsArrayHigh, mapDisplay, "High Traffic");
    }

    private void createNormalTraffic(JSONArray incidentsArray, MapboxMap mapDisplay, String typeTraffic) throws JSONException {
        for (int i = 0; i < incidentsArray.length(); i++) {
            JSONObject incident = incidentsArray.getJSONObject(i);
            Polyline polyline = createTrafficPolyline(mapDisplay, incident, typeTraffic);
            trafficPolylines.add(polyline);
        }
    }

    /**
     * This method will be in charge of once filtered the data of the points stored in the database,
     * it obtains them and draws the lines.
     * @param mapDisplay It is the map which will be shown .
     * @param incidentObject is the JSON file to extract the data .
     */
    private Polyline createTrafficPolyline(MapboxMap mapDisplay, JSONObject incidentObject, String trafficType) throws JSONException {
        JSONObject geometryObject = incidentObject.getJSONObject("geometry");
        JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(2f);
        polylineStyle(trafficType,polylineOptions);

        for (int j = 0; j < coordinatesArray.length(); j++) {
            JSONArray coordinate = coordinatesArray.getJSONArray(j);
            double latitude = coordinate.getDouble(0);
            double longitude = coordinate.getDouble(1);
            polylineOptions.add(new LatLng(latitude, longitude));
        }

        Polyline polyline = mapDisplay.addPolyline(polylineOptions);
        return polyline;
    }

    /**
     * This method will be in charge the style of the different types of traffic whit different Colors
     * @param trafficType It is the traffic Type .
     * @param polylineOptions is the polyline Options.
     */
    private void polylineStyle(String trafficType, PolylineOptions polylineOptions) {
        HashMap<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Normal Traffic", Color.parseColor("#FFA500"));
        colorMap.put("Low Traffic", Color.GREEN);
        colorMap.put("High Traffic", Color.RED);

        Integer color = colorMap.get(trafficType);
        if (color != null) {
            polylineOptions.color(color);
        }
    }

    /**
     * Retrieves the singleton instance of ShowTrafficController.
     *
     * @return The singleton instance.
     */
    public static ShowTrafficController getInstance(){
        if (instance == null) instance = new ShowTrafficController();
        return instance;
    }

}
