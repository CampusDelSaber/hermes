package com.isc.hermes.model.navigation;

import com.isc.hermes.R;
import com.isc.hermes.model.navigation.directions.DirectionEnum;
import com.isc.hermes.model.navigation.directions.DirectionsParser;
import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecordBuilder;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Response;

/**
 * The NavigationRouteParser class is responsible for parsing route information and creating a list of route segments.
 */
public class NavigationRouteParser {
    private final DirectionsParser directionsParser;
    private final RouteSegmentRecordBuilder segmentRecordBuilder;

    public NavigationRouteParser() {
        directionsParser = new DirectionsParser();
        segmentRecordBuilder = new RouteSegmentRecordBuilder();
    }

    public static final int GEO_JSON_LATITUDE = 1;
    public static final int GEO_JSON_LONGITUDE = 0;

    /**
     * Creates a list of route segments from the route information.
     *
     * @param routeInformation The JSON object containing route information.
     * @return The list of route segments.
     */
    public CompletableFuture<List<RouteSegmentRecord>> makeRouteSegments(JSONObject routeInformation) {
        CompletableFuture<List<RouteSegmentRecord>> futureRouteSegments = new CompletableFuture<>();

        // Perform the route segment calculation asynchronously
        CompletableFuture.runAsync(() -> {
            List<RouteSegmentRecord> routeSegments = new ArrayList<>();

            try {
                JSONArray route = routeInformation.getJSONObject("geometry").getJSONArray("coordinates");

                System.out.println("RUTA KARI KARI");
                System.out.println(route);
                for (int i = 0; i < route.length() - 1; i++) {
                    LatLng start = new LatLng(route.getJSONArray(i).getDouble(1), route.getJSONArray(i).getDouble(0));
                    LatLng end = new LatLng(route.getJSONArray(i + 1).getDouble(1), route.getJSONArray(i + 1).getDouble(0));

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII");
                    // Convert LatLng coordinates to Mapbox Point
                    Point startPoint = Point.fromLngLat(start.getLongitude(), start.getLatitude());
                    Point endPoint = Point.fromLngLat(end.getLongitude(), end.getLatitude());

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII  1");

                    // Retrieve the instructions using Mapbox Geocoder
                    String startFeatures = reverseGeocode(startPoint);
                    String endFeatures = reverseGeocode(endPoint);

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII  2");

                    String startStreetName = startFeatures.isEmpty() ? "" : startFeatures;
                    String endStreetName = endFeatures.isEmpty() ? "" : endFeatures;

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII  3");
                    System.out.println(startStreetName);
                    System.out.println(endStreetName);

                    DirectionsRecord[] directions = new DirectionsRecord[]{
                            new DirectionsRecord(startStreetName, DirectionEnum.GO_STRAIGHT),
                            new DirectionsRecord(endStreetName, getTurnDirection(start, end))
                    };

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII  4");

                    // Create RouteSegmentRecord object and add it to the list
                    RouteSegmentRecord routeSegment = new RouteSegmentRecord(start, end, directions);
                    routeSegments.add(routeSegment);

                    System.out.println("COMO ES KARISIRIIIIIIIIIIIII  5");
                }

                // Complete the future with the route segments
                futureRouteSegments.complete(routeSegments);
            } catch (JSONException e) {
                e.printStackTrace();
                // Complete the future exceptionally with the encountered exception
                futureRouteSegments.completeExceptionally(e);
            }
        });

        return futureRouteSegments;
    }
    private DirectionEnum getTurnDirection(LatLng start, LatLng end) {
        double bearing = calculateBearing(start, end);

        // Define the threshold angles for determining the turn direction
        double leftTurnThreshold = 45.0;
        double rightTurnThreshold = 315.0;

        if (bearing >= leftTurnThreshold && bearing < rightTurnThreshold) {
            return DirectionEnum.TURN_LEFT;
        } else {
            return DirectionEnum.TURN_RIGHT;
        }
    }

    private double calculateBearing(LatLng start, LatLng end) {
        double startLat = Math.toRadians(start.getLatitude());
        double startLng = Math.toRadians(start.getLongitude());
        double endLat = Math.toRadians(end.getLatitude());
        double endLng = Math.toRadians(end.getLongitude());

        double deltaLng = endLng - startLng;

        double y = Math.sin(deltaLng) * Math.cos(endLat);
        double x = Math.cos(startLat) * Math.sin(endLat) - Math.sin(startLat) * Math.cos(endLat) * Math.cos(deltaLng);

        double bearing = Math.atan2(y, x);
        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;

        return bearing;
    }

    private String reverseGeocode(Point point) {
        try {
            String apiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat={latitude}&lon={longitude}";
            apiUrl = apiUrl.replace("{latitude}", String.valueOf(point.latitude()));
            apiUrl = apiUrl.replace("{longitude}", String.valueOf(point.longitude()));

            System.out.println(apiUrl);
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            JSONObject jsonObject = new JSONObject(response);

            // Get the street name from the address object
            String streetName = jsonObject.getString("display_name");

            // Return the street name
            return streetName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method takes a JSON object containing route information and creates a list of route segments.
     *
     * @param cords are the coordinates.
     * @return Returns the list of route segments.
     * @throws JSONException
     */
    private LatLng unpack(JSONArray cords) throws JSONException {
        return new LatLng(cords.getDouble(GEO_JSON_LATITUDE), cords.getDouble(GEO_JSON_LONGITUDE));
    }

    /**
     * This method unpacks the coordinates from a JSON object and creates a LatLng object.
     * It receives a JSONArray containing the coordinates and returns the corresponding LatLng object.
     *
     * @param start coordinate.
     * @param end coordinate.
     * @return an array of DirectionsRecord objects.
     */
    private DirectionsRecord[] parseDirections(LatLng start, LatLng end){
        if (!directionsParser.hasAnchor()) {
            directionsParser.setAnchor(start);
        }
        return new DirectionsRecord[]{directionsParser.translate(start), directionsParser.translate(end)};
    }
}
