package com.isc.hermes.model;

import android.content.Context;
import android.os.StrictMode;
import com.isc.hermes.R;
import com.isc.hermes.utils.PlaceSearch;
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

    protected CurrentLocationModel currentLocationModel;
    private Context context;
    private PlaceSearch placeSearch;

    /**
     * This method is the constructor of the class
     */
    public Searcher(){
        placeSearch = new PlaceSearch();
    }

    /**
     * Overloading constructor of the class
     */
    public Searcher(Context applicationContext) {
        currentLocationModel  = new CurrentLocationModel();
        this.context = applicationContext;
    }


    /**
     * Retrieves a list of suggestion features for a given user location and query.
     *
     * @param userLocation The current user location as a CurrentLocationModel object.
     * @param query        The search query for which suggestion features will be obtained.
     * @return A list of CarmenFeature objects representing the found suggestion features.
     * @throws IOException If an error occurs during the geocoding call execution.
     */
    public List<CarmenFeature> getSuggestionsFeatures(CurrentLocationModel userLocation, String query) {
        MapboxGeocoding client = buildGeocodingClient(userLocation, query, "BO");
        Response<GeocodingResponse> geocodingResponse = executeGeocodingCall(client);

        if (!isGeocodingResponseValid(geocodingResponse)) {
            client = buildGeocodingClient(userLocation, query, null);
            geocodingResponse = executeGeocodingCall(client);
        }

        return getFeaturesFromGeocodingResponse(geocodingResponse);
    }

    /**
     * Builds a Mapbox Geocoding client based on the provided user location, query, and country.
     *
     * @param userLocation The current user location as a CurrentLocationModel object.
     * @param query        The search query to be used for geocoding.
     * @param country      The country code to limit geocoding results (optional, can be null).
     * @return A MapboxGeocoding object configured with the specified parameters.
     */
    private MapboxGeocoding buildGeocodingClient(CurrentLocationModel userLocation, String query, String country) {
        MapboxGeocoding.Builder builder = MapboxGeocoding.builder()
                .accessToken(context.getString(R.string.access_token))
                .query(query)
                .proximity(Point.fromLngLat(userLocation.getLongitude(), userLocation.getLatitude()))
                .autocomplete(true);

        if (country != null) {
            builder.country(country);
        }

        return builder.build();
    }

    /**
     * Executes a geocoding call using the provided MapboxGeocoding client.
     *
     * @param client The MapboxGeocoding client configured for the geocoding request.
     * @return The Response object containing the geocoding response.
     */
    private Response<GeocodingResponse> executeGeocodingCall(MapboxGeocoding client) {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            return client.executeCall();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the geocoding response is valid.
     *
     * @param geocodingResponse The Response object containing the geocoding response.
     * @return True if the response is valid, false otherwise.
     */
    private boolean isGeocodingResponseValid(Response<GeocodingResponse> geocodingResponse) {
        return geocodingResponse.isSuccessful()
                && geocodingResponse.body() != null
                && !geocodingResponse.body().features().isEmpty();
    }

    /**
     * Retrieves the list of CarmenFeature objects from the geocoding response.
     *
     * @param geocodingResponse The Response object containing the geocoding response.
     * @return A list of CarmenFeature objects representing the found features.
     */
    private List<CarmenFeature> getFeaturesFromGeocodingResponse(Response<GeocodingResponse> geocodingResponse) {
        return Objects.requireNonNull(geocodingResponse.body()).features();
    }

    /**
     * Method to get the searcher Suggestions places information setting a waypoint to use it later as a location in map
     *
     * @param query the consult of the searcher field text
     * @return the features list with the waypoint of the suggestions
     */
    public List<WayPoint> getSearcherSuggestionsPlacesInfo(String query) {
        if (query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<WayPoint> featuresInfoList = new ArrayList<>();
        List<CarmenFeature> suggestions = getSuggestionsFeatures(currentLocationModel, query);
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

    /**
     * Searches for places of a specific type near the current location.
     *
     * @param placeType   the type of place to search for
     * @param listener    the listener to receive the search results
     */
    public void searchPlacesByType(String placeType, PlaceSearch.SearchPlacesListener listener) {
        // TODO: Replace this data with the current location
        double latitude = -17.37;
        double longitude = -66.18;
        placeSearch.searchPlacesByType(placeType, latitude, longitude, listener);
    }


}
