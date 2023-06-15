package com.isc.hermes.model.incidents;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the geometry of a coordinate.
 */
public class Geometry {
    private String type;
    private List<Double[]> coordinates;

    public Geometry(String type, List<Double[]> coordinates) {
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
    public List<Double[]> getCoordinates() {
        return coordinates;
    }

    /**
     * Changes the arrayList of arrays containing coordinate points.
     *
     * @param coordinates receives a new coordinates.
     */
    public void setCoordinates(ArrayList<Double[]> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Geometry{" +
                "\ntype='" + type + '\'' +
                ",\ncoordinates = \t");
        for(Double[] currentCoordinate: coordinates){
            stringBuilder.append("[ "+ currentCoordinate[0] + ", " + currentCoordinate[1]+" ]");
        }
        return stringBuilder.toString();
    }
}
