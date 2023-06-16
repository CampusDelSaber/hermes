package com.isc.hermes.model.incidents;

import java.util.ArrayList;

/**
 * This class represents the geometry of a coordinate.
 */
public class Geometry {
    private String type;
    private ArrayList<double[]> coordinates;

    public Geometry(String type, ArrayList<double[]> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    /**
     * Return the type of point geometry to represent.
     *
     * @return String type.
     */
    public String getType() {
        return type;
    }

    /**
     * Changes the type of point geometry to be represented.
     *
     * @param type receives a new name type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns an arrayList of arrays containing coordinate points.
     */
    public ArrayList<double[]> getCoordinates() {
        return coordinates;
    }

    /**
     * Changes the arrayList of arrays containing coordinate points.
     *
     * @param coordinates receives a new coordinates.
     */
    public void setCoordinates(ArrayList<double[]> coordinates) {
        this.coordinates = coordinates;
    }
}
