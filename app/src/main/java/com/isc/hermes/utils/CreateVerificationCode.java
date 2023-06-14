package com.isc.hermes.utils;

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
        int minimum = 0;
        int maximum = 9;
        return new Random().nextInt(maximum - minimum + 1) + minimum;
    }
}