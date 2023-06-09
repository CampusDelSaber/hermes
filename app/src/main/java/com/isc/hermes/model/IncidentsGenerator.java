package com.isc.hermes.model;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IncidentsGenerator {

    public static Polygon[] generateIncidentsZones(double currentLat,
                                                         double currentLng,
                                                         int quantity) {
        Polygon[] polygons = new Polygon[quantity];
        for (int i = 0; i < quantity; i++) {
            Polygon polygon = getRandomPolygon(currentLat, currentLng, 5);
            polygons[i] = polygon;
            System.out.println("NEW POLYGON: " + polygon);
        }
        return polygons;
    }

    public static Polygon getRandomPolygon(double currentLat, double currentLng, int numVertex) {
        LatLng[] coordinatesList = CoordinatesGenerator.getVariousCloserCoordinates(
                currentLat,
                currentLng,
                numVertex);

        Polygon polygon = Polygon.fromLngLats(convertToList(coordinatesList));
        System.out.println(polygon);
        return polygon;
    }
    
    public static List<List<Point>> convertToList(LatLng[] coordinatesList) {
        List<List<Point>> points = new ArrayList<>();
        points.add(new ArrayList<>());
        for (LatLng latLng :
                coordinatesList) {
            points.get(0).add(Point.fromLngLat(latLng.getLatitude(), latLng.getLongitude()));
        }

        return points;
    }
}
