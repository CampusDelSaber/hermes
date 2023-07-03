package com.isc.hermes.requests.incidents;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class to obtain all polygons from database
 */
public class PolygonRequester {
    private final String NATURAL_DISASTER_API_URL =
            "https://api-rest-hermes.onrender.com/incidents?types=Danger%20Zone,Natural%20Disaster";
    private OkHttpClient client;
    private Request request;
    private List<List<List<Point>>> polygons;

    public PolygonRequester() {
        this.client = new OkHttpClient();
        this.polygons = new ArrayList<>();
        requestIncidents();
    }

    /**
     * Method to init requesting action from natural disaster url
     */
    private void requestIncidents() {
        this.request = new Request.Builder()
                .url(NATURAL_DISASTER_API_URL).build();
        initCallBack();
    }


    /**
     * Method to init the requesting from client to database
     */
    private void initCallBack() {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JsonArray jsonResponse = JsonParser.parseString(
                            response.body().string()).getAsJsonArray();
                    buildPolygons(jsonResponse);
                }
            }
        });
    }

    /**
     * Method to fill last polygons list from json array
     *
     * @param jsonResponse Is json containing all polygons
     */
    private void buildPolygons(JsonArray jsonResponse) {
        JsonArray coordinates;
        JsonObject incidentDocument, geometry;
        for (JsonElement incidentElement : jsonResponse) {
            incidentDocument = incidentElement.getAsJsonObject();
            geometry = incidentDocument.get("geometry").getAsJsonObject();
            coordinates = geometry.get("coordinates").getAsJsonArray();
            polygons.add(buildPolygon(coordinates));
        }
    }

    /**
     * Method to obtain a single polygon from a json array with its coordinates
     *
     * @param polygonPoints Is array with all polygon points
     * @return polygon coordinates
     */
    private List<List<Point>> buildPolygon(JsonArray polygonPoints) {
        List<Point> polygon = new ArrayList<>();
        polygonPoints.forEach(point -> {
            polygon.add(Point.fromLngLat(
                    point.getAsJsonArray().get(0).getAsDouble(),
                    point.getAsJsonArray().get(1).getAsDouble()));
        });

        return List.of(polygon);
    }

    /**
     * Method to return the final result to requesting from database
     *
     * @return All polygons with its coordinates located in database
     */
    public List<List<List<Point>>> getPolygons() {
        requestIncidents();
        return polygons;
    }
}
