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

    /**
     * This method generate a string with the coordinates list.
     *
     * @return String with the coordinates.
     */
    public String getStringCoordinates(){
        StringBuilder stringBuilder = new StringBuilder("[");
        if(coordinates.size() > 1){
            for (Double[] currentCoordinate : coordinates){
                stringBuilder.append("["+currentCoordinate[0]+", "+currentCoordinate[1]+"],");
            }
            stringBuilder.deleteCharAt(stringBuilder.toString().length()-1);
            stringBuilder.append("]");
        }else {
            stringBuilder.append(coordinates.get(0)[0]+", "+coordinates.get(0)[1]+"]");
        }
        return stringBuilder.toString();
    }

    /**
     * This is the toString method to get a geometry like string.
     *
     * @return geometry string.
     */
    @Override
    public String toString() {
        return "{" +
                "type:" + type +",\n" +
                "coordinates: " + getStringCoordinates() + "\n" +
                "}";
    }
}
