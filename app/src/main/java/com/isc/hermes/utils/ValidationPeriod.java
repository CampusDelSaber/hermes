package com.isc.hermes.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class ValidationPeriod {

    private ZoneId zoneId;
    private LocalTime localTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ValidationPeriod() {
        zoneId = ZoneId.of("America/New_York");
        localTime = LocalTime.now(zoneId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Date createValidationPeriod() {
        Date validationPeriod = new Date();
        int minuteCurrent = localTime.getMinute();
        int validationTime = 30;

        int minuteFinal = minuteCurrent + validationTime;

        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();

        if (minuteFinal > 59) {
            minute = minuteFinal - 59;
            hour++;
        }

        validationPeriod.setHours(hour);
        validationPeriod.setMinutes(minute);
        validationPeriod.setSeconds(second);

        return validationPeriod;
    }

}
