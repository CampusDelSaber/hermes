package com.isc.hermes.model.incidents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the types of existing incidents
 */
public enum IncidentType {
    TRAFFIC_INCIDENT("Traffic Incident", GeometryType.LINESTRING), SOCIAL_INCIDENT("Social Incident", GeometryType.POINT),
    DANGER_ZONE("Danger Zone", GeometryType.POLYGON), NATURAL_DISASTER("Natural_Disaster", GeometryType.POLYGON);
    private String name;
    private GeometryType geometryType;

    /**
     * Builder, enter the values necessary to better identify the type of incident.
     *
     * @param name String name, represent the name of incident
     * @param geometryType GeometryType, represent the geometry type that represent the incident.
     */
    IncidentType(String name, GeometryType geometryType) {
        this.name = name;
        this.geometryType = geometryType;
    }

    /**
     * Return the name of incident type.
     *
     * @return String type, name incident.
     */
    public String getName(){
        return name;
    }


    /**
     * Return the geometry type.
     *
     * @return GeometryType type.
     */
    public GeometryType getGeometryType(){
        return geometryType;
    }

    /**
     * Returns a random incident type depending on the type of geometry passed to it.
     * <p>
     *  An incident type is obtained that matches the type of geometry passed.
     * </p>
     *
     * @param geometryType GeometryType to analyze in the generation of incident type.
     * @return IncidentType, incident got.
     */
    public static IncidentType getRandomIncidentByGeometry(GeometryType geometryType){
        List<IncidentType> incidentList = new ArrayList<>(List.of(TRAFFIC_INCIDENT, SOCIAL_INCIDENT, DANGER_ZONE, NATURAL_DISASTER));
        Random random = new Random();
        IncidentType incidentReturned = null;
        int numRandom;
        do{
            numRandom = random.nextInt(3 - 0 + 1) + 0;
            incidentReturned = incidentList.get(numRandom);
        }while (!incidentReturned.geometryType.equals(geometryType));
        return incidentReturned;
    }
}
