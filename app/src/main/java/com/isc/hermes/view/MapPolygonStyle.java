package com.isc.hermes.view;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

import android.graphics.Color;

import androidx.annotation.NonNull;


import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

/**
 * Class to display polygons on map although its style
 */
public class MapPolygonStyle implements Style.OnStyleLoaded {
    private final List<List<Point>> allPolygonsPoints;


    public MapPolygonStyle(MapboxMap mapboxMap,List<List<Point>> allPolygonsPoints){
        this.allPolygonsPoints = allPolygonsPoints;
        mapboxMap.setStyle(MapClickEventsManager.getInstance().getMapboxMap().getStyle().getUri(),this);
    }

    /**
     * Method to add polygons layers to map
     * @param style the style that has finished loading
     */
    public void onStyleLoaded(@NonNull Style style) {
        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(allPolygonsPoints)));
        style.addLayerBelow(new FillLayer("layer-id", "source-id").withProperties(
                        fillColor(Color.parseColor("#3bb2d0")),
                        fillOpacity(0.5f)
                ), "settlement-label"
        );
    }
}
