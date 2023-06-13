package com.isc.hermes.model;

import java.util.Random;

public class CreatePass {

    public String generatePass() {
        String code = "";

        while (code.length() < 6) {
            code += getCharacter();
        }

        return code;
    }

    private char getCharacter() {
        char letter = ' ';
        Random random = new Random();
        int min = 48;
        int max = 122;
        boolean run = true;

        while (run) {
            int asciiCode = random.nextInt(max - min + 1) + min;

            letter = (char) asciiCode;

            if (!isSymbol(letter)) {
                run = false;
            }
        }

        return letter;
    }

    private boolean isSymbol(char character) {
        String regex = "[^a-zA-Z0-9]";
        return String.valueOf(character).matches(regex);
    }

}