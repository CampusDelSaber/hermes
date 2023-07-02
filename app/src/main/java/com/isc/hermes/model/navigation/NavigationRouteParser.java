package com.isc.hermes.model.navigation;

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

    public static final int GEO_JSON_LATITUDE = 1;
    public static final int GEO_JSON_LONGITUDE = 0;

    /**
     * Creates a list of route segments from the route information.
     *
     * @param routeInformation The JSON object containing route information.
     * @return The list of route segments.
     */
    public static List<RouteSegmentRecord> makeRouteSegments(JSONObject routeInformation) {
        List<RouteSegmentRecord> routeSegments = new ArrayList<>();
        try {
            JSONArray route = routeInformation.getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < 10000; i++) {
                if (route.optJSONArray(i) != null) {
                    if (route.optJSONArray(i + 1) != null) {
                        JSONArray start = route.getJSONArray(i);
                        JSONArray end = route.getJSONArray(i + 1);
                        routeSegments.add(new RouteSegmentRecord(
                                new LatLng(start.getDouble(GEO_JSON_LATITUDE), start.getDouble(GEO_JSON_LONGITUDE)),
                                new LatLng(end.getDouble(GEO_JSON_LATITUDE), end.getDouble(GEO_JSON_LONGITUDE))
                        ));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeSegments;
    }
}
