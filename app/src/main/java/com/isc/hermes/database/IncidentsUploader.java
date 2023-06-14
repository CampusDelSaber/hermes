package com.isc.hermes.database;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class IncidentsUploader {
    private final String API_URL = "https://api-rest-hermes.onrender.com/";

    public void uploadIncident(String incidentJsonString){
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            setConnectionHeaders(connection);
            sendJsonPayload(connection, incidentJsonString);
            getResponseCode(connection);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getResponseCode(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Incident uploaded successfully.");
        } else {
            System.out.println("Failed to upload incident. Response Code: " + responseCode);
        }
    }

    private void setConnectionHeaders(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
    }
    private void sendJsonPayload(HttpURLConnection connection, String incidentJsonString) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(incidentJsonString);
        outputStream.flush();
        outputStream.close();
    }
}
