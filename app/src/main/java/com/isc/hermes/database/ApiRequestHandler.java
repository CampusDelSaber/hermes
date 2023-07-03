package com.isc.hermes.database;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
     * Sends a POST request to the specified API URL with the provided JSON data.
     *
     * @param apiUrl   The URL of the API to send the request to.
     * @param jsonData The JSON data to be sent in the request body.
     */
    public void postDataFromApi(String apiUrl, String jsonData) {
        try {
           URL url = new URL(apiUrl);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonData);
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Sends a POST request to the specified API URL with the provided JSON data.
     *
     * @param apiUrl   The URL of the API to send the request to.
     */
    public void postDataFromApi(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            int responseCode = connection.getResponseCode();
            System.out.println("CODE RESPONSE: " + responseCode);
            connection.disconnect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Updates data in the database through a PUT request to the specified API URL with the provided JSON data.
     *
     * @param apiUrl   The URL of the API to send the request to.
     * @param jsonData The JSON data to be sent in the request body.
     */
    public void updateDataInDatabase(String apiUrl, String jsonData) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonData);
            outputStream.flush();
            outputStream.close();
            connection.getResponseCode();
            connection.disconnect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Sends a DELETE request to the specified API URL with the provided ID.
     *
     * @param apiUrl the URL of the API to send the request to
     * @param id     the ID of the data to delete
     */
    public void deleteDataById(String apiUrl, String id) {
        try {
            URL url = new URL(apiUrl + "/" + id);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println("CODE: " + code);
            connection.disconnect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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

    /**
     * Sends a PUT request to the specified API URL with the provided JSON data.
     *
     * @param apiUrl  The URL of the API to send the request to.
     * @param jsonData The JSON data to include in the request body.
     */
    public void putDataFromApi(String apiUrl, String jsonData) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonData);
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            System.out.println("CODE RESPONSE: " + responseCode);
            connection.disconnect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
