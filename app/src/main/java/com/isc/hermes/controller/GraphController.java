package com.isc.hermes.controller;

import android.annotation.SuppressLint;

import com.isc.hermes.model.graph.Graph;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GraphController {
    public static void getIntersections() {
        double latitud = -17.429321;
        double longitud = -66.159709;
        int radio = 500;

        try {
            @SuppressLint("DefaultLocale") String request = String.format(
                    "[out:json];way(around:%d,%f,%f)[highway~\"^(primary|secondary|tertiary|residential|unclassified)$\"];node(w);out center;",
                    radio, latitud, longitud
            );

            String codedRequest = URLEncoder.encode(request, "UTF-8");

            URL url = new URL("https://overpass-api.de/api/interpreter?data=" + codedRequest);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();

            System.out.println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buildGraphFromIntersections(String intersectionsJson) throws JSONException {
        Graph graph = new Graph();


        if (intersectionsJson != null) {
            JSONObject json = new JSONObject(intersectionsJson);
            JSONArray intersections = json.getJSONArray("elements");

            System.out.println();
            System.out.println(intersections);

            for (int i = 0; i < intersections.length(); i++) {
                System.out.println(intersections.get(i));
                /*JSONObject intersection = intersections.getJSONObject(i);
                JSONArray location = intersection.getJSONArray("routable_points");
                JSONObject location2 = intersection.getJSONObject("coordinates");

                double latitude = location.getDouble(1);
                double longitude = location.getDouble(0);

                NodeTest node = new NodeTest(latitude, longitude);
                System.out.println(node.getLatitude() + ", " + node.getLongitude());*/

                //graph.addNode(node);

            }

        }

        //return graph;
    }
}