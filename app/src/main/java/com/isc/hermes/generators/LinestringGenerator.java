package com.isc.hermes.generators;

import java.util.List;

/**
 * This method has the responsibility to generate linestring coordinates data.
 */
public class LinestringGenerator extends CoordinateGen {

    private PointGenerator pointGenerator;

    /**
     * This is the constructor method to initialize the point generator to generate linestring.
     */
    public LinestringGenerator() {
        this.pointGenerator = new PointGenerator();
    }

    /**
     * This method generate a valid lineString coordinates.
     *
     * @param amountPoints that has the lineString.
     * @return lineString coordinates generated.
     */
    public List<Double[]> getLinestring(
            Double[] referencePoint,
            Radium radium,
            int amountPoints
    ) {
        if (isValidReferencePoint(referencePoint) && amountPoints > 2) {
            resetGeneratedCoordinates();
            coordinates.add(referencePoint);

            for (int i = 1; i < amountPoints; i++) {
                coordinate = pointGenerator.getNearPoint(referencePoint, radium);
                coordinates.add(coordinate);
            }
        }

        return coordinates;
    }

}
