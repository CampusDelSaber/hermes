package com.isc.hermes.model;


import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Searcher {
    public void performSearch(String query) {
        MapboxGeocoding client = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(query)
                .build();

        client.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CarmenFeature> results = response.body().features();
                    System.out.println(results.get(0));
                    // Process the search results
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                // Handle the failure
            }
        });
    }
}
