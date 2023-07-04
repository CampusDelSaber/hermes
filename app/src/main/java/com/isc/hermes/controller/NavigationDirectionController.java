package com.isc.hermes.controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to get the turn by turn directions from the route chosen
 */
public class NavigationDirectionController {
    private final Context context;
    private final RelativeLayout directionsForm;
    private TextView directionsList;

    /**
     * Constructor for the NavigationDirectionController class.
     *
     * @param context               The application context.
     */
    public NavigationDirectionController(Context context) {
        this.context = context;
        directionsForm = ((AppCompatActivity) context).findViewById(R.id.directions_form);
        directionsList = ((AppCompatActivity) context).findViewById(R.id.directions_list);
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
     * @param routeJson The JSON string representing the route.
     */
    private void parseRoute(JSONObject routeJson) {
        try {
            JSONArray coordinates = routeJson.getJSONObject("geometry").getJSONArray("coordinates");
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
        try {
            JSONArray startPoint = coordinates.getJSONArray(0);
            double prevLatitude = startPoint.getDouble(1);
            double prevLongitude = startPoint.getDouble(1);
            setDirectionsArray(coordinates, prevLatitude, prevLongitude);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the directions array based on the provided coordinates.
     *
     * @param coordinates  the JSONArray containing the coordinates
     * @param prevLatitude the latitude of the previous point
     * @param prevLongitude the longitude of the previous point
     * @throws JSONException if there is an error while parsing the coordinates
     */
    private void setDirectionsArray(JSONArray coordinates, double prevLatitude, double prevLongitude) throws JSONException {
        List<String> directions = new ArrayList<>();
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
    public void processRoute(JSONObject routeJson) {
        parseRoute(routeJson);
    }
}
