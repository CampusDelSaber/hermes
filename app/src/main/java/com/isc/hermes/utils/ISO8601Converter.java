package com.isc.hermes.utils;

import android.os.Build;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ISO8601Converter {
    private static ISO8601Converter instance;

    public Date convertISO8601ToDate(String iso8601Date) {
        LocalDateTime localDateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.parse(iso8601Date, DateTimeFormatter.ISO_DATE_TIME);
        }
        Instant instant = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Date.from(instant);
        }
        return new Date();
    }

    public static ISO8601Converter getInstance() {
        if (instance == null) instance = new ISO8601Converter();
        return instance;
    }
}
