package com.isc.hermes.generators;

import java.util.List;

public class LinestringGenerator extends CoordinateGen {

    private PointGenerator pointGenerator;

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
        if (referencePoint != null && amountPoints > 1) {
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
