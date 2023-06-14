package com.isc.hermes.model.incidents;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Incident {
    private String id;
    private String type;
    private String reason;
    private Date dateCreated;
    private Date deathDate;
    private JSONObject geometry;

    public Incident(
            String id, String type, String reason, Date dateCreated, Date deathDate,
            JSONObject geometry
    ) {
        this.id = id;
        this.type = type;
        this.reason = reason;
        this.dateCreated = dateCreated;
        this.deathDate = deathDate;
        this.geometry = geometry;
    }

    public JSONObject getGeometry() {
        return geometry;
    }

    public LatLng getPointCoordinates() throws JSONException {
        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");
        double lng = coordinatesArray.getDouble(0);
        double lat = coordinatesArray.getDouble(1);
        return new LatLng(lat, lng);
    }
}
