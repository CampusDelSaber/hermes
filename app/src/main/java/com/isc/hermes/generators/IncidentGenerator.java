package com.isc.hermes.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.Radium;
import com.isc.hermes.model.incidents.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This class handles incident generators to randomly generate incidents.
 */
public class IncidentGenerator {

    private Random random;
    private PointGenerator pointGenerator;
    private IncidentType incidentType;
    private List<Incident> incidents;
    private final List<IncidentType> incidentTypes = (List.of(
            IncidentType.TRAFFIC_INCIDENT, IncidentType.SOCIAL_INCIDENT,
            IncidentType.DANGER_ZONE, IncidentType.NATURAL_DISASTER));
    private final List<Radium> radii = List.of(Radium.FIVE_METERS, Radium.TEN_METERS,
            Radium.TWENTY_FIVE_METERS, Radium.FIFTY_METERS);
    ;

    /**
     * Constructor, initializes the generator types in an array.
     */
    public IncidentGenerator() {
        this.random = new Random();
        this.pointGenerator = new PointGenerator();
        this.incidents = new ArrayList<>();
    }

    /**
     * This method generate a death date randomly.
     *
     * @return valid death date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Date generateDeathDateRandomly() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate fourteenDaysLater = tomorrow.plusDays(14);
        LocalDateTime startDateTime = tomorrow.atStartOfDay();
        LocalDateTime endDateTime = fourteenDaysLater.atStartOfDay();

        long startMillis = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long randomMillis = startMillis + (long) (random.nextDouble() * (endMillis - startMillis));

        return new Date(randomMillis);
    }

    /**
     * Generates a list of incidents randomly based on the incident generators you have.
     *
     * @param radium Radius in which the incidents were generated.
     * @param referencePoint Reference coordinate that is taken to generate incidents in a range based on this point.
     * @param amount Number of points to be generated.
     * @return List of incidents generated.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Incident> getIncidentsRandomly(Double[] referencePoint, Radium radium, int amount) {
        incidents.clear();
        pointGenerator
                .getMultiPoint(referencePoint, radium, amount)
                .forEach(reference -> {
                    incidentType = getIncidentTypeRandomly();
                    incidents.add(buildIncident(incidentType, referencePoint));
                });

        return incidents;
    }

    /**
     * This method build a random incident using a context like their parameters.
     *
     * @param incidentType is the incident type of the incident that will created.
     * @param referencePoint is the reference coordinate to generate the incident coordinates.
     * @return random incident built.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Incident buildIncident(IncidentType incidentType, Double[] referencePoint) {
        List<Double[]> incidentCoordinates = incidentType.getGenerator().generate(referencePoint,
                getRadiumRandomly(), getIntegerRandomly(3, 10));
        Geometry geometry = new Geometry(
                incidentType.getGenerator().getTypeGeometry().getName(), incidentCoordinates);

        return new Incident(incidentType.getType(),
                getReasonRandomly(incidentType), new Date(),
                generateDeathDateRandomly(), geometry);
    }

    /**
     * This method generate a random integer using a range.
     *
     * @param min is the min value of the range.
     * @param max is the max value of the range.
     * @return random integer generated.
     */
    private int getIntegerRandomly(int min, int max) {
        return random.nextInt((max - 1) - min + 1) + min;
    }

    /**
     * This method generate a random reason using the reason list of a incident type,
     *
     * @param incidentType to get the reason type list.
     * @return random reason of the list of reason.
     */
    private String getReasonRandomly(IncidentType incidentType) {
        int randomIndex = getIntegerRandomly(0, incidentType.getReasons().size());
        return incidentType.getReasons().get(randomIndex);
    }

    /**
     * This method generate a random incident type.
     *
     * @return random incident type.
     */
    public IncidentType getIncidentTypeRandomly() {
        int randomIndex = getIntegerRandomly(0, incidentTypes.size());
        return incidentTypes.get(randomIndex);
    }

    /**
     * This method generate a random radium.
     *
     * @return random radium.
     */
    public Radium getRadiumRandomly() {
        int randomIndex = getIntegerRandomly(0, radii.size());
        return radii.get(randomIndex);
    }
}
