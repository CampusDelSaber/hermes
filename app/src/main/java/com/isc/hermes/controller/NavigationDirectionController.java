package com.isc.hermes.controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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

    /**
     * Constructor for the NavigationDirectionController class.
     *
     * @param context               The application context.
     * @param mapWayPointController The map waypoint controller.
     */
    public NavigationDirectionController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        this.mapWayPointController = mapWayPointController;
        directionsForm = ((AppCompatActivity) context).findViewById(R.id.directions_form);

        directionsList = ((AppCompatActivity) context).findViewById(R.id.directions_list);

        Map<String, String> r = new HashMap<>();

        r.put("Route A", "{\"type\":\"Feature\",\"distance\":0.5835077072636502,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.154149,-17.393858],[-66.15306,-17.393682],[-66.15291,-17.394716],[-66.153965,-17.394903]]}}");
        r.put("Route B", "{\"type\":\"Feature\",\"distance\":0.5961126697414532,\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-66.156338,-17.394251],[-66.155208,-17.394064],[-66.155045,-17.39503],[-66.154875,-17.396151],[-66.153754,-17.395951],[-66.153965,-17.394903]]}}");
        r.put("Route C", "{}");

        String jsonA = r.get("Route A");
        String jsonB = r.get("Route B");
        String jsonC = r.get("Route C");

        processRoute(jsonA);
    }

    /**
     * Updates the UI components related to the direction points.
     */
    void updateUiPointsComponents() {
        if (isActive) {
            directionsForm.startAnimation(Animations.entryAnimation);
            directionsForm.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Gets the directions form layout.
     *
     * @return The directions form layout.
     */
    public RelativeLayout getDirectionsForm() {
        return directionsForm;
    }

    /**
     * Parses the route JSON and processes the coordinates.
     *
     * @param json The JSON string representing the route.
     */
    private void parseRoute(String json) {
        try {
            JSONObject jsonRoute = new JSONObject(json);
            JSONArray coordinates = jsonRoute.getJSONObject("geometry").getJSONArray("coordinates");
            processCoordinates(coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the coordinates and generates directions.
     *
     * @param coordinates The array of coordinates.
     */
    private void processCoordinates(JSONArray coordinates) {
        List<String> directions = new ArrayList<>();

        try {
            JSONArray startPoint = coordinates.getJSONArray(0);
            double prevLatitude = startPoint.getDouble(1);
            double prevLongitude = startPoint.getDouble(1);

            for (int i = 1; i < coordinates.length(); i++) {
                JSONArray currentPoint = coordinates.getJSONArray(i);
                double latitude = currentPoint.getDouble(1);
                double longitude = currentPoint.getDouble(0);

                String streetName = getStreetNameForCoordinates(latitude, longitude);
                String direction = determineDirection(prevLatitude, prevLongitude, latitude, longitude);
                String directionWithStreet = direction + " on " + streetName;
                directions.add(directionWithStreet);

                prevLatitude = latitude;
                prevLongitude = longitude;
            }

            displayDirections(directions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the street name for the given coordinates using a Geocoder.
     *
     * @param latitude  The latitude of the coordinates.
     * @param longitude The longitude of the coordinates.
     * @return The street name associated with the coordinates.
     */

    private String getStreetNameForCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context);
        String streetName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                streetName = address.getThoroughfare();
                if (streetName == null)
                    streetName = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return streetName;
    }

    /**
     * Determines the direction based on the previous and current coordinates.
     *
     * @param prevLatitude  The latitude of the previous coordinate.
     * @param prevLongitude The longitude of the previous coordinate.
     * @param latitude      The latitude of the current coordinate.
     * @param longitude     The longitude of the current coordinate.
     * @return The direction based on the angle between the coordinates.
     */
    private String determineDirection(double prevLatitude, double prevLongitude, double latitude, double longitude) {
        double angle = calculateAngle(Math.toRadians(prevLatitude), Math.toRadians(prevLongitude), Math.toRadians(latitude), Math.toRadians(longitude));

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

    /**
     * Calculates the angle between two sets of coordinates.
     *
     * @param latA  The latitude of the first coordinate.
     * @param lonA  The longitude of the first coordinate.
     * @param latB  The latitude of the second coordinate.
     * @param lonB  The longitude of the second coordinate.
     * @return The angle between the two coordinates.
     */

    private double calculateAngle(double latA, double lonA, double latB, double lonB) {
        double dLon = lonB - lonA;
        double y = Math.sin(dLon) * Math.cos(latB);
        double x = Math.cos(latA) * Math.sin(latB) - Math.sin(latA) * Math.cos(latB) * Math.cos(dLon);
        double angle = Math.atan2(y, x);
        return Math.toDegrees(angle);
    }

    /**
     * Displays the directions in a text format.
     *
     * @param directions The list of directions to display.
     */
    private void displayDirections(List<String> directions) {
        StringBuilder directionsText = new StringBuilder();
        for (int i = 0; i < directions.size(); i++) {
            directionsText.append((i + 1)).append(". ").append(directions.get(i)).append("\n");
        }
        directionsList.setText(directionsText.toString());
    }

    /**
     * Processes the route JSON.
     *
     * @param routeJson The JSON string representing the route.
     */
    public void processRoute(String routeJson) {
        parseRoute(routeJson);
    }

}
