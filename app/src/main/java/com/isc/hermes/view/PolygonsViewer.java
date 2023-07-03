package com.isc.hermes.view;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.isc.hermes.utils.MapManager;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

/**
 * Class to display polygons on map although its style
 */
public class PolygonsViewer implements Style.OnStyleLoaded {
    private final List<List<List<Point>>> polygons;
    private final String polygonColor;

    /**
     * This is the constructor method to build the polygon viewer.
     *
     * @param mapboxMap where the polygons will be displayed.
     * @param polygons to displayed.
     * @param polygonColor is the color for each polygon.
     */
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

            style.addLayerBelow(new LineLayer("border-layer-id-" + i, "source-id-" + i)
                    .withProperties(
                            PropertyFactory.lineColor(Color.parseColor(polygonColor)),
                            PropertyFactory.lineWidth(2.0f),
                            PropertyFactory.lineOffset(0f),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineOpacity(1f)
                    ), "settlement-label");

            style.addLayerBelow(new FillLayer("fill-layer-id-" + i, "source-id-" + i)
                    .withProperties(
                            PropertyFactory.fillColor(Color.parseColor(polygonColor)),
                            PropertyFactory.fillOpacity(0.1f)
                    ), "border-layer-id-" + i);
        }
    }
}
