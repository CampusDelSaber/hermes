package com.isc.hermes.model.Utils;

import android.graphics.Color;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
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
    private List<List<Point>> allPoints;

    /**
     * Constructor method
     * @param mapView
     */
    public MapPolyline(MapView mapView) {
        this.mapView = mapView;
        this.allPoints = new ArrayList<>();
    }

    /**
     * This method will display the line with the coordinates.
     * @param geoJson coordinates.
     *
     * @param colors line color.
     */
    public void displaySavedCoordinates(List<String> geoJson, List<Integer> colors){
        List<Point> coordinatesRoute1 = new ArrayList<>();
        List<Point> coordinatesRoute2 = new ArrayList<>();
        List<Point> coordinatesRoute3 = new ArrayList<>();
        List<List<Point>> coordinates = new ArrayList<>();

        for (Point point : initializeCoordinates(geoJson.get(0))) {
            coordinatesRoute1.add(Point.fromLngLat(point.longitude(), point.latitude()));
        }
        for (Point point : initializeCoordinates(geoJson.get(1))) {
            coordinatesRoute2.add(Point.fromLngLat(point.longitude(), point.latitude()));
        }

        for (Point point : initializeCoordinates(geoJson.get(2))) {
            coordinatesRoute3.add(Point.fromLngLat(point.longitude(), point.latitude()));
        }

        coordinates.add(coordinatesRoute1);
        coordinates.add(coordinatesRoute2);
        coordinates.add(coordinatesRoute3);

        MapPolyline mapPolyline = new MapPolyline(mapView);
        mapPolyline.drawPolyline(coordinates,colors);
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
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {

                List<Point> polyline1Points = points.get(0);
                LineString polyline1 = LineString.fromLngLats(polyline1Points);

                List<Point> polyline2Points = points.get(1);
                LineString polyline2 = LineString.fromLngLats(polyline2Points);

                List<Point> polyline3Points = points.get(2);
                LineString polyline3 = LineString.fromLngLats(polyline3Points);

                List<Feature> features = setFeatures(polyline1, polyline2, polyline3);
                FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

                GeoJsonSource polyline1Source = new GeoJsonSource("polyline1-source", featureCollection);
                GeoJsonSource polyline2Source = new GeoJsonSource("polyline2-source", featureCollection);
                GeoJsonSource polyline3Source = new GeoJsonSource("polyline3-source", featureCollection);
                style.addSource(polyline1Source);
                style.addSource(polyline2Source);
                style.addSource(polyline3Source);

                verifyPolylineNull(polyline1Points, "polyline1-layer", "polyline1-source",colors, 0, style);
                verifyPolylineNull(polyline2Points, "polyline2-layer", "polyline2-source",colors, 1, style);
                verifyPolylineNull(polyline3Points, "polyline3-layer", "polyline3-source",colors, 2, style);

                polyline1Source.setGeoJson(features.get(0));
                polyline2Source.setGeoJson(features.get(1));
                polyline3Source.setGeoJson(features.get(2));
            });
        });
    }

    /**
     * This method will set the features with the polylines.
     *
     * @return list of features.
     */
    public  List<Feature> setFeatures(LineString polyline1, LineString polyline2, LineString polyline3){
        List<Feature> features = new ArrayList<>();
        features.add(Feature.fromGeometry(polyline1));
        features.add(Feature.fromGeometry(polyline2));
        features.add(Feature.fromGeometry(polyline3));
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
