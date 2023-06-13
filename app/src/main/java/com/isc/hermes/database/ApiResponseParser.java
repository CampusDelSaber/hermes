package com.isc.hermes.database;

import org.json.JSONArray;
import org.json.JSONException;

public class ApiResponseParser {
    private static ApiResponseParser instance;

    public JSONArray getJSONArrayOnResult(String result) {
        try {
            return new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static ApiResponseParser getInstance() {
        if (instance == null) instance = new ApiResponseParser();
        return instance;
    }
}
