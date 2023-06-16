package com.isc.hermes.generators;

import com.isc.hermes.model.incidents.*;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This class handles incident generators to randomly generate incidents.
 */
public class IncidentGeneratorManage {
    private IGeometryGenerator[] generators;

    /**
     * Constructor, initializes the generator types in an array.
     */
    public IncidentGeneratorManage(){
        generators = new IGeometryGenerator[3];
        generators[0] = new PointGenerator();
        generators[1] = new PolygonGenerator();
        generators[2] = new LinestringGenerator();
    }

    /**
     * Generates a list of incidents randomly based on the incident generators you have.
     *
     * @param radium Radius in which the incidents were generated.
     * @param referencePoint Reference coordinate that is taken to generate incidents in a range based on this point.
     * @param amount Number of points to be generated.
     * @return List of incidents generated.
     */
    public ArrayList<Incident> generateRandomIncidents(Radium radium, Double[] referencePoint, int amount){
        Random random = new Random();
        int randomPosition;
        IGeometryGenerator generatorSelected;
        List<Double[]> pointsGenerated;
        int numberPoints;
        ArrayList<Incident> incidentsGenerated = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            numberPoints = 1;
            randomPosition = random.nextInt((generators.length-1) - 0 + 1) + 0;
            generatorSelected = generators[randomPosition];
            if(generatorSelected.getTypeGeometry().equals(GeometryType.LINE_STRING)){
                numberPoints = random.nextInt(5 - 2 + 1) + 2;
            }
            pointsGenerated = generatorSelected.generate(referencePoint, radium, numberPoints);
            Incident incident = getIncident(generatorSelected, pointsGenerated);
            incidentsGenerated.add(incident);
        }
        return incidentsGenerated;
    }

    /**
     * Generates an incident type object based on a coordinate and the type of generator used to build it.
     *
     * @param generator IGeometryGenerator type, Information from an incident generator is used.
     * @param coordinates Coordinate representing the incidents.
     * @return Incident object.
     */
    private Incident getIncident(IGeometryGenerator generator, List<Double[]> coordinates){
        IncidentType incidentType = IncidentType.getRandomIncidentByGeometry(generator.getTypeGeometry());
        Geometry geometry = new Geometry(generator.getTypeGeometry().getName(), coordinates);
        return  new Incident(new ObjectId(), incidentType.getName(), "Desc", new Date(), new Date(), geometry);
    }
}
