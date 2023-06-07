package com.isc.hermes.model;


import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import java.io.IOException;
import retrofit2.Response;


public class Searcher {
    private int resultsNum = 0;
    public int performSearch(String query) {
        System.out.println("----HOLA---");
        MapboxGeocoding client = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(query)
                .build();

        System.out.println("**++++++++entro a cliente**********+");

        Response<GeocodingResponse> geocodingResponseResponse;

        try {
            geocodingResponseResponse = client.executeCall();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(geocodingResponseResponse.body().query().size());
        System.out.println(geocodingResponseResponse.raw());
        System.out.println(geocodingResponseResponse.body().features().get(0).placeName()+ " SIUUUU");
        System.out.println(geocodingResponseResponse.body().query().toString());

        for (CarmenFeature feature: geocodingResponseResponse.body().features()) {
            Point point = feature.center();
            System.out.println("------");
            System.out.println(feature.placeName());
            System.out.println(feature.properties());
            System.out.println(point.latitude());
            System.out.println(point.longitude());
            System.out.println("-----------");
        }

        System.out.println("end of line"+ resultsNum + "UWUWWWWWWWWWWWWWWWWWWWWWWWWW");
        return geocodingResponseResponse.body().features().size();
    }
}
