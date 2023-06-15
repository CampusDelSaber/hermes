package com.isc.hermes.model.Utils;

/**
 * Utility class containing various helper methods.
 * This class provides common utility methods that can be used across the application.
 */
public class Utils {

    /**
     * Returns the first word in a string of words.
     *
     * @param words The string of words.
     * @return The first word in the string.
     */
    public static String getFirstWord(String words) {
        String[] wordsArray = words.split(" ");
        String expectedWord = "";

        if (wordsArray.length > 0) expectedWord = wordsArray[0];
        return expectedWord;
    }
}
