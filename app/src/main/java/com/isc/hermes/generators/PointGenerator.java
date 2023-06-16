package com.isc.hermes.generators;

import java.util.List;

/**
 * This method has the responsibility to generate point coordinates data.
 */
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

    /**
     * This method reset the value of attempts amount.
     */
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
        Double[] coordinate = new Double[2];
        coordinate[0] = referencePoint[0] + distance * Math.cos(randomAngle);
        coordinate[1] = referencePoint[1] + distance * Math.sin(randomAngle);
        return coordinate;
    }

    /**
     * This method generate a multi points coordinates list on a limit area.
     *
     * @param referencePoint to get the main point of limit area.
     * @param radium to get points of limit area.
     * @param amountPoints are the amount the points to found.
     * @return multi points coordinates.
     */
    public List<Double[]> getPointCoordinates(
            Double[] referencePoint,
            Radium radium,
            int amountPoints
    ) {
        coordinates.clear();
        if (isValidReferencePoint(referencePoint)) {
            for (int i = 0; i < amountPoints; i++) {
                coordinates.add(getNearPoint(referencePoint, radium));
            }
        }

        return coordinates;
    }

}
