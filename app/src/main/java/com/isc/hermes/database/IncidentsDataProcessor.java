package com.isc.hermes.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

public class IncidentsDataProcessor {
    private ApiHandler apiHandler;
    private final String INCIDENTS_COLLECTION_NAME = "incidents";
    private ApiResponseParser responseParser;

    public IncidentsDataProcessor(){
        apiHandler = new ApiHandler();
        responseParser = ApiResponseParser.getInstance();
    }

    public JSONArray getAllIncidents() throws ExecutionException, InterruptedException {
        Future<String> future = apiHandler.getFutureCollectionGetString(INCIDENTS_COLLECTION_NAME);
        String futureResponse = future.get();
        JSONArray incidentsArray = responseParser.getJSONArrayOnResult(futureResponse);
        handleApiResponseExample(incidentsArray);
        return incidentsArray;
    }

    public void handleApiResponseExample(JSONArray incidentsArray) {
        try {
            for (int i = 0; i < incidentsArray.length(); i++) {
                JSONObject incidentObj = incidentsArray.getJSONObject(i);
                String id = incidentObj.getString("_id");
                String type = incidentObj.getString("type");
                String reason = incidentObj.getString("reason");
                String dateCreated = incidentObj.getString("dateCreated");

                // Process the incident data as needed, example:
                Timber.tag("Incident").d("ID: %s", id);
                Timber.tag("Incident").d("Type: %s", type);
                Timber.tag("Incident").d("Reason: %s", reason);
                Timber.tag("Incident").d("Date Created: %s", dateCreated);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

