package com.isc.hermes.utils;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.widget.SearchView;

import androidx.annotation.NonNull;

import com.google.android.material.color.utilities.PointProvider;
import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import java.util.List;

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
        addMapboxSearcher(mapboxMap);
    }

    private void addMapboxSearcher(MapboxMap mapboxMap) {
        // Create a symbol layer to display search results on the map
        SymbolLayer symbolLayer = new SymbolLayer("searchResultsLayer", "searchResultsSource");
        symbolLayer.setProperties(
                iconAllowOverlap(true),
                iconImage("marker-icon"), // Customize the marker icon
                iconSize(1.5f),
                textField("{place_name}"), // Customize the label text
                textColor(Color.BLACK),
                textSize(12f),
                textOffset(new Float[]{0f, -1.5f})
        );

        // Add the symbol layer to the map
        mapboxMap.getStyle(style -> style.addLayer(symbolLayer));
    }
}
