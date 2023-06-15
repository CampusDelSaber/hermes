package com.isc.hermes.model.Utils;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrafficUtils {

    private String locationName;
    private String destination;
    private double estimationTime;

    private Map<String, Integer> unitMap;
    private static TrafficUtils instance;

    public TrafficUtils() {
        this.unitMap = new HashMap<>();
        unitMap.put("minute", Calendar.MINUTE);
        unitMap.put("minutes", Calendar.MINUTE);
        unitMap.put("hour", Calendar.HOUR);
        unitMap.put("hours", Calendar.HOUR);
    }

    /**
     * This method generates a new ObjectId as a hexadecimal string.
     *
     * @return The generated ObjectId as a hexadecimal string.
     */
    public String generateObjectId() {
        ObjectId objectId = new ObjectId();
        return objectId.toHexString();
    }
    /**
     * This method adds a specified time duration to the current date and returns it as a formatted string.
     *
     * @param timeDuration The time duration to add in the format "X unit" (e.g., "2 hours", "1 day").
     * @return The modified date as a formatted string.
     */
    public String addTimeToCurrentDate(String timeDuration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String[] parts = timeDuration.split(" ");
        int amount = Integer.parseInt(parts[0]);
        String unit = parts[1].toLowerCase();

        Integer unitValue = unitMap.get(unit);
        if (unitValue != null) {
            calendar.add(unitValue, amount);
        }

        Date modifiedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(modifiedDate);
    }

    /**
     * This method generates the current date and time as a formatted string.
     *
     * @return The current date and time as a formatted string.
     */
    public String generateCurrentDateCreated() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(currentDate);
    }
    /**
     * Retrieves the instance of the IncidentsUtils class.
     *
     * @return The instance of the IncidentsUtils class.
     */
    public static TrafficUtils getInstance() {
        if (instance == null) instance = new TrafficUtils();
        return instance;
    }
}

