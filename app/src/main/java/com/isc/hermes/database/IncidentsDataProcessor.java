package com.isc.hermes.database;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import timber.log.Timber;

/**
 * Class responsible for processing incidents data obtained from the API.
 */
public class IncidentsDataProcessor {
    private ApiHandler apiHandler;
    private final String INCIDENTS_COLLECTION_NAME = "incidents";
    private ApiResponseParser responseParser;
    private static IncidentsDataProcessor instance;

    /**
     * Constructs a new instance of IncidentsDataProcessor.
     */
    public IncidentsDataProcessor() {
        apiHandler = ApiHandler.getInstance();
        responseParser = ApiResponseParser.getInstance();
    }

    /**
     * Retrieves all incidents from the API.
     *
     * @return The JSONArray containing all incidents.
     * @throws ExecutionException   If an error occurs during execution.
     * @throws InterruptedException If the operation is interrupted.
     */
    public JSONArray getAllIncidents() throws ExecutionException, InterruptedException {
        Future<String> future = apiHandler.getFutureCollectionString(INCIDENTS_COLLECTION_NAME);
        String futureResponse = future.get();
        JSONArray incidentsArray = responseParser.getJSONArrayOnResult(futureResponse);
        handleApiResponseExample(incidentsArray);
        return incidentsArray;
    }

    /**
     * This method gets the near incidents from the API
     * @param latLng the coordinates to be able where to get the incidents
     * @param radius the radius of the incidents near to that coordinates
     * @return the incidents JSON Array response
     */
    public JSONArray getNearIncidents(LatLng latLng, double radius) {
        try{
            Future<String> future =
                    apiHandler.getFutureCollectionString(buildApiUrlNearIncidents(latLng, radius));
            String futureResponse = future.get();
            return responseParser.getJSONArrayOnResult(futureResponse);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * This method will build the url to make the read operation to the API of near incidents
     * @param latLng the coordinates to be able where to get the incidents
     * @param radius the radius of the incidents near to that coordinates
     * @return the API url to get the near incidents
     */
    private String buildApiUrlNearIncidents(LatLng latLng, double radius) {
        String latitude = Double.toString(latLng.getLatitude());
        String longitude = Double.toString(latLng.getLongitude());
        String radiusString = Double.toString(radius / 100.0);

        return String.format(
                "%s?longitude=%s&latitude=%s&radius=%s",
                INCIDENTS_COLLECTION_NAME, longitude, latitude, radiusString
        );
    }

    /**
     * Handles the API response and processes the incidents data.
     *
     * @param incidentsArray The JSONArray containing the incidents data.
     */
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

    /**
     * Retrieves the singleton instance of IncidentsDataProcessor.
     *
     * @return The singleton instance of IncidentsDataProcessor.
     */
    public static IncidentsDataProcessor getInstance() {
        if (instance == null) instance = new IncidentsDataProcessor();
        return instance;
    }

    public ApiHandler getApiHandler() {
        return apiHandler;
    }

    public void setApiHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    public void setResponseParser(ApiResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public ApiResponseParser getResponseParser() {
        return responseParser;
    }
}
