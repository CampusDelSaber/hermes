package com.isc.hermes.view;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.isc.hermes.utils.MapManager;
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
public class PolygonsViewer implements Style.OnStyleLoaded {
    private final List<List<List<Point>>> polygons;
    private final String polygonColor;

    public PolygonsViewer(MapboxMap mapboxMap, List<List<List<Point>>> polygons, String polygonColor){
        this.polygonColor = polygonColor;
        this.polygons = polygons;
        mapboxMap.setStyle(MapManager.getInstance().getMapboxMap().getStyle().getUri(),this);
    }

    /**
     * Method to add polygons layers to map
     * @param style the style that has finished loading
     */
    public void onStyleLoaded(@NonNull Style style) {
        List<List<Point>> polygon;
        for (int i = 0; i < polygons.size(); i++) {
            polygon = polygons.get(i);
            style.addSource(new GeoJsonSource("source-id-" + i, Polygon.fromLngLats(polygon)));
            style.addLayerBelow(new FillLayer("layer-id-" + i, "source-id-" + i).withProperties(
                    fillColor(Color.parseColor(polygonColor)),
                    fillOpacity(0.3f)
            ), "settlement-label");
        }
    }
}
