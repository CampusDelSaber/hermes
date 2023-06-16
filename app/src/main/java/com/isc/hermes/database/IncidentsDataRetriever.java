package com.isc.hermes.database;

import android.os.AsyncTask;

import com.isc.hermes.model.incidents.Incident;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncidentsDataRetriever {
    private static IncidentsDataRetriever instance;

    public interface IncidentsRetrievalListener {
        void onIncidentsRetrieved(List<Incident> incidents);
        void onIncidentsRetrievalFailed(Exception e);
    }

    public void retrieveIncidents(IncidentsRetrievalListener listener) {
        new IncidentsRetrievalTask(listener).execute();
    }

    private static class IncidentsRetrievalTask extends AsyncTask<Void, Void, List<Incident>> {
        private IncidentsRetrievalListener listener;
        private Exception exception;

        public IncidentsRetrievalTask(IncidentsRetrievalListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Incident> doInBackground(Void... params) {
            try {
                return performNetworkRequest();
            } catch (IOException | JSONException | ParseException e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Incident> incidents) {
            if (incidents != null) {
                listener.onIncidentsRetrieved(incidents);
            } else {
                listener.onIncidentsRetrievalFailed(exception);
            }
        }

        private List<Incident> performNetworkRequest() throws IOException, JSONException, ParseException {
            List<Incident> incidents = new ArrayList<>();

            URL url = new URL("https://api-rest-hermes.onrender.com/incidents");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("_id");
                    String type = jsonObject.getString("type");
                    String reason = jsonObject.getString("reason");
                    Date dateCreated = parseDate(jsonObject.getString("dateCreated"));
                    Date deathDate = parseDate(jsonObject.getString("deathDate"));
                    JSONObject geometry = jsonObject.getJSONObject("geometry");

                    Incident incident = new Incident(id, type, reason, dateCreated, deathDate, geometry);
                    incidents.add(incident);
                }
            }

            return incidents;
        }

        private Date parseDate(String dateString) throws ParseException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return dateFormat.parse(dateString);
        }
    }

    public static IncidentsDataRetriever getInstance() {
        if (instance == null) {
            instance = new IncidentsDataRetriever();
        }
        return instance;
    }
}
