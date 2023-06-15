package com.isc.hermes.model.incidents;

import com.isc.hermes.generators.TypeGeometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum TypeIncident {
    TRAFFIC_INCIDENT("Traffic Incident", TypeGeometry.LINESTRING), SOCIAL_INCIDENT("Social Incident", TypeGeometry.POINT),
    DANGER_ZONE("Danger Zone", TypeGeometry.POLYGON), NATURAL_DISASTER("Natural_Disaster", TypeGeometry.POLYGON);
    private String name;
    private TypeGeometry typeGeometry;
    TypeIncident(String name, TypeGeometry typeGeometry) {
        this.name = name;
        this.typeGeometry = typeGeometry;
    }

    public String getName(){
        return name;
    }

    public TypeGeometry getTypeGeometry(){
        return typeGeometry;
    }

    public static TypeIncident getRandomIncidentByGeometry(TypeGeometry typeGeometry){
        List<TypeIncident> incidentList = new ArrayList<>(List.of(TRAFFIC_INCIDENT, SOCIAL_INCIDENT, DANGER_ZONE, NATURAL_DISASTER));
        Random random = new Random();
        TypeIncident incidentReturned = null;
        int numRandom;
        do{
            numRandom = random.nextInt(3 - 0 + 1) + 0;
            incidentReturned = incidentList.get(numRandom);
        }while (!incidentReturned.typeGeometry.equals(typeGeometry));
        return incidentReturned;
    }
}
