package com.isc.hermes.generators;

import com.isc.hermes.model.incidents.Geometry;
import com.isc.hermes.model.incidents.Incident;
import com.isc.hermes.model.incidents.TypeIncident;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GeneratorManager {
    private IGeometryGenerator[] generators;

    public GeneratorManager(){
        generators = new IGeometryGenerator[3];
        generators[0] = new PointGenerator();
        generators[1] = new PolygonGenerator();
        generators[2] = new LinestringGenerator();
    }

    public ArrayList<Incident> generateRandomIncidents(Radium radium, Double[] referencePoint, int amount){
        Random random = new Random();
        int randomNumber;
        IGeometryGenerator generatorSelected;
        List<Double[]> pointsGenerated;
        int numberPoints;
        ArrayList<Incident> incidentsGenerated = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            numberPoints = 1;
            randomNumber = random.nextInt(generators.length - 0 + 1) + 0;
            generatorSelected = generators[randomNumber];
            if(generatorSelected.getTypeGeometry().equals(TypeGeometry.LINESTRING)){
                numberPoints = random.nextInt(5 - 2 + 1) + 2;
            }
            pointsGenerated = generatorSelected.generate(referencePoint, radium, numberPoints);
            Incident incident = getIncident(generatorSelected, pointsGenerated);
            incidentsGenerated.add(incident);
        }
        return incidentsGenerated;
    }

    private Incident getIncident(IGeometryGenerator generator, List<Double[]> coordinates){
        TypeIncident typeIncident = TypeIncident.getRandomIncidentByGeometry(generator.getTypeGeometry());
        Geometry geometry = new Geometry(generator.getTypeGeometry().getName(), coordinates);
        return  new Incident(new ObjectId(), typeIncident.getName(), "Desc", new Date(), new Date(), geometry);
    }
}
