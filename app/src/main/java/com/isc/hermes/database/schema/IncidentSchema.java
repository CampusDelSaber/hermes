package com.isc.hermes.database.schema;

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

    IncidentSchema(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
