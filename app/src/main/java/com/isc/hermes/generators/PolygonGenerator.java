package com.isc.hermes.generators;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This method has the responsibility to generate polygon coordinates data.
 */
public class PolygonGenerator extends CoordinateGen {

    private Geometry polygon;
    private PointGenerator pointGenerator;
    private List<Coordinate> polygonCoordinates;
    private GeometryFactory geometryFactory;
    private DelaunayTriangulationBuilder triangulationBuilder;
    private CoordinateParser coordinateParser;

    /**
     * This is the constructor method to initialize all necessary variables to
     * generate polygon coordinates data.
     */
    public PolygonGenerator() {
        this.coordinateParser = new CoordinateParser();
        this.pointGenerator = new PointGenerator();
        this.polygonCoordinates = new ArrayList<>();
        this.geometryFactory = new GeometryFactory();
        this.triangulationBuilder = new DelaunayTriangulationBuilder();
    }

    /**
     * This method generate a polygon coordinates using a limited range.
     *
     * @param referencePoint to get the main point reference of limited range.
     * @param radium to get the radium using the reference point.
     * @return the polygon coordinates generated.
     */
    public List<Double[]> getPolygon(Double[] referencePoint, Radium radium) {
        polygonCoordinates.clear();

        if (isValidToGeneratePolygon(referencePoint)) {
            polygonCoordinates.add(new Coordinate(referencePoint[0], referencePoint[1]));
            for (int i = 0; i < 15; i++) {
                coordinate = pointGenerator.getNearPoint(referencePoint, radium);
                polygonCoordinates.add(new Coordinate(coordinate[0], coordinate[1]));
            }
            polygon = buildTriangulation(polygonCoordinates);
        }

        return coordinateParser.parseToDoubles(polygon);
    }

    /**
     * This method verify if the reference point is valid to generate a polygon coordinates.
     *
     * @param referencePoint to verify if it valid.
     * @return boolean to know if it is valid.
     */
    private boolean isValidToGeneratePolygon(Double[] referencePoint) {
        return referencePoint != null && referencePoint.length == 2;
    }

    /**
     * This method build triangulation using the polygon coordinates generated.
     *
     * @param polygonCoordinates are the coordinates generated.
     * @return triangulation built.
     */
    public Geometry buildTriangulation(List<Coordinate> polygonCoordinates) {
        triangulationBuilder.setSites(polygonCoordinates);
        return triangulationBuilder.getTriangles(geometryFactory).union();
    }

}
