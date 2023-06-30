package com.isc.hermes.utils.regex;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * This class is in charged to validate inputs.
 */

public class InputValidator {

    private static final int MAX_LENGTH_TEXT = 15;
    private static final String REGEX_SPECIAL_CHARACTERS = "^[a-zA-Z0-9 ]*$";
    private static final String REGEX_SPACES = ".*\\s{3,}.*";
    @SuppressLint("StaticFieldLeak")
    private static EditText inputText;

    /**
     *This method validates the input text.
     *
     * @param editText The EditText containing the input text.
     * @param textInput The input text to validate.
     * @return {@code true} if the input text is valid, {@code false} otherwise.
     */
    public static boolean validateInput(EditText editText,String textInput) {
        inputText = editText;
        String trimmedInput = textInput.trim();
        return isNotEmpty(trimmedInput)
                && !containsSpecialCharacters(trimmedInput)
                && !exceedsMaxLength(trimmedInput)
                && !containsExcessiveSpaces(trimmedInput);
    }

    /**
     * Checks if the input text is not empty.
     *
     * @param input The input text to check.
     * @return {@code true} if the input text is not empty, {@code false} otherwise.
     */
    private static boolean isNotEmpty(String input) {
        if (TextUtils.isEmpty(input)) {
            setErrorMessage("This field is required");
            return false;
        }
        return true;
    }

    /**
     * Checks if the input text contains special characters.
     *
     * @param input The input text to check.
     * @return {@code true} if the input text contains special characters, {@code false} otherwise.
     */
    private static boolean containsSpecialCharacters(String input) {
        if (!input.matches(REGEX_SPECIAL_CHARACTERS)) {
            setErrorMessage("Special characters not allowed");
            return true;
        }
        return false;
    }

    /**
     * Checks if the input text exceeds the maximum allowed length.
     *
     * @param input The input text to check.
     * @return {@code true} if the input text exceeds the maximum length, {@code false} otherwise.
     */
    private static boolean exceedsMaxLength(String input) {
        if (input.length() > MAX_LENGTH_TEXT) {
            setErrorMessage("Max 15 characters allowed");
            return true;
        }
        return false;
    }

    /**
     * Checks if the input text contains excessive spaces.
     *
     * @param input The input text to check.
     * @return {@code true} if the input text contains excessive spaces, {@code false} otherwise.
     */
    private static boolean containsExcessiveSpaces(String input) {
        if (input.matches(REGEX_SPACES)) {
            setErrorMessage("No more than 2 spaces are allowed");
            return true;
        }
        return false;
    }

    /**
     * Sets an error message for the input field and clears the text.
     *
     * @param errorMessage The error message to be displayed.
     */
    private static void setErrorMessage(String errorMessage) {
        inputText.setError(errorMessage);
        inputText.setText("");
    }
}
