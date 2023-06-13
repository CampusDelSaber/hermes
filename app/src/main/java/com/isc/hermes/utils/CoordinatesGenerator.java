package com.isc.hermes.utils;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Random;

/**
 * This class generate random coordinates,
 * to put different attributes like incidents aleatory on the map.
 *
 * The methods use a location (Latitude and Longitude) to generate random coordinates nearby.
 * Also use an distance to generate the coordinates around this distance.
 *
 * The best way to use the methods is put the current location on the lat lng fields required.
 */
public class CoordinatesGenerator {
    // City distance is used to generate coordinates on the city of the location.
    private static final int cityQuadrantDistance = 30000;
    // Neighborhood distance is used to generate coordinates on the neighborhood of the location.
    private static final int neighborhoodQuadrantDistance = 2000;

    /**
     * This method get an random coordinate around to one location.
     * @param currentLat Latitude of the location to generate coordinate nearby.
     * @param currentLng Longitude of the location to generate coordinate nearby.
     * @param distance The distance define how far away can the coordinate be.
     * @return an LatLng object to represent an point on the world
     */
    public static LatLng getRandomCoordinates(double currentLat, double currentLng, int distance) {
        return new LatLng(getRandomFloat(currentLat, distance),
                                    getRandomFloat(currentLng, distance));
    }

    /**
     * This method generate a collection of random coordinates far away to the location entered.
     *
     * @param currentLat Latitude of the location to generate coordinate nearby.
     * @param currentLng Longitude of the location to generate coordinate nearby.
     * @param quantity How many coordinates (Points) need.
     * @return LatLng[] array.
     */
    public static LatLng[] getVariousFarAwayCoordinates(double currentLat, double currentLng, int quantity) {
        LatLng[] latLngCoordinatesList = new LatLng[quantity];
        for (int i = 0; i < quantity; i++) {
            latLngCoordinatesList[i] = getRandomCoordinates(currentLat, currentLng, cityQuadrantDistance);
        }
        return latLngCoordinatesList;
    }

    /**
     * This method generate a collection of random coordinates nearby to the location entered.
     *
     * @param currentLat Latitude of the location to generate coordinate nearby.
     * @param currentLng Longitude of the location to generate coordinate nearby.
     * @param quantity How many coordinates (Points) need.
     * @return LatLng[] array.
     */
    public static LatLng[] getVariousCloserCoordinates(double currentLat, double currentLng, int quantity) {
        LatLng[] latLngCoordinatesList = new LatLng[quantity];
        for (int i = 0; i < quantity; i++) {
            latLngCoordinatesList[i] = getRandomCoordinates(currentLat, currentLng, neighborhoodQuadrantDistance);
        }
        return latLngCoordinatesList;
    }

    /**
     * This method generate an random float based on one coordinate axis (Longitude X or Latitude Y)
     * Uses the distance to generate the float number between this range.
     *
     * @param coordinateXorY Latitude or Longitude
     * @param distance How far away can the new latitude or longitude be
     * @return an double number with the decimal information of the new latitude or longitude.
     */
    private static double getRandomFloat(double coordinateXorY, int distance) {
        Random rand = new Random();
        int decimalPart = rand.nextInt(distance) + 1;

        double decimalConverted = decimalPart / 1e6;
        boolean isPositive = rand.nextBoolean();

        if (isPositive) return coordinateXorY + decimalConverted;
        else return coordinateXorY - decimalConverted;
    }
}
