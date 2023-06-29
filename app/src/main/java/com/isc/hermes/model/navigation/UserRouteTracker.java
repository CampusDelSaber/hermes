package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class UserRouteTracker {
    private final CoordinatesDistanceCalculator distanceCalculator;
    private final CurrentLocationModel currentLocation;
    private final ListIterator<RouteSegmentRecord> availableSegments;
    private LatLng destination;
    private RouteSegmentRecord currentRouteSegmentRecord;
    private JSONObject routeInformation;


    public UserRouteTracker(String geoJSON) {
        try {
            routeInformation = new JSONObject(geoJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentRouteSegmentRecord = null;
        distanceCalculator = CoordinatesDistanceCalculator.getInstance();
        currentLocation = CurrentLocationModel.getInstance();
        availableSegments = makeRouteSegments().listIterator();
        updateCurrentRouteSegment(currentLocation.getLatLng());
    }

    public double getPartialSegmentDistance() {
        LatLng userLocation = currentLocation.getLatLng();
        if (!isUserInSegment(userLocation)) {
            updateCurrentRouteSegment(userLocation);
        }
        return distanceCalculator.calculateDistance(
                userLocation,
                currentRouteSegmentRecord.getEnd()
        );
    }

    private void updateCurrentRouteSegment(LatLng userLocation) {
        if (availableSegments.hasNext()) {
            currentRouteSegmentRecord = availableSegments.next();
            if (!NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation)) {
                throw new UserOutsideRouteException(currentRouteSegmentRecord, userLocation);
            }
        }
    }

    private List<RouteSegmentRecord> makeRouteSegments() {
        List<RouteSegmentRecord> routeSegments = new ArrayList<>();
        try {
            JSONArray route = routeInformation.getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < 10000; i++) {
                if (route.optJSONArray(i) != null) {
                    if (route.optJSONArray(i + 1) != null) {
                        JSONArray start = route.getJSONArray(i);
                        JSONArray end = route.getJSONArray(i + 1);
                        routeSegments.add(new RouteSegmentRecord(
                                new LatLng(start.getDouble(0), start.getDouble(1)),
                                new LatLng(end.getDouble(0), end.getDouble(1))
                        ));
                    }else {
                        JSONArray dest = route.getJSONArray(i);
                        destination = new LatLng(dest.getDouble(0), dest.getDouble(1));
                        break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeSegments;
    }

    public boolean hasUserArrived() {
        return NavigationTrackerTools.isPointReached(destination, currentLocation.getLatLng());
    }

    public boolean isUserInSegment(LatLng userLocation) {
        boolean isInsideSegment = NavigationTrackerTools.isInsideSegment(currentRouteSegmentRecord, userLocation);
        boolean isEndReached = NavigationTrackerTools.isPointReached(currentRouteSegmentRecord.getEnd(), userLocation);

        return isInsideSegment && !isEndReached;
    }

    public JSONObject getRouteInformation() {
        return routeInformation;
    }
}
