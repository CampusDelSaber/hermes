package com.isc.hermes.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.Radium;
import com.isc.hermes.model.incidents.IncidentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private Random random;
    private List<Integer> generatedIntegers;
    private List<List<Double[]>> generatedCoordinates;
    private final List<IncidentType> INCIDENT_TYPES = (List.of(
            IncidentType.SOCIAL_INCIDENT, IncidentType.DANGER_ZONE, IncidentType.NATURAL_DISASTER));
    private final List<Radium> RADII = List.of(Radium.TEN_METERS, Radium.TWENTY_FIVE_METERS,
            Radium.FIFTY_METERS, Radium.ONE_HUNDRED_METERS);

    public RandomGenerator() {
        this.random = new Random();
        this.generatedCoordinates = new ArrayList<>();
        this.generatedIntegers = new ArrayList<>();
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
        long randomMillis;

        do {
            randomMillis = startMillis + (long) (random.nextDouble() * (endMillis - startMillis));
        } while (randomMillis == startMillis);

        return new Date(randomMillis);
    }

    /**
     * This method generate a random reason using the reason list of a incident type,
     *
     * @param incidentType to get the reason type list.
     * @return random reason of the list of reason.
     */
    public String getReasonRandomly(IncidentType incidentType) {
        int randomIndex = getIntegerRandomly(0, incidentType.getReasons().size());
        return incidentType.getReasons().get(randomIndex);
    }

    /**
     * This method generate a random incident type.
     *
     * @return random incident type.
     */
    public IncidentType getIncidentTypeRandomly() {
        int randomIndex = getIntegerRandomly(0, INCIDENT_TYPES.size());
        return INCIDENT_TYPES.get(randomIndex);
    }

    /**
     * This method generate a random radium.
     *
     * @return random radium.
     */
    public Radium getRadiumRandomly() {
        return RADII.get(getIntegerRandomly(0, RADII.size()));
    }

    /**
     * This method generate a random integer using a range.
     *
     * @param min is the min value of the range.
     * @param max is the max value of the range.
     * @return random integer generated.
     */
    public int getIntegerRandomly(int min, int max) {
        int range = max - min;
        if (generatedIntegers.size() >= range) {
            generatedIntegers.clear();
        }
        int randomInt;
        do {
            randomInt = random.nextInt(max - min) + min;
        } while (generatedIntegers.contains(randomInt));
        generatedIntegers.add(randomInt);
        return randomInt;
    }

}
