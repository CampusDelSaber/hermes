package com.isc.hermes.utils;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generate different shapes of incidents; zones, points, and lines.
 */
public class IncidentsGenerator {

    /**
     * This method generates many random polygons nearby the location.
     * @param currentLat Latitude of the location to generate polygon nearby.
     * @param currentLng Longitude of the location to generate polygon nearby.
     * @param quantity How many polygons need.
     * @return an array of polygons
     */
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

    /**
     * This method us used to generate one Polygon nearby the location.
     * @param currentLat Latitude of the location to generate polygon nearby.
     * @param currentLng Longitude of the location to generate polygon nearby.
     * @param numVertex number of vertex of the polygon.
     * @return an Polygon (MapBox)
     */
    public static Polygon getRandomPolygon(double currentLat, double currentLng, int numVertex) {
        LatLng[] coordinatesList = CoordinatesGenerator.getVariousCloserCoordinates(
                currentLat,
                currentLng,
                numVertex);

        Polygon polygon = Polygon.fromLngLats(convertToList(coordinatesList));
        System.out.println(polygon);
        return polygon;
    }

    /**
     *
     * @param coordinatesList
     * @return
     */
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
