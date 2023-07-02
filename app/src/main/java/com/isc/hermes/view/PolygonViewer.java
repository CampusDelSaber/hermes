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
public class PolygonViewer implements Style.OnStyleLoaded {

    private List<List<Point>> polygon;
    private String polygonColorHexCode;

    /**
     * This is the constructor method to render a polygon on map.
     * @param mapboxMap where the polygon will be render.
     * @param polygon to render on map.
     * @param polygonColor is the polygon color.
     */
    public PolygonViewer(MapboxMap mapboxMap, List<List<Point>> polygon, String polygonColor){
        this.polygonColorHexCode = polygonColor;
        this.polygon = polygon;
        mapboxMap.setStyle(MapManager.getInstance().getMapboxMap().getStyle().getUri(),this);
    }

    /**
     * Method to add polygons layers to map
     * @param style the style that has finished loading
     */
    public void onStyleLoaded(@NonNull Style style) {
        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(polygon)));
        style.addLayerBelow(new FillLayer("layer-id", "source-id").withProperties(
                        fillColor(Color.parseColor(polygonColorHexCode)),
                        fillOpacity(0.5f)
                ), "settlement-label"
        );
    }
}
