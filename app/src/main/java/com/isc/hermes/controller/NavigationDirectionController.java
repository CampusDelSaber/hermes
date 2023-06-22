package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationDirectionController {
    static boolean isActive;
    private final Context context;
    private final RelativeLayout directionsForm;
    private final MapWayPointController mapWayPointController;
    private TextView directionsList;


    public NavigationDirectionController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        this.mapWayPointController = mapWayPointController;
        directionsForm = ((AppCompatActivity) context).findViewById(R.id.directions_form);

        directionsList = ((AppCompatActivity)context).findViewById(R.id.directions_list);

        Map<String, String> r = new HashMap<>();

        r.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        r.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        r.put("Route C", "{}");

        String jsonA = r.get("Route A");
        String jsonB = r.get("Route B");
        String jsonC = r.get("Route C");

        processRoute(jsonA);

    }

    private void setButtons() {

    }

    void updateUiPointsComponents() {
        if (isActive) {
            directionsForm.startAnimation(Animations.entryAnimation);
            directionsForm.setVisibility(View.VISIBLE);
        }
    }

    public RelativeLayout getDirectionsForm() {
        return directionsForm;
    }

    private void parseRoute(String json) {
        try {
            JSONObject jsonRoute = new JSONObject(json);
            JSONArray coordinates = jsonRoute.getJSONObject("geometry").getJSONArray("coordinates");
            processCoordinates(coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void processCoordinates(JSONArray coordinates) throws JSONException {
        List<String> directions = new ArrayList<>();

        JSONArray startPoint = coordinates.getJSONArray(0);
        double prevLatitude = startPoint.getDouble(1);
        double prevLongitude = startPoint.getDouble(0);

        for (int i = 1; i < coordinates.length(); i++) {
            JSONArray currentPoint = coordinates.getJSONArray(i);
            double latitude = currentPoint.getDouble(1);
            double longitude = currentPoint.getDouble(0);

            double angle = calculateAngle(Math.toRadians(prevLatitude), Math.toRadians(prevLongitude), Math.toRadians(latitude), Math.toRadians(longitude));
            String direction = determineDirection(angle);
            directions.add(direction);

            prevLatitude = latitude;
            prevLongitude = longitude;
        }

        displayDirections(directions);
    }



    private double calculateAngle(double latA, double lonA, double latB, double lonB) {
        double dLon = lonB - lonA;
        double y = Math.sin(dLon) * Math.cos(latB);
        double x = Math.cos(latA) * Math.sin(latB) - Math.sin(latA) * Math.cos(latB) * Math.cos(dLon);
        double angle = Math.atan2(y, x);
        return Math.toDegrees(angle);
    }

    private String determineDirection(double angle) {
        if (angle > -45 && angle <= 45) {
            return "Go straight";
        } else if (angle > 45 && angle <= 135) {
            return "Turn left";
        } else if (angle > -135 && angle <= -45) {
            return "Turn right";
        } else {
            return "Go straight";
        }
    }

    private void displayDirections(List<String> directions) {
        StringBuilder directionsText = new StringBuilder();
        for (int i = 0; i < directions.size(); i++) {
            directionsText.append((i + 1)).append(". ").append(directions.get(i)).append("\n");
        }
        directionsList.setText(directionsText.toString());
    }

    public void processRoute(String routeJson) {
        parseRoute(routeJson);
    }
}
