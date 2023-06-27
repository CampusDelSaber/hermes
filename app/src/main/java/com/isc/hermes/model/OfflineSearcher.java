package com.isc.hermes.model;

import android.content.Context;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import java.util.List;

import retrofit2.Response;

public class OfflineSearcher {

    private Context context;
    protected CurrentLocationModel currentLocationModel;


    /**
     * @return
     */
    public Response<GeocodingResponse> getSuggestionsFeatureOffline(){
        OfflineGeocoding offlineGeocoding = OfflineGeocoding.getInstance(context);
        MapboxGeocodingOffline reverseGeocode = offlineGeocoding.createReverseGeocodeOfflineTask(
                language, text, limit, proximity);
        reverseGeocode.setOfflineReverseGeocodeCallback(new OfflineReverseGeocodeCallback() {
            @Override
            public void onSuccess(List<GeocodingFeature> results) {
                // Maneja los resultados de la búsqueda sin conexión exitosa
            }

            @Override
            public void onError(String errorMessage) {
                // Maneja los errores de la búsqueda sin conexión
            }
        });
        reverseGeocode.execute();
        return
    }

    /**
     * @param offlineFeatures
     * @return
     */
    public static Response<GeocodingResponse> convertToGeocodingResponse(List<GeocodingFeature> offlineFeatures) {
        List<CarmenFeature> carmenFeatures = new ArrayList<>();
        for (GeocodingFeature offlineFeature : offlineFeatures) {
            CarmenFeature carmenFeature = convertToCarmenFeature(offlineFeature);
            carmenFeatures.add(carmenFeature);
        }

        CarmenContext carmenContext = new CarmenContext.Builder().build();
        CarmenGeometry carmenGeometry = new CarmenGeometry.Builder().build();

        GeocodingResponse geocodingResponse = new GeocodingResponse.Builder()
                .attribution(GeocodingCriteria.ATTRIBUTION_GEOMETRY)
                .query("")
                .features(carmenFeatures)
                .rawResponse("")
                .type(GeocodingCriteria.TYPE_PLACE)
                .carmenContext(carmenContext)
                .carmenGeometry(carmenGeometry)
                .build();

        Response<GeocodingResponse> response = Response.success(geocodingResponse);
        return response;
    }

    /**
     * @param offlineFeature
     * @return
     */
    private static CarmenFeature convertToCarmenFeature(GeocodingFeature offlineFeature) {
        // Obtener los datos necesarios de la GeocodingFeature offline
        String id = offlineFeature.getId();
        String type = offlineFeature.getType();
        String placeName = offlineFeature.getPlaceName();
        String relevance = offlineFeature.getRelevance();
        CarmenGeometry carmenGeometry = new CarmenGeometry.Builder()
                .type(offlineFeature.getGeometry().getType())
                .coordinates(offlineFeature.getGeometry().getCoordinates())
                .build();

        // Construir el CarmenFeature
        CarmenFeature carmenFeature = new CarmenFeature.Builder()
                .id(id)
                .type(type)
                .placeName(placeName)
                .relevance(Float.parseFloat(relevance))
                .geometry(carmenGeometry)
                .build();

        return carmenFeature;
    }
}
