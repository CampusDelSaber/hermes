package com.isc.hermes.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class responsible for handling API requests and retrieving data from the API.
 */
public class ApiRequestHandler {
    private static ApiRequestHandler instance;

    /**
     * Retrieves data from the API using the provided API URL.
     *
     * @param apiUrl The URL of the API endpoint.
     * @return The data retrieved from the API as a string.
     */
    public String getDataFromApi(String apiUrl) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    /**
     * Retrieves the singleton instance of ApiRequestHandler.
     *
     * @return The singleton instance of ApiRequestHandler.
     */
    public static ApiRequestHandler getInstance() {
        if (instance == null) instance = new ApiRequestHandler();
        return instance;
    }
}
