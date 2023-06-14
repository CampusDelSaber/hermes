package com.isc.hermes.generators;

public class PointGenerator extends CoordinateGen {

    private int maxAttempts;

    /**
     * This method generate a point coordinate within a habitable zone.
     *
     * @return point coordinate.
     */
    public Double[] getStreetPoint() {
        resetAttempts();

        do {
            coordinate[0] = generateRandomLongitude();
            coordinate[1] = generateRandomLatitude();
            maxAttempts--;
        } while (!streetValidator.isPointStreet(coordinate[0], coordinate[1])
                && maxAttempts > 0);

        return coordinate;
    }

    private void resetAttempts() {
        maxAttempts = 10000;
    }

    /**
     * This method generate a valid point surrounded by another as a reference.
     *
     * @param referencePoint to found others points.
     * @param radium the size radium valid to found a coordinates.
     * @return near point.
     */
    public Double[] getNearPoint(Double[] referencePoint, Radium radium) {
        double randomAngle = getRandomAngle();
        double distance = getNearDistance(radium);
        coordinate[0] = referencePoint[0] + distance * Math.cos(randomAngle);
        coordinate[1] = referencePoint[1] + distance * Math.sin(randomAngle);
        return coordinate;
    }

}
