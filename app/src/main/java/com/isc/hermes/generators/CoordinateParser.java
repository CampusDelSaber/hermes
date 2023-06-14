package com.isc.hermes.generators;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

public class CoordinateParser {

    public Coordinate doubleToCoordinate(Double[] coordinate) {
        return coordinate.length == 2 ?
                new Coordinate(coordinate[0], coordinate[1]) : null;
    }

    public List<Double[]> parseToDoubles(Geometry polygom) {
        return parseToDoubles(List.of(polygom.getCoordinates()));
    }

    public List<Double[]> parseToDoubles(List<Coordinate> coordinates) {
        List<Double[]> doubleCoordinates = new ArrayList<>();
        coordinates.forEach(coordinate -> {
            doubleCoordinates.add(new Double[]{coordinate.getX(), coordinate.getY()});
        });

        return doubleCoordinates;
    }

    public List<Coordinate> parseToCoordinates(List<Double[]> doubleCoordinates) {
        List<Coordinate> coordinates = new ArrayList<>();
        doubleCoordinates.forEach(coordinate -> {
            coordinates.add(doubleToCoordinate(coordinate));
        });

        return coordinates;
    }
}
