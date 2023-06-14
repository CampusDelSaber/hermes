package com.isc.hermes.database;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public  String createRandomIncidentId() {
        List<Character> characters = new ArrayList<>();
        Random random = new Random();
        for (char c = '0'; c <= '9'; c++) {
            characters.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
        }
        StringBuilder final_id = new StringBuilder();
        int length = 15;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.size());
            char randomCharacter = characters.get(randomIndex);
            final_id.append(randomCharacter);
        }
        return final_id.toString();
    }
}
