package com.isc.hermes.requests.overpass;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This class makes a request to the Overpass api to get the map paths.
 */
public class IntersectionRequest {
    /**
     * Method to get the intersection or nodes of the map, in a determinate area specifying the
     * midpoint and a radius.
     *
     * @param latitude Is the latitude of a midpoint.
     * @param longitude Is the longitude of a midpoint.
     * @param radius Is the area to obtain data.
     * @return A string in Json format.
     */
    public String getIntersections(double latitude, double longitude, int radius) {

        try {
            @SuppressLint("DefaultLocale") String request = String.format(
                    "[out:json];way(around:%d,%f,%f)[highway~" +
                    "\"^(primary|secondary|tertiary|residential|unclassified)$\"];node(w);out center;",
                    radius, latitude, longitude
            );

            String codedRequest = URLEncoder.encode(request, "UTF-8");

            URL url = new URL("https://overpass-api.de/api/interpreter?data=" + codedRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String intersection;
            while ((intersection = reader.readLine()) != null) {
                response.append(intersection);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
