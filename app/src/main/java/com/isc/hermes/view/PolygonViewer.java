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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

/**
 * Class to display polygons on map although its style
 */
public class PolygonViewer implements Style.OnStyleLoaded {

    private List<List<Point>> polygon;
    private List<List<List<Point>>> polygons;
    private final String POLYGON_COLOR = "#6F1AB6";
    private final String POLYGONS_COLOR = "#FF0303";

    /**
     * This is the constructor method to render a polygon on map.
     *
     * @param mapboxMap where the polygon will be render.
     */
    public PolygonViewer(MapboxMap mapboxMap, List<List<List<Point>>> polygons, List<List<Point>> polygon) {
        this.polygons = polygons;
        this.polygon = polygon;
        mapboxMap.setStyle(MapManager.getInstance().getMapboxMap().getStyle().getUri(), this);
    }

    /**
     * Method to add polygons layers to map
     *
     * @param style the style that has finished loading
     */
    public void onStyleLoaded(@NonNull Style style) {
        if (polygon != null) loadPolygon(style);
        if (polygons != null && !polygons.isEmpty()) loadPolygons(style);
    }

    /**
     * This method render a only polygon on the map.
     *
     * @param style map style.
     */
    private void loadPolygon(@NonNull Style style) {
        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(polygon)));
        style.addLayerBelow(new LineLayer("border-layer-id", "source-id")
                .withProperties(
                        PropertyFactory.lineColor(Color.parseColor(POLYGON_COLOR)),
                        PropertyFactory.lineWidth(2.0f),
                        PropertyFactory.lineOffset(0f),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineOpacity(1f)
                ), "settlement-label");
        style.addLayerBelow(new FillLayer("layer-id", "source-id").withProperties(
                        fillColor(Color.parseColor(POLYGON_COLOR)),
                        fillOpacity(0.15f)
                ), "settlement-label"
        );
    }

    /**
     * This method render a polygons on the map.
     *
     * @param style map style,
     */
    private void loadPolygons(@NonNull Style style) {
        List<List<Point>> currentPolygon;
        for (int i = 0; i < polygons.size(); i++) {
            currentPolygon = polygons.get(i);
            style.addSource(new GeoJsonSource("source-id-" + i, Polygon.fromLngLats(currentPolygon)));

            style.addLayerBelow(new LineLayer("border-layer-id-" + i, "source-id-" + i)
                    .withProperties(
                            PropertyFactory.lineColor(Color.parseColor(POLYGONS_COLOR)),
                            PropertyFactory.lineWidth(2.0f),
                            PropertyFactory.lineOffset(0f),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineOpacity(1f)
                    ), "settlement-label");

            style.addLayerBelow(new FillLayer("fill-layer-id-" + i, "source-id-" + i)
                    .withProperties(
                            PropertyFactory.fillColor(Color.parseColor(POLYGONS_COLOR)),
                            PropertyFactory.fillOpacity(0.15f)
                    ), "border-layer-id-" + i);
        }
    }
}
