package com.isc.hermes.generators;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.ArrayList;
import java.util.List;

public class PolygonGenerator extends CoordinateGen {

    private Geometry polygon;
    private PointGenerator pointGenerator;
    private List<Coordinate> polygonCoordinates;
    private GeometryFactory geometryFactory;
    private DelaunayTriangulationBuilder triangulationBuilder;
    private CoordinateParser coordinateParser;


    public PolygonGenerator() {
        this.coordinateParser = new CoordinateParser();
        this.pointGenerator = new PointGenerator();
        this.polygonCoordinates = new ArrayList<>();
        this.geometryFactory = new GeometryFactory();
        this.triangulationBuilder = new DelaunayTriangulationBuilder();
    }

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

    private boolean isValidToGeneratePolygon(Double[] referencePoint) {
        return referencePoint != null && referencePoint.length == 2;
    }

    public Geometry buildTriangulation(List<Coordinate> polygonCoordinates) {
        triangulationBuilder.setSites(polygonCoordinates);
        return triangulationBuilder.getTriangles(geometryFactory).union();
    }

}
