package com.isc.hermes.model.Utils;

import android.graphics.Color;

import com.isc.hermes.utils.MapManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class MapPolyline {
    private final MapboxMap mapboxMap;
    private List<List<Point>> allPoints;
    private List<Integer> colors;

    public MapPolyline() {
        mapboxMap = MapManager.getInstance().getMapboxMap();
        this.allPoints = new ArrayList<>();
        this.colors = new ArrayList<>();
    }

    public void displaySavedCoordinates(List<String> geoJson, List<Integer> colors) {
        extractCoordinates(geoJson);
        this.colors = colors;

        Thread thread = new Thread(new DrawPolylineRunnable());
        thread.start();
    }

    private void extractCoordinates(List<String> geoJson) {
        for (String json : geoJson) {
            List<Point> coordinatesRoute = new ArrayList<>();
            for (Point point : initializeCoordinates(json)) {
                coordinatesRoute.add(Point.fromLngLat(point.longitude(), point.latitude()));
            }
            allPoints.add(coordinatesRoute);
        }
    }

    private List<Point> initializeCoordinates(String geoJson) {
        LineString lineString = LineString.fromJson(geoJson);
        return getCoordinates(lineString);
    }

    private List<Point> getCoordinates(LineString lineString) {
        return new ArrayList<>(lineString.coordinates());
    }

    private void drawPolyline(List<List<Point>> points, List<Integer> colors) {
        mapboxMap.getStyle(style -> {
            List<LineString> polyLineGeoJson = getPointsList(points, colors, style);
            List<Feature> features = setFeatures(polyLineGeoJson);
            FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

            style.addSource(new GeoJsonSource("polyline-source", featureCollection));
            style.addLayer(new LineLayer("polyline-layer", "polyline-source")
                    .withProperties(
                            PropertyFactory.lineColor(colors.get(0)), // Use the first color
                            PropertyFactory.lineWidth(4.5f)
                    ));
        });
    }

    private List<LineString> getPointsList(List<List<Point>> points, List<Integer> colors, Style style) {
        List<LineString> polyLineGeoJson = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            List<Point> pointList = points.get(i);
            polyLineGeoJson.add(LineString.fromLngLats(pointList));
            verifyPolylineNull(pointList, colors.get(i), style);
        }
        return polyLineGeoJson;
    }

    private List<Feature> setFeatures(List<LineString> polylineList) {
        List<Feature> features = new ArrayList<>();
        for (LineString lineString : polylineList) {
            features.add(Feature.fromGeometry(lineString));
        }
        return features;
    }

    private void verifyPolylineNull(List<Point> polylinePoints, int color, Style style) {
        if (polylinePoints != null) {
            LineLayer polylineLayer = style.getLayerAs("polyline-layer");
            if (polylineLayer != null) {
                polylineLayer.setProperties(
                        PropertyFactory.lineColor(color),
                        PropertyFactory.lineWidth(4.5f)
                );
            }
        }
    }

    public void displayPolyline(LatLng point1, LatLng point2) {
        if (mapboxMap != null) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .add(point1, point2)
                    .color(Color.GRAY)
                    .width(5));
        }
    }

    private class DrawPolylineRunnable implements Runnable {
        @Override
        public void run() {
            drawPolyline(allPoints, colors);
        }
    }
}
