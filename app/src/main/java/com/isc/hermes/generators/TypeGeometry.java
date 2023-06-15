package com.isc.hermes.generators;

public enum TypeGeometry {
    LINESTRING("LineString"), POINT("Point"), POLYGON("Polygon");
    private String name;
    TypeGeometry(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
