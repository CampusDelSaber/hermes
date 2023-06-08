package com.isc.hermes.requests.geocoders;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReverseGeocoding extends Geocoder{

    public CarmenFeature getPointInfo(
            double longitude,
            double latitude,
            String geocodingCriteria
    ) {
        Point queryPoint = Point.fromLngLat(longitude, latitude);
        return getInfoResponse(getGeocodingBuilt(queryPoint, geocodingCriteria));
    }

    public CarmenFeature getLineStringInfo(
            List<double[]> coordinates,
            String geocodingCriteria
    ) {
        String queryString = String.valueOf(LineString.fromLngLats(toPointsCoordinates(coordinates)));
        return getInfoResponse(getGeocodingBuilt(queryString, geocodingCriteria));
    }

    public CarmenFeature getPolygonInfo(
            List<double[]> coordinates,
            String geocodingCriteria
    ) {
        String queryString = String.valueOf(Polygon.fromLngLats(
                Collections.singletonList(toPointsCoordinates(coordinates))));
        return getInfoResponse(getGeocodingBuilt(queryString, geocodingCriteria));
    }

    private MapboxGeocoding getGeocodingBuilt(Point queryPoint, String geocodingCriteria) {
        return MapboxGeocoding.builder()
                .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                .query(queryPoint)
                .geocodingTypes(geocodingCriteria)
                .build();
    }

    private MapboxGeocoding getGeocodingBuilt(String queryString, String geocodingCriteria) {
        return MapboxGeocoding.builder()
                .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                .query(queryString)
                .geocodingTypes(geocodingCriteria)
                .build();
    }

    private List<Point> toPointsCoordinates(List<double[]> coordinates) {
        List<Point> points = new ArrayList<>();
        coordinates.forEach(doubleCoordinate -> {
            points.add(Point.fromLngLat(doubleCoordinate[0], doubleCoordinate[1]));
        });

        return points;
    }
}
