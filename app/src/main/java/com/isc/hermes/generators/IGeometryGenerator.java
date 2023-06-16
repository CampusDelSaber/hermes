package com.isc.hermes.generators;

import com.isc.hermes.model.incidents.GeometryType;

import java.util.List;

/**
 * This interface abstracts the features of a generator.
 */
public interface IGeometryGenerator {
    /**
     * This method generates points that form geoemetric objects.
     *
     * @param referencePoint to get the main point reference of limited range.
     * @param radium to get the radium using the reference point.
     * @param amount It is used depending on the type of implementation and type of figure to be built.
     */
    List<Double[]> generate(Double[] referencePoint, Radium radium, int amount);

    /**
     * This method returns the type of geometric object that is generated.
     *
     * @return TypeGeometry type.
     */
    GeometryType getTypeGeometry();
}
