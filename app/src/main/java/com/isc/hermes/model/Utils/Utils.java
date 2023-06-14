package com.isc.hermes.model.Utils;

import com.isc.hermes.model.User;
import com.isc.hermes.model.user.UserRoles;

import java.util.Arrays;

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

    public static String transformToCamelcase(String word){
        StringBuilder builder = new StringBuilder();
        char[] letters = word.strip().toCharArray();

        builder.append(Character.toString(letters[0]).toUpperCase());
        for (int index = 1; index < letters.length; index++) {
            builder.append(Character.toString(letters[index]).toLowerCase());
        }

        return builder.toString();
    }

}
