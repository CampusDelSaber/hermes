package com.isc.hermes.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.Radium;
import com.isc.hermes.model.incidents.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class handles incident generators to randomly generate incidents.
 */
public class IncidentGenerator {

    private RandomGenerator randomGenerator;
    private LocalDateTime timeout;
    private PointGenerator pointGenerator;
    private IncidentType incidentType;
    private List<Incident> incidents;

    /**
     * Constructor, initializes the generator types in an array.
     */
    public IncidentGenerator() {
        this.randomGenerator = new RandomGenerator();
        this.pointGenerator = new PointGenerator();
        this.incidents = new ArrayList<>();
    }

    /**
     * Generates a list of incidents randomly based on the incident generators you have.
     *
     * @param radium         Radius in which the incidents were generated.
     * @param referencePoint Reference coordinate that is taken to generate incidents in a range based on this point.
     * @param amount         Number of points to be generated.
     * @return List of incidents generated.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Incident> getIncidentsRandomly(Double[] referencePoint, Radium radium, int amount) {
        incidents.clear();
        if (withoutTimeout()) {
            pointGenerator
                    .getMultiPoint(referencePoint, radium, amount)
                    .forEach(reference -> {
                        incidentType = randomGenerator.getIncidentTypeRandomly();
                        incidents.add(buildIncident(incidentType, reference));
                    });
            timeout = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
        }

        return incidents;
    }

    /**
     * This method build a random incident using a context like their parameters.
     *
     * @param incidentType   is the incident type of the incident that will created.
     * @param referencePoint is the reference coordinate to generate the incident coordinates.
     * @return random incident built.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Incident buildIncident(IncidentType incidentType, Double[] referencePoint) {
        List<Double[]> incidentCoordinates = incidentType.getGenerator().generate(referencePoint,
                randomGenerator.getRadiumRandomly(), randomGenerator.getIntegerRandomly(3, 10));
        Geometry geometry = new Geometry(
                incidentType.getGenerator().getTypeGeometry().getName(), incidentCoordinates);

        return new Incident(incidentType.getType(),
                randomGenerator.getReasonRandomly(incidentType), new Date(),
                randomGenerator.generateDeathDateRandomly(), geometry);
    }

    /**
     * Checks if the incident generation process can be performed without a timeout.
     *
     * @return if there is no timeout set or the seconds between timeouts is
     * less than or equal to zero, otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean withoutTimeout() {
        return timeout == null || getSecondsBetweenTimeout() <= 0;
    }

    /**
     * This method calculate the time between the current with the timeout.
     *
     * @return difference between the current with the timeout in minutes.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getMinutesBetweenTimeout() {
        return timeout == null ? 0 : ChronoUnit.MINUTES.between(LocalDateTime.now(), timeout);
    }

    /**
     * This method calculate the time between the current with the timeout.
     *
     * @return difference between the current with the timeout in seconds.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getSecondsBetweenTimeout() {
        return timeout == null ? 0 : ChronoUnit.SECONDS.between(LocalDateTime.now(), timeout);
    }
}
