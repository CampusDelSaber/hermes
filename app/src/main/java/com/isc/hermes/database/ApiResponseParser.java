package com.isc.hermes.database;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Class responsible for parsing API responses.
 */
public class ApiResponseParser {
    private static ApiResponseParser instance;

    /**
     * Retrieves a JSONArray from the API response.
     *
     * @param result The API response as a string.
     * @return The JSONArray parsed from the response.
     */
    public JSONArray getJSONArrayOnResult(String result) {
        try {
            return new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    /**
     * Retrieves the singleton instance of ApiResponseParser.
     *
     * @return The singleton instance of ApiResponseParser.
     */
    public static ApiResponseParser getInstance() {
        if (instance == null) instance = new ApiResponseParser();
        return instance;
    }
}
