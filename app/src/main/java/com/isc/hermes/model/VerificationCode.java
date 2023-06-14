package com.isc.hermes.model;

import com.isc.hermes.utils.CreateVerificationCode;
import com.isc.hermes.utils.ValidationPeriod;

import java.util.Date;

public class VerificationCode {

    private String userEmail, verificationCode;
    private Date validationPeriod;

    public VerificationCode(String userEmail) {
        this.userEmail = userEmail;
        this.verificationCode = new CreateVerificationCode().generateVerificationCode();
        this.validationPeriod = new ValidationPeriod().createValidationPeriod();
    }

}
