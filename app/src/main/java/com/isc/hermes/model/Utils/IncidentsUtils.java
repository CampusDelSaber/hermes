package com.isc.hermes.model.Utils;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Utility class for handling incidents-related operations.
 */
public class IncidentsUtils {
    /**
     * Generates a new ObjectId as a hexadecimal string.
     *
     * @return The generated ObjectId as a hexadecimal string.
     */
    public static String generateObjectId() {
        ObjectId objectId = new ObjectId();
        return objectId.toHexString();
    }
    /**
     * Adds a specified time duration to the current date and returns it as a formatted string.
     *
     * @param timeDuration The time duration to add in the format "X unit" (e.g., "2 hours", "1 day").
     * @return The modified date as a formatted string.
     */
    public static String addTimeToCurrentDate(String timeDuration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String[] parts = timeDuration.split(" ");
        int amount = Integer.parseInt(parts[0]);
        String unit = parts[1].toLowerCase();

        if (unit.equals("minute") || unit.equals("minutes")) {
            calendar.add(Calendar.MINUTE, amount);
        } else if (unit.equals("hour") || unit.equals("hours")) {
            calendar.add(Calendar.HOUR, amount);
        } else if (unit.equals("day") || unit.equals("days")) {
            calendar.add(Calendar.DAY_OF_YEAR, amount);
        } else if (unit.equals("month") || unit.equals("months")) {
            calendar.add(Calendar.MONTH, amount);
        }

        Date modifiedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(modifiedDate);
    }
    /**
     * Generates the current date and time as a formatted string.
     *
     * @return The current date and time as a formatted string.
     */
    public static String generateCurrentDateCreated() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(currentDate);
    }
}
