package com.isc.hermes.generators;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import com.isc.hermes.requests.geocoders.StreetValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class has the responsibility to generate random coordinates.
 */
public class CoordinateGen {

    protected Random random;
    protected Double[] coordinate;
    protected List<Double[]> coordinates;
    protected StreetValidator streetValidator;

    /**
     * This is the constructor method to initialize necessary variables.
     */
    public CoordinateGen() {
        this.random = new Random();
        this.coordinate = new Double[2];
        this.coordinates = new ArrayList<>();
        this.streetValidator = new StreetValidator();
    }

    /**
     * This method reset the content of coordinates list.
     */
    protected void resetGeneratedCoordinates() {
        coordinates.clear();
    }

    /**
     * This method generate a random angle.
     *
     * @return random angle.
     */
    protected double getRandomAngle() {
        return Math.random() * 2 * Math.PI;
    }

    /**
     * This method generate the distance using a radium.
     *
     * @param radium to generate the near distance.
     * @return near distance.
     */
    protected double getNearDistance(Radium radium) {
        return Math.random() * radium.getValue();
    }

    /**
     * This method generate a random longitude within the geospatial limits of the world.
     *
     * @return longitude coordinate.
     */
    protected double generateRandomLongitude() {
        return random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE) + MIN_LONGITUDE;
    }

    /**
     * This method generate a random latitude within the geospatial limits of the world.
     *
     * @return latitude coordinate.
     */
    protected double generateRandomLatitude() {
        return random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE) + MIN_LATITUDE;
    }

}
