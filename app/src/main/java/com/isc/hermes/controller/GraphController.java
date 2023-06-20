package com.isc.hermes.controller;

import com.isc.hermes.model.graph.Graph;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphController {
    private static final String API_KEY = "sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";
    private static final String API_BASE_URL = "https://api.mapbox.com";

    public static void main(String[] args) throws JSONException {
        double latitude = 40.7128;
        double longitude = -74.0060;
        double latitude2 = 40.6328;
        double longitude2 = -74.1060;
        int radius = 500;
        String intersectionsUrl = buildIntersectionsUrl(latitude, longitude,latitude2, longitude2, radius);
        String intersectionsJson = sendHttpRequest(intersectionsUrl);
        buildGraphFromIntersections(intersectionsJson);

        //Graph graph = buildGraphFromIntersections(intersectionsJson);

    }

    public static String buildIntersectionsUrl(double latitude, double longitude, double latitude2, double longitude2, int radius) {
        //https://api.mapbox.com/geocoding/v5/mapbox.places-permanent/{longitude1},{latitude1};{longitude2},{latitude2}/intersections.json?access_token=TU_ACCESS_TOKEN&radius=500&limit=10&language=es&types=intersection,street
        return "https://api.mapbox.com/geocoding/v5/mapbox.places/" +
                longitude + "," + latitude + ".json?types=intersection&" +
                "bbox=" + longitude + "," + latitude + ";" + longitude2 + "," + latitude2 +
                "&access_token=" + API_KEY;
        /*return "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude + "," + latitude + ".json?access_token=" + API_KEY;*/
        /*return "https://api.mapbox.com/directions/v5/mapbox/driving/" + longitude + "," + latitude + ";" + longitude2 + ","
                + latitude2 + "?access_token=" + API_KEY + "&geometries=geojson&overview=full&steps=true";*/
        /*return API_BASE_URL + "/v8/mapbox.intersections/" + longitude + "," + latitude +
                "?radius=" + radius + "&access_token=" + API_KEY;*/
    }

    public static String sendHttpRequest(String url) {
        /*String url = "https://api.mapbox.com/v8/mapbox.intersections/-74.006,40.7128?radius=500&access_token=sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";
        /*HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();

            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line;
            StringBuilder responseContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            System.out.println(responseContent.toString());

            reader.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println(url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful()) {
                System.out.print("Test .............");
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void buildGraphFromIntersections(String intersectionsJson) throws JSONException {
        Graph graph = new Graph();
        System.out.println(intersectionsJson);

        if (intersectionsJson != null) {
            JSONObject json = new JSONObject(intersectionsJson);
            JSONArray intersections = json.getJSONArray("intersections");

            for (int i = 0; i < intersections.length(); i++) {
                JSONObject intersection = intersections.getJSONObject(i);
                JSONArray location = intersection.getJSONArray("location");

                double latitude = location.getDouble(1);
                double longitude = location.getDouble(0);

                NodeTest node = new NodeTest(latitude, longitude);
                System.out.println(node.getLatitude() + ", " + node.getLongitude());

                //graph.addNode(node);

            }

        }

        //return graph;
    }
}

class NodeTest {
    private double latitude;
    private double longitude;

    public NodeTest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Implementa getters y setters si es necesario
    // ...
}