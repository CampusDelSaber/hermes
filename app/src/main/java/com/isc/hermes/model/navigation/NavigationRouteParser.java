package com.isc.hermes.model.navigation;

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

import java.io.IOException;
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

                for (int i = 0; i < route.length() - 1; i++) {
                    LatLng start = unpack(route.getJSONArray(i));
                    LatLng end = unpack(route.getJSONArray(i + 1));

                    // Convert LatLng coordinates to Mapbox Point
                    Point startPoint = Point.fromLngLat(start.getLongitude(), start.getLatitude());
                    Point endPoint = Point.fromLngLat(end.getLongitude(), end.getLatitude());

                    // Retrieve the instructions using Mapbox Geocoder
                    List<CarmenFeature> startFeatures = reverseGeocode(startPoint);
                    List<CarmenFeature> endFeatures = reverseGeocode(endPoint);

                    String startStreetName = startFeatures.isEmpty() ? "" : startFeatures.get(0).text();
                    String endStreetName = endFeatures.isEmpty() ? "" : endFeatures.get(0).text();

                    DirectionsRecord[] directions = new DirectionsRecord[]{
                            new DirectionsRecord(startStreetName, DirectionEnum.GO_STRAIGHT),
                            new DirectionsRecord(endStreetName, getTurnDirection(start, end))
                    };

                    // Create RouteSegmentRecord object and add it to the list
                    RouteSegmentRecord routeSegment = new RouteSegmentRecord(start, end, directions);
                    routeSegments.add(routeSegment);
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

    private List<CarmenFeature> reverseGeocode(Point point) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken("YOUR_MAPBOX_ACCESS_TOKEN")
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();
            Response<GeocodingResponse> response = client.executeCall();
            if (response.isSuccessful()) {
                GeocodingResponse geocodingResponse = response.body();
                if (geocodingResponse != null) {
                    return geocodingResponse.features();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
