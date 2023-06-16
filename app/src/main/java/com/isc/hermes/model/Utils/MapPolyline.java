package com.isc.hermes.model.Utils;

import android.graphics.Color;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.Source;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class will be in charge of displaying the Polyline.
 */
public class MapPolyline {

    private MapView mapView;

    /**
     * Constructor method
     * @param mapView
     */
    public MapPolyline(MapView mapView) {
        this.mapView = mapView;
    }

    /**
     * This method will display the line with the coordinates.
     * @param geoJson coordinates.
     * @param color line color.
     */
    public void displaySavedCoordinates(String geoJson, int color){
        Feature feature = Feature.fromJson(geoJson);
        LineString lineString = (LineString) feature.geometry();
        List<Point> coordinates = getCoordinates(lineString);
        List<Point> coordinatesDisplayed = new ArrayList<>();

        for (Point point : coordinates) {
            System.out.println("Latitude: " + point.latitude());
            System.out.println("Longitude: " + point.longitude());
            coordinatesDisplayed.add(Point.fromLngLat(point.longitude(), point.latitude()));
        }
        MapPolyline mapPolyline = new MapPolyline(mapView);
        mapPolyline.drawPolyline(coordinatesDisplayed, color);
    }
    public List<Point> getCoordinates(LineString lineString){
        List<Point> lngLatAlts = lineString.coordinates();

        return new ArrayList<>(lngLatAlts);
    }

    /**
     * This method will plot the line on the map with a certain style and color.
     * @param pointList list with coordinates.
     * @param color polyline color.
     */
    public void drawPolyline(List<Point> pointList, int color) {
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                LineString lineString = LineString.fromLngLats(pointList);
                Feature feature = Feature.fromGeometry(lineString);
                Source polylineSource = new GeoJsonSource("polyline-source", feature);
                style.addSource(polylineSource);

                LineLayer polylineLayer = new LineLayer("polyline-layer", "polyline-source");
                polylineLayer.setProperties(
                        PropertyFactory.lineColor(color),
                        PropertyFactory.lineWidth(4f)
                );
                style.addLayer(polylineLayer);
            });
        });
    }
}
