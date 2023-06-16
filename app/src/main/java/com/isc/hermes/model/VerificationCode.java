package com.isc.hermes.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.utils.CreateVerificationCode;
import com.isc.hermes.utils.ValidationPeriod;

import java.util.Date;

public class VerificationCode {

    private String id, email, verificationCode;
    private Boolean isValid;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public VerificationCode(String id, String userEmail) {
        this.id = id;
        this.email = userEmail;
        this.verificationCode = new CreateVerificationCode().generateVerificationCode();
        this.isValid = true;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
