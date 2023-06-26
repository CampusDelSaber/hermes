package com.isc.hermes.model.incidents;

/**
 * This Enum represents the types of geometry that.
 */
public enum GeometryType {

    LINE_STRING("LineString"),
    POINT("Point"),
    POLYGON("Polygon");

    private final String name;

    /**
     * This is the constructor method to initialize the geometry type value.
     *
     * @param name Name of geometry.
     */
    GeometryType(String name) {
        this.name = name;
    }

    /**
     * Return the name of a geometry type.
     *
     * @return geometry name.
     */
    public String getName(){
        return name;
    }
}
