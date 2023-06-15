package com.isc.hermes.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.utils.CreateVerificationCode;
import com.isc.hermes.utils.ValidationPeriod;

import java.util.Date;

public class VerificationCode {

    private String email, verificationCode;
    private Date deathDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public VerificationCode(String userEmail) {
        this.email = userEmail;
        this.verificationCode = new CreateVerificationCode().generateVerificationCode();
        this.deathDate = new ValidationPeriod().createValidationPeriod();
    }

}
