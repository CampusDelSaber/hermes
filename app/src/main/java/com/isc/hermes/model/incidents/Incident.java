package com.isc.hermes.model.incidents;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * This class represents the characteristics of an incident.
 */
public class Incident {
    
    private String type;
    private String reason;
    private Date dateCreated;
    private Date deathDate;
    private Geometry geometry;

    public Incident(String type, String reason, Date dateCreated, Date deathDate, Geometry geometry) {
        this.type = type;
        this.reason = reason;
        this.dateCreated = dateCreated;
        this.deathDate = deathDate;
        this.geometry = geometry;
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
        return deathDate;
    }

    /**
     * Returns a geometry object containing geospatial data
     *
     * @return Geometry type.
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * This is the toString method to build a incident js object.
     *
     * @return incident js object.
     */
    @NonNull
    @Override
    public String toString() {
        return "Incident{"+
                ",\ntype = " + type + '\'' +
                ",\nreason = " + reason + '\'' +
                ",\ndateCreated = " + dateCreated +
                ",\ndeadthDate = " + deathDate +
                ",\ngeometry = " + geometry +
                '}';
    }
}
