package com.isc.hermes.utils;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.isc.hermes.R;
import com.isc.hermes.model.IncidentsGenerator;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

/**
 * Class for configuring a MapboxMap object.
 */
public class MapConfigure {

    /**
     * Configures a MapboxMap object with the MAPBOX_STREETS style.
     *
     * @param mapboxMap the MapboxMap object to be configured
     */
    public void configure(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        Polygon[] polygons = IncidentsGenerator.generateIncidentsZones(
                -17.395495,
                -66.148099,
                5);

        for (Polygon polygon : polygons) {
            mapboxMap.getStyle().addSource(new GeoJsonSource("polygon-source", polygon));
            FillLayer polygonLayer = new FillLayer("polygon-layer", "polygon-source");
            polygonLayer.setProperties(
                    fillOpacity(0.5f),
                    fillColor("#3bb2d0")
            );

            mapboxMap.getStyle().addLayer(polygonLayer);
            Log.i("Mapbox", "Polygon added");
        }
    }
}
