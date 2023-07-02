package com.isc.hermes.model.navigation;

import com.isc.hermes.model.navigation.directions.DirectionsParser;
import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecordBuilder;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The NavigationRouteParser class is responsible for parsing route information and creating a list of route segments.
 */
public class NavigationRouteParser {
    private DirectionsParser directionsParser;
    private RouteSegmentRecordBuilder segmentRecordBuilder;

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
    public List<RouteSegmentRecord> makeRouteSegments(JSONObject routeInformation) {
        List<RouteSegmentRecord> routeSegments = new ArrayList<>();

        try {
            JSONArray route = routeInformation.getJSONObject("geometry").getJSONArray("coordinates");

            for (int i = 0; i < 10000; i++) {
                if (route.optJSONArray(i) != null) {
                    if (route.optJSONArray(i + 1) != null) {
                        LatLng start = unpack(route.getJSONArray(i));
                        LatLng end = unpack(route.getJSONArray(i + 1));
                        segmentRecordBuilder.setStart(start)
                               .setEnd(end)
                               .setDirections(parseDirections(start, end));

                        routeSegments.add(segmentRecordBuilder.createRouteSegmentRecord());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeSegments;
    }

    private LatLng unpack(JSONArray cords) throws JSONException {
        return new LatLng(cords.getDouble(GEO_JSON_LATITUDE), cords.getDouble(GEO_JSON_LONGITUDE));
    }

    private DirectionsRecord[] parseDirections(LatLng start, LatLng end){
        if (!directionsParser.hasAnchor()) {
            directionsParser.setAnchor(start);
        }
        return new DirectionsRecord[]{directionsParser.translate(start), directionsParser.translate(end)};
    }
}
