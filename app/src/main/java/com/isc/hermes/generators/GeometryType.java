package com.isc.hermes.generators;

/**
 * This Enum represents the types of geometry that.
 */
public enum GeometryType {
    LINESTRING("LineString"), POINT("Point"), POLYGON("Polygon");
    private String name;

    /**
     * Constructor, inicializa las valores para identificar los tipos de geometria.
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
