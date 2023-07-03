package com.isc.hermes.model.Utils;


import android.graphics.Color;

import com.isc.hermes.utils.MapManager;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be in charge of displaying the Polyline.
 */
public class MapPolyline {
    private final MapboxMap mapboxMap;
    private ArrayList<GeoJsonSource> idPolyLinesList;


    /**
     * Constructor method
     */
    public MapPolyline() {
        mapboxMap = MapManager.getInstance().getMapboxMap();
        idPolyLinesList = new ArrayList<>();
    }

    /**
     * Draws a polyline on the screen based on the provided GeoJSON, colors, and coordinates.
     *
     * @param geoJson      The GeoJSON data representing the polyline.
     * @param colors       The list of colors to be used for different segments of the polyline.
     * @param coordinates  The list of coordinate lists defining the polyline.
     */
    public void drawPolyline(List<String> geoJson, List<Integer> colors, List<List<Point>> coordinates) {
        extractCoordinates(coordinates, geoJson);
        drawPolyline(coordinates, colors);
    }

    /**
     * This method will display the line with the coordinates.
     * @param geoJson coordinates.
     *
     * @param colors line color.
     */
    public void displaySavedCoordinates(List<String> geoJson, List<Integer> colors){
        Thread thread = new Thread(new DrawPolylineRunnable(geoJson, colors));
        thread.start();
    }

    /**
     * This method extracts the coordinates from the list and geoJson
     */
    private void extractCoordinates(List<List<Point>> coordinates, List<String> geoJson){
        for (String json : geoJson){
            List<Point> coordinatesRoute = new ArrayList<>();
            for (Point point : initializeCoordinates(json)){
                coordinatesRoute.add(Point.fromLngLat(point.longitude(), point.latitude()));
            }
            coordinates.add(coordinatesRoute);
        }
    }

    /**
     * This method will extract coordinates form geoJson.
     * @param geoJson coordinates.
     * @return
     */
    public List<Point> initializeCoordinates(String geoJson){
        Feature feature = Feature.fromJson(geoJson);
        LineString lineString = (LineString) feature.geometry();
        List<Point> coordinates = getCoordinates(lineString);
        return coordinates;
    }

    /**
     * This methodo will return and array list with coordinates
     * @param lineString geographic point.
     * @return
     */
    public List<Point> getCoordinates(LineString lineString){
        List<Point> lngLatAlts = lineString.coordinates();

        return new ArrayList<>(lngLatAlts);
    }

    /**
     * This method will plot the line on the map with a certain style and color.
     * @param colors polyline color.
     */

    public void drawPolyline(List<List<Point>> points, List<Integer> colors){
        mapboxMap.getStyle(style -> {
            List<LineString> polyLineGeoJson = getPointsList(points, colors, style);
            List<Feature> features = setFeatures(polyLineGeoJson);
            FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
            for (int i = 0; i < features.size(); i++){
                GeoJsonSource polylineSource =
                        new GeoJsonSource("polyline" + i + "-source", featureCollection);
                idPolyLinesList.add(polylineSource);
                style.addSource(polylineSource);
                polylineSource.setGeoJson(features.get(i));
            }
        });
    }


    /**
     * This method will hide all the polylines in the map
     */
    public void hidePolylines() {
        mapboxMap.setStyle(mapboxMap.getStyle().getUri());
    }
    /**
     * This method will get the points list
     * @param points
     * @param colors
     * @param style
     * @return
     */
    private List<LineString> getPointsList(
            List<List<Point>> points, List<Integer> colors, Style style
    ){
        List<LineString> polyLineGeoJson = new ArrayList<>();
        for (int i = 0; i < points.size(); i++){
            List<Point> pointList = points.get(i);
            polyLineGeoJson.add(LineString.fromLngLats(pointList));
            verifyPolylineNull(
                    pointList,
                    "polyline" + i + "-layer",
                    "polyline" + i + "-source",
                    colors, i, style);
        }
        return polyLineGeoJson;
    }

    /**
     * This method will set the features with the polylines.
     *
     * @return list of features.
     */
    public  List<Feature> setFeatures(List<LineString> polylineList){
        List<Feature> features = new ArrayList<>();
        for (LineString lineString : polylineList){
            features.add(Feature.fromGeometry(lineString));
        }
        return features;
    }

    /**
     * This method will set the style for polylines.
     *
     * @param polylinePoints list of polylines
     * @param colors list of colors
     */
    public void verifyPolylineNull(List<Point> polylinePoints, String layerID, String sourceID, List<Integer> colors, int count, Style style){
        if(polylinePoints != null && colors.get(count) != null){
            LineLayer polylineLayer = new LineLayer(layerID, sourceID);
            polylineLayer.setProperties(
                    PropertyFactory.lineColor(colors.get(count)),
                    PropertyFactory.lineWidth(4.5f)
            );
            style.addLayer(polylineLayer);
        }
    }

    /**
     * Displays a polyline on the map between two given points.
     *
     * @param point1 The first point of the polyline.
     * @param point2 The second point of the polyline.
     */
    public void displayPolyline(LatLng point1, LatLng point2) {
        if (mapboxMap != null) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .add(point1, point2)
                    .color(Color.GRAY)
                    .width(5));
        }
    }

    private class DrawPolylineRunnable implements Runnable{
        private List<String> geoJson;
        private List<Integer> colors;
        public DrawPolylineRunnable(List<String> geoJson, List<Integer> colors){
            this.geoJson = geoJson;
            this.colors = colors;
        }

        @Override
        public void run() {
            drawPolyline(geoJson, colors, new ArrayList<>());
        }
    }
}