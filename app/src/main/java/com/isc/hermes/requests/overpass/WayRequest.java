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
public class WayRequest {
    /**
     * Method to get the edges of the ways, in a determinate area specifying the midpoint and
     * a radius.
     *
     * @param latitude Is the latitude of a midpoint.
     * @param longitude Is the longitude of a midpoint.
     * @param radius Is the area to obtain data.
     * @return A string in Json format.
     */
    public String getEdges(double latitude, double longitude, int radius) {
        try {
            @SuppressLint("DefaultLocale") String request = String.format(
                    "[out:json];way(around:%d,%f,%f)[highway~" +
                    "\"^(primary|secondary|tertiary|residential|unclassified)$\"][\"junction\"!=" +
                    "\"roundabout\"];node(w)->.n1;node(w)->.n2;way(bn.n1)(bn.n2);out meta;",
                    radius, latitude, longitude
            );

            String codedRequest = URLEncoder.encode(request, "UTF-8");

            URL url = new URL("https://overpass-api.de/api/interpreter?data=" + codedRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String way;
            while ((way = reader.readLine()) != null) {
                response.append(way);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
