package com.isc.hermes.generators;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import com.isc.hermes.requests.geocoders.StreetValidator;

import java.util.Random;

public class CoordinateGen {

    private int maxAttempts;
    private Random random;
    private StreetValidator streetValidator;


    public CoordinateGen() {
        this.random = new Random();
        this.streetValidator = new StreetValidator();
    }

    public double[] genPoint() {
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

    private double generateRandomLongitude() {
        return random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE) + MIN_LONGITUDE;
    }

    private double generateRandomLatitude() {
        return random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE) + MIN_LATITUDE;
    }

}
