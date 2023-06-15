package com.isc.hermes.model.incidents;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Model class that represents an incident.
 */
public class Incident {
    private String id;
    private String type;
    private String reason;
    private Date dateCreated;
    private Date deathDate;
    private JSONObject geometry;

    /**
     * Constructs an instance of Incident.
     *
     * @param id          The ID of the incident.
     * @param type        The type of the incident.
     * @param reason      The reason for the incident.
     * @param dateCreated The date the incident was created.
     * @param deathDate   The date of death related to the incident.
     * @param geometry    The geometry information of the incident.
     */
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

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    /**
     * Retrieves the geometry information of the incident.
     *
     * @return The geometry information.
     */
    public JSONObject getGeometry() {
        return geometry;
    }

    /**
     * Retrieves the point coordinates of the incident as a LatLng object.
     *
     * @return The LatLng object representing the point coordinates.
     * @throws JSONException if there is an error parsing the JSON or accessing the coordinates.
     */
    public LatLng getPointCoordinates() throws JSONException {
        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");
        double lng = coordinatesArray.getDouble(0);
        double lat = coordinatesArray.getDouble(1);
        return new LatLng(lat, lng);
    }
}
