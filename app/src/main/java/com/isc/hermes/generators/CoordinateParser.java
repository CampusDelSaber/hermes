package com.isc.hermes.generators;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the responsibility to parse coordinate data.
 */
public class CoordinateParser {

    /**
     * This method parse double coordinate to coordinate type.
     *
     * @param coordinate is the double coordinate.
     * @return coordinate type.
     */
    public Coordinate doubleToCoordinate(Double[] coordinate) {
        return coordinate.length == 2 ?
                new Coordinate(coordinate[0], coordinate[1]) : null;
    }

    /**
     * This method parse the coordinates of polygon to list of double coordinates.
     *
     * @param polygom is the polygon to ger coordinates data.
     * @return double coordinates.
     */
    public List<Double[]> parseToDoubles(Geometry polygom) {
        return parseToDoubles(List.of(polygom.getCoordinates()));
    }

    /**
     * This method parse coordinate coordinates list to double coordinate list.
     *
     * @param coordinates are the coordinates of coordinate types.
     * @return double coordinates list.
     */
    public List<Double[]> parseToDoubles(List<Coordinate> coordinates) {
        List<Double[]> doubleCoordinates = new ArrayList<>();
        coordinates.forEach(coordinate -> {
            doubleCoordinates.add(new Double[]{coordinate.getX(), coordinate.getY()});
        });

        return doubleCoordinates;
    }

    /**
     * This method parse double coordinates to list of coordinate types.
     *
     * @param doubleCoordinates are the double coordinates.
     * @return list of coordinates types.
     */
    public List<Coordinate> parseToCoordinates(List<Double[]> doubleCoordinates) {
        List<Coordinate> coordinates = new ArrayList<>();
        doubleCoordinates.forEach(coordinate -> {
            coordinates.add(doubleToCoordinate(coordinate));
        });

        return coordinates;
    }
}
