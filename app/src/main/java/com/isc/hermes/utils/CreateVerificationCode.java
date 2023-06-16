package com.isc.hermes.utils;

import java.util.Random;

/**
 * The CreateVerificationCode class is responsible for generating a verification code.
 */
public class CreateVerificationCode {

    /**
     * Generates a random verification code consisting of six digits.
     *
     * @return The generated verification code.
     */
    public String generateVerificationCode() {
        String code = "";

        while (code.length() < 6) {
            code += getDigits();
        }

        return code;
    }

    /**
     * Generates a random digit between 0 and 9.
     *
     * @return The generated digit.
     */
    private int getDigits() {
        int minimum = 0;
        int maximum = 9;
        return new Random().nextInt(maximum - minimum + 1) + minimum;
    }
}
