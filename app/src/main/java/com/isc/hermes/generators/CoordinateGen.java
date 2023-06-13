package com.isc.hermes.generators;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import com.isc.hermes.requests.geocoders.StreetValidator;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class has the responsibility to generate random coordinates.
 */
public class CoordinateGen {

    private int maxAttempts;
    private Random random;
    private StreetValidator streetValidator;

    /**
     * This is the constructor method to initialize necessary variables.
     */
    public CoordinateGen() {
        this.random = new Random();
        this.streetValidator = new StreetValidator();
    }

    /**
     * This method generate a point coordinate within a habitable zone.
     *
     * @return point coordinate.
     */
    public double[] generatePoint() {
        double[] pointCoordinates = new double[2];
        maxAttempts = 10000;

        do {
            pointCoordinates[0] = generateRandomLongitude();
            pointCoordinates[1] = generateRandomLatitude();
            maxAttempts--;
        } while (!streetValidator.isPointStreet(pointCoordinates[0], pointCoordinates[1])
                && maxAttempts > 0);

        return pointCoordinates;
    }

    /**
     * This method generate a valid point surrounded by another as a reference.
     *
     * @param referencePoint to found others points.
     * @param radium the size radium valid to found a coordinates.
     * @return near point.
     */
    public double[] getNearPoint(double[] referencePoint, Radium radium) {
        double[] pointCoordinates = new double[2];
        int maxAttempts = 10000;
        double randomAngle, distance;

        for (int i = 0; i < maxAttempts; i++) {
            randomAngle = Math.random() * 2 * Math.PI;
            distance = Math.random() * radium.getValue();
            pointCoordinates[0] = referencePoint[0] + distance * Math.cos(randomAngle);
            pointCoordinates[1] = referencePoint[1] + distance * Math.sin(randomAngle);

            if (streetValidator.isPointStreet(pointCoordinates[0], pointCoordinates[1])) {
                return pointCoordinates;
            }
        }
        return null;
    }

    private boolean isValidToGeneratePolygon(double[] referencePoint, int amountEdges) {
        return referencePoint != null && amountEdges > 2;
    }

    public Geometry generatePolygon(double[] referencePoint, Radium radium, int amountEdges) {
        Geometry geometry = null;
        List<Coordinate> coordinates = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();

        if (isValidToGeneratePolygon(referencePoint, amountEdges)) {
            coordinates.add(new Coordinate(referencePoint[0], referencePoint[1]));
            double[] newCoordinate;
            for (int i = 0; i < amountEdges; i++) {
                newCoordinate = getNearPoint(referencePoint, radium);
                coordinates.add(new Coordinate(newCoordinate[0], newCoordinate[1]));
            }
            triangulationBuilder.setSites(coordinates);
            geometry = triangulationBuilder.getTriangles(geometryFactory).union();
        }

        return geometry;
    }

    /**
     * This method generate a valid lineString coordinates.
     *
     * @param amountPoints that has the lineString.
     * @return lineString coordinates generated.
     */
    public List<double[]> generateLineString(Radium radium, int amountPoints) {
        List<double[]> pointCoordinates = new ArrayList<>();
        double[] startPoint = generatePoint();
        double[] previousPoint, newPoint;

        if (startPoint != null) {
            pointCoordinates.add(startPoint);

            for (int i = 1; i < amountPoints; i++) {
                previousPoint = pointCoordinates.get(i - 1);
                newPoint = getNearPoint(previousPoint, radium);
                if (newPoint == null) return pointCoordinates;
                pointCoordinates.add(newPoint);
            }
        }

        return pointCoordinates;
    }

    /**
     * This method generate a random longitude within the geospatial limits of the world.
     *
     * @return longitude coordinate.
     */
    private double generateRandomLongitude() {
        return random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE) + MIN_LONGITUDE;
    }

    /**
     * This method generate a random latitude within the geospatial limits of the world.
     *
     * @return latitude coordinate.
     */
    private double generateRandomLatitude() {
        return random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE) + MIN_LATITUDE;
    }

}
