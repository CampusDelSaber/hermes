package com.isc.hermes.model.incidents;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * This class represents the characteristics of an incident.
 */
public class Incident {

    private ObjectId _id;
    private String type;
    private String reason;
    private Date dateCreated;
    private Date deadthDate;
    private Geometry geometry;

    public Incident(ObjectId _id, String type, String reason, Date dateCreated, Date deadthDate, Geometry geometry) {
        this._id = _id;
        this.type = type;
        this.reason = reason;
        this.dateCreated = dateCreated;
        this.deadthDate = deadthDate;
        this.geometry = geometry;
    }

    /**
     * Gets the incident ID
     *
     * @return ObjectId type
     */
    public ObjectId getId() {
        return _id;
    }

    /**
     * Gets the type of accident.
     *
     * @return String type.
     */
    public String getType() {
        return type;
    }

    /**
     * Obtains the reason for the accident.
     *
     * @return String type.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets the start date of the incident.
     *
     * @return
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Get the date up to the approximate duration of the incident.
     *
     * @return Date type.
     */
    public Date getDeathDate() {
        return deadthDate;
    }

    /**
     * Returns a geometry object containing geospatial data
     *
     * @return Geometry type.
     */
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public String toString() {
        return "Incident{"+
                "\n_id = " + _id +
                ",\ntype = " + type + '\'' +
                ",\nreason = " + reason + '\'' +
                ",\ndateCreated = " + dateCreated +
                ",\ndeadthDate = " + deadthDate +
                ",\ngeometry = " + geometry +
                '}';
    }
}