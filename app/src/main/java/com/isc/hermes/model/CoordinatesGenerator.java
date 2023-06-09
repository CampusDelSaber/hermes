package com.isc.hermes.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Random;

public class CoordinatesGenerator {
    private static final int cityQuadrantDistance = 30000;
    private static final int streetQuadrantDistance = 2000;

    public static LatLng getRandomCoordinates(double currentLat, double currentLng, int distance) {
        return new LatLng(getRandomFloat(currentLat, distance),
                                    getRandomFloat(currentLng, distance));
    }

    public static LatLng[] getVariousCoordinates(double currentLat, double currentLng, int quantity) {
        LatLng[] latLngCoordinatesList = new LatLng[quantity];
        for (int i = 0; i < quantity; i++) {
            latLngCoordinatesList[i] = getRandomCoordinates(currentLat, currentLng, cityQuadrantDistance);
        }
        return latLngCoordinatesList;
    }

    public static LatLng[] getVariousCloserCoordinates(double currentLat, double currentLng, int quantity) {
        LatLng[] latLngCoordinatesList = new LatLng[quantity];
        for (int i = 0; i < quantity; i++) {
            latLngCoordinatesList[i] = getRandomCoordinates(currentLat, currentLng, streetQuadrantDistance);
        }
        return latLngCoordinatesList;
    }

    private static double getRandomFloat(double coordinateXorY, int distance) {
        Random rand = new Random();
        int decimalPart = rand.nextInt(distance) + 1;

        double decimalConverted = decimalPart / 1e6;
        boolean isPositive = rand.nextBoolean();

        if (isPositive) return coordinateXorY + decimalConverted;
        else return coordinateXorY - decimalConverted;
    }
}
