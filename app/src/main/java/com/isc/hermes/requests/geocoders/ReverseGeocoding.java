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

/**
 * This class is responsible for using mapbox reverse geocoding.
 */
public class ReverseGeocoding extends Geocoder{

    /**
     * This method get information about a point coordinates.
     *
     * @param longitude is the longitude of the point.
     * @param latitude is the latitude of the point.
     * @param geocodingCriteria is the criteria to build the reverse geocoding.
     * @return information using a point coordinate.
     */
    public CarmenFeature getPointInfo(
            double longitude,
            double latitude,
            String geocodingCriteria
    ) {
        Point queryPoint = Point.fromLngLat(longitude, latitude);
        return getInfoResponse(builtReverseGeocoding(queryPoint, geocodingCriteria));
    }

    /**
     * This method get information about a Linestring coordinates.
     *
     * @param coordinates are the coordinates of the linestring.
     * @param geocodingCriteria is the criteria to build the reverse geocoding.
     * @return information using a linestring coordinates.
     */
    public CarmenFeature getLineStringInfo(
            List<double[]> coordinates,
            String geocodingCriteria
    ) {
        String queryString = String.valueOf(LineString.fromLngLats(toPointsCoordinates(coordinates)));
        return getInfoResponse(builtReverseGeocoding(queryString, geocodingCriteria));
    }

    /**
     * This method get information about a Polygon coordinates.
     *
     * @param coordinates are the coordinates of the polygon.
     * @param geocodingCriteria is the criteria to build the reverse geocoding.
     * @return information using a polygon coordinates.
     */
    public CarmenFeature getPolygonInfo(
            List<double[]> coordinates,
            String geocodingCriteria
    ) {
        String queryString = String.valueOf(Polygon.fromLngLats(
                Collections.singletonList(toPointsCoordinates(coordinates))));
        return getInfoResponse(builtReverseGeocoding(queryString, geocodingCriteria));
    }

    /**
     * This method built a reverse geocoding for a point coordinate.
     *
     * @param queryPoint is the point coordinate query.
     * @param geocodingCriteria is the criteria to build the reverse geocoding.
     * @return reverse geocoding built.
     */
    private MapboxGeocoding builtReverseGeocoding(Point queryPoint, String geocodingCriteria) {
        return MapboxGeocoding.builder()
                .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                .query(queryPoint)
                .geocodingTypes(geocodingCriteria)
                .build();
    }

    /**
     * This method built a reverse geocoding for a lot coordinates.
     *
     * @param queryString are the coordinates query.
     * @param geocodingCriteria is the criteria to build the reverse geocoding.
     * @return reverse geocoding built.
     */
    private MapboxGeocoding builtReverseGeocoding(String queryString, String geocodingCriteria) {
        return MapboxGeocoding.builder()
                .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                .query(queryString)
                .geocodingTypes(geocodingCriteria)
                .build();
    }

    /**
     * This method parse double coordinate to points coordinates.
     *
     * @param coordinates are the double coordinates.
     * @return points coordinates list.
     */
    private List<Point> toPointsCoordinates(List<double[]> coordinates) {
        List<Point> points = new ArrayList<>();
        coordinates.forEach(doubleCoordinate -> {
            points.add(Point.fromLngLat(doubleCoordinate[0], doubleCoordinate[1]));
        });

        return points;
    }
}
