package com.isc.hermes.model.incidents;

import com.isc.hermes.generators.CoordinatesGenerable;
import com.isc.hermes.generators.LinestringGenerator;
import com.isc.hermes.generators.PointGenerator;
import com.isc.hermes.generators.PolygonGenerator;

import java.util.List;

/**
 * This class represents the types of existing incidents
 */
public enum IncidentType {

    TRAFFIC_INCIDENT (
            "Traffic",
            List.of("Vehicle crashes", "Road construction", "Peak hours", "Heavy truck traffic"),
            new LinestringGenerator()
    ), SOCIAL_INCIDENT (
            "Social Incident",
            List.of("Protests", "Festivals", "Sports races", "Rallies"),
            new PointGenerator()
    ), DANGER_ZONE (
            "Danger Zone",
            List.of("Protesters fight", "Military control"),
            new PolygonGenerator()
    ), NATURAL_DISASTER(
            "Natural Disaster",
            List.of("Flooding", "Earthquakes", "Wildfires", "Severe storms"),
            new PolygonGenerator()
    );

    private final String type;
    private final List<String> reasons;
    private final CoordinatesGenerable generator;

    /**
     * Builder, enter the values necessary to better identify the type of incident.
     *
     * @param name      String name, represent the name of incident
     * @param generator GeometryType, represent the geometry type that represent the incident.
     */
    IncidentType(String name, List<String> reasons, CoordinatesGenerable generator) {
        this.type = name;
        this.reasons = reasons;
        this.generator = generator;
    }

    /**
     * Return the name of incident type.
     *
     * @return String type, name incident.
     */
    public String getType() {
        return type;
    }


    /**
     * Return the geometry type.
     *
     * @return GeometryType type.
     */
    public CoordinatesGenerable getGenerator() {
        return generator;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
