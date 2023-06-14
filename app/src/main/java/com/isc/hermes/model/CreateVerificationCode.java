package com.isc.hermes.model;

import java.util.Random;

public class CreateVerificationCode {

    public String generateVerificationCode() {
        String code = "";

        while (code.length() < 6) {
            code += getDigits();
        }

        return code;
    }

    private int getDigits() {
        Random random = new Random();
        int minimum = 0;
        int maximum = 9;
        return random.nextInt(maximum - minimum + 1) + minimum;
    }
}