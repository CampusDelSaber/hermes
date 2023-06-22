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

    public List<Point> initializeCoordinates(String geoJson){
        Feature feature = Feature.fromJson(geoJson);
        LineString lineString = (LineString) feature.geometry();
        List<Point> coordinates = getCoordinates(lineString);
        return coordinates;
    }
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


                List<Feature> features = new ArrayList<>();
                features.add(Feature.fromGeometry(polyline1));
                features.add(Feature.fromGeometry(polyline2));
                features.add(Feature.fromGeometry(polyline3));
                FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

                GeoJsonSource polyline1Source = new GeoJsonSource("polyline1-source", featureCollection);
                GeoJsonSource polyline2Source = new GeoJsonSource("polyline2-source", featureCollection);
                GeoJsonSource polyline3Source = new GeoJsonSource("polyline3-source", featureCollection);
                style.addSource(polyline1Source);
                style.addSource(polyline2Source);
                style.addSource(polyline3Source);

                if(polyline1Points != null){
                    LineLayer polyline1Layer = new LineLayer("polyline1-layer", "polyline1-source");
                    polyline1Layer.setProperties(
                            PropertyFactory.lineColor(colors.get(0)),
                            PropertyFactory.lineWidth(4.5f)
                    );
                    style.addLayer(polyline1Layer);
                }

                if(polyline2Points != null){
                    System.out.println("BOBO");
                    LineLayer polyline2Layer = new LineLayer("polyline2-layer", "polyline2-source");
                    polyline2Layer.setProperties(
                            PropertyFactory.lineColor(colors.get(1)),
                            PropertyFactory.lineWidth(4.5f)
                    );
                    style.addLayer(polyline2Layer);
                }

                if(polyline3Points != null){
                    LineLayer polyline3Layer = new LineLayer("polyline3-layer", "polyline3-source");
                    polyline3Layer.setProperties(
                            PropertyFactory.lineColor(colors.get(2)),
                            PropertyFactory.lineWidth(4.5f)
                    );
                    style.addLayer(polyline3Layer);
                }

                polyline1Source.setGeoJson(features.get(0));
                polyline2Source.setGeoJson(features.get(1));
                polyline3Source.setGeoJson(features.get(2));
            });
        });
    }

}
