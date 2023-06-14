package com.isc.hermes.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class ValidationPeriod {

    private ZoneId zoneId;
    private LocalTime localTime;
    private LocalDate localDate;
    private static final String TIME_ZONE = "Europe/London";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ValidationPeriod() {
        zoneId = ZoneId.of(TIME_ZONE);
        localTime = LocalTime.now(zoneId);
        localDate = LocalDate.now(zoneId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Date createValidationPeriod() {
        Date validationPeriod = new Date();
        int minuteCurrent = localTime.getMinute();
        int validationTime = 30;

        int minuteFinal = minuteCurrent + validationTime;

        int hour = localTime.getHour();
        int second = localTime.getSecond();
        int day = localDate.getDayOfMonth();
        System.out.println(day);
        int month = localDate.getMonthValue();
        int year = validationPeriod.getYear();

        if (minuteFinal >= 59) {
            minuteFinal = minuteFinal - 60;
            if (hour == 23) {
                hour = 0;
                if (day == 31 || day == 30 || day == 29 || day == 28) {
                    day = 1;
                    if (month == 12) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }
                } else {
                    day++;
                }
            } else {
                hour++;
            }
        }

        validationPeriod.setHours(hour);
        validationPeriod.setMinutes(minuteFinal);
        validationPeriod.setSeconds(second);
        validationPeriod.setYear(year);
        validationPeriod.setMonth(month);
        validationPeriod.setDate(day);

        System.out.println(validationPeriod);

        return validationPeriod;
    }

}
