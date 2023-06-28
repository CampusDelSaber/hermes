package com.isc.hermes.model.Utils;


import com.isc.hermes.utils.MapManager;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Feature;
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
    private List<List<Point>> allPoints;
    private ArrayList<GeoJsonSource> idPolyLinesList;


    /**
     * Constructor method
     */
    public MapPolyline() {
        mapboxMap = MapManager.getInstance().getMapboxMap();
        this.allPoints = new ArrayList<>();
        this.idPolyLinesList = new ArrayList<>();
    }

    /**
     * This method will display the line with the coordinates.
     * @param geoJson coordinates.
     *
     * @param colors line color.
     */
    public void displaySavedCoordinates(List<String> geoJson, List<Integer> colors){
        long startTime = System.currentTimeMillis();
        List<List<Point>> coordinates = new ArrayList<>();
        extractCoordinates(coordinates, geoJson);

        MapPolyline mapPolyline = new MapPolyline();
        mapPolyline.drawPolyline(coordinates,colors);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("DURATION displaySavedCoordinates");
        System.out.println("Execution time: " + executionTime + " milliseconds");

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
        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("DRAW POLYLINE");
        System.out.println("Execution time: " + executionTime + " milliseconds");
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
        if(polylinePoints != null){
            LineLayer polylineLayer = new LineLayer(layerID, sourceID);
            polylineLayer.setProperties(
                    PropertyFactory.lineColor(colors.get(count)),
                    PropertyFactory.lineWidth(4.5f)
            );
            style.addLayer(polylineLayer);
        }
    }
}
