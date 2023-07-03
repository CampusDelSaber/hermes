package com.isc.hermes.generators;

import android.util.Log;

import com.isc.hermes.model.Radium;
import com.isc.hermes.model.incidents.GeometryType;
import com.mongodb.client.model.geojson.PolygonCoordinates;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.tri.TriangulationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This method has the responsibility to generate polygon coordinates data.
 */
public class PolygonGenerator extends CoordinateGen implements CoordinatesGenerable {

    //private Geometry polygon;
    /*private PointGenerator pointGenerator;
    private List<Coordinate> polygonCoordinates;
    private GeometryFactory geometryFactory;
    private DelaunayTriangulationBuilder triangulationBuilder;
    private CoordinateParser coordinateParser;*/

    /**
     * This is the constructor method to initialize all necessary variables to
     * generate polygon coordinates data.
     */
    public PolygonGenerator() {
       /* this.coordinateParser = new CoordinateParser();
        this.pointGenerator = new PointGenerator();
        this.polygonCoordinates = new ArrayList<>();
        this.geometryFactory = new GeometryFactory();
        this.triangulationBuilder = new DelaunayTriangulationBuilder();*/
    }

    /**
     * This method generate a polygon coordinates using a limited range.
     *
     * @param referencePoint to get the main point reference of limited range.
     * @param radium to get the radium using the reference point.
     * @param vertexAmount to define the number of vertices of the polygon.
     * @return the polygon coordinates generated.
     */
    @Override
    public List<Double[]> generate(Double[] referencePoint, Radium radium, int vertexAmount) {
        List<Coordinate> polygonCoordinates = new ArrayList<>();
        CoordinateParser coordinateParser = new CoordinateParser();
        Geometry polygon = null;
        if (isValidReferencePoint(referencePoint)) {
            polygonCoordinates.add(coordinateParser.doubleToCoordinate(referencePoint));
            polygonCoordinates = genSymmetricPolygon(referencePoint, radium, vertexAmount);
            polygon = buildTriangulation(polygonCoordinates);
        }

        return coordinateParser.parseToDoubles(polygon);
    }

    /**
     * This method generate coordinates to represent a symmetric polygon.
     *
     * @param referencePoint to get the main point reference of limited range.
     * @param radium to get the radium using the reference point.
     * @param vertexAmount to define the number of vertices of the polygon.
     * @return symmetric polygon coordinates generated.
     */
    public List<Coordinate> genSymmetricPolygon(
            Double[] referencePoint, Radium radium, int vertexAmount)
    {
        PointGenerator pointGenerator = new PointGenerator();
        List<Coordinate> polygonCoordinates = new ArrayList<>();
        CoordinateParser coordinateParser = new CoordinateParser();

        double currentAngle;
        double angleIncrement = 2 * Math.PI / vertexAmount;


        for (int i = 0; i < vertexAmount; i++) {
            currentAngle = i * angleIncrement;
            coordinate = pointGenerator.getNearPoint(referencePoint, radium, currentAngle);
            polygonCoordinates.add(coordinateParser.doubleToCoordinate(coordinate));
        }

        return polygonCoordinates;
    }

    /**
     * This method returns the type of geometric object that is generated.
     *
     * @return TypeGeometry type.
     */
    @Override
    public GeometryType getTypeGeometry() {
        return GeometryType.POLYGON;
    }

    /**
     * This method build triangulation using the polygon coordinates generated.
     *
     * @param polygonCoordinates are the coordinates generated.
     * @return triangulation built.
     */
    public Geometry buildTriangulation(List<Coordinate> polygonCoordinates) {
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        GeometryFactory geometryFactory = new GeometryFactory();
        triangulationBuilder.setSites(polygonCoordinates);
        return triangulationBuilder.getTriangles(geometryFactory).union();
    }

}
