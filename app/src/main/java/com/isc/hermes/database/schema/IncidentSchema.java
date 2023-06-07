package com.isc.hermes.database.schema;

/**
 * This enum represents the fields that the schema of an incident has.
 */
public enum IncidentSchema {

    ID("_id"),
    TYPE("type"),
    REASON("reason"),
    DATE_CREATED("dateCreated"),
    DEATH_DATE("deathDate"),
    GEOMETRY("geometry"),
    GEOMETRY_TYPE("geometry.type"),
    GEOMETRY_COORDINATES("geometry.coordinates");

    private final String field;

    /**
     * This is the constructor method to initialize the schema field you want to use.
     * @param field
     */
    IncidentSchema(String field) {
        this.field = field;
    }

    /**
     * This is a getter method to get the value of the created schema field.
     *
     * @return field value.
     */
    public String getField() {
        return field;
    }
}
