package com.isc.hermes.model;

import android.os.StrictMode;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Response;


/**
 * Class to represent the searcher functionality of the app,
 * setting the methods to make a call at the api to receive
 * the real time suggestions of a query received
 *
 * @see CarmenFeature a feature reresented with geojson properties
 */
public class Searcher {
    /**
     * Method to get the the suggestions features carmen List to have access of all the properties to render the locations
     *
     * @param query the consult of the searcher field text
     * @return the features list with the suggestions features
     */
    public List<CarmenFeature> getSuggestionsFeatures(String query) {
        MapboxGeocoding client = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(query)
                .autocomplete(true)
                .build();

        Response<GeocodingResponse> geocodingResponseResponse;
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            geocodingResponseResponse = client.executeCall();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return Objects.requireNonNull(geocodingResponseResponse.body()).features();
    }

    /**
     * Method to get the searcher Suggestions places information setting a waypoint to use it later as a location in map
     *
     * @param query the consult of the searcher field text
     * @return the features list with the waypoint of the suggestions
     */
    public List<WayPoint> getSearcherSuggestionsPlacesInfo(String query) {
        List<WayPoint> featuresInfoList = new ArrayList<>();
        List<CarmenFeature> suggestions = getSuggestionsFeatures(query);
        for (CarmenFeature feature : suggestions) {
            featuresInfoList.add(instanceWaypointFeature(feature));
        }
        return featuresInfoList;
    }

    /**
     * Method to instance a wapoint feature passing the latitud and longitud of a geo point of the feature
     *
     * @param feature to pass the attributes of a waypoint
     * @return the instanced waypoint
     */
    private WayPoint instanceWaypointFeature(CarmenFeature feature) {
        Point point = feature.center();
        assert point != null;
        return new WayPoint(
                feature.placeName(),
                feature.properties(),
                point.latitude(),
                point.longitude());
    }

}
