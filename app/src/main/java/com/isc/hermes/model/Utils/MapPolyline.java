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


import java.util.Arrays;
import java.util.List;


public class MapPolyline {

    private MapView mapView;
    private Point startPoint;
    private Point endPoint;
    private List<Point> pointList;



    public MapPolyline(MapView mapView, List<Point> pointList) { //Point startPoint, Point endPoint
        this.mapView = mapView;

        this.pointList = pointList;
        //this.startPoint = startPoint;
        //this.endPoint = endPoint;
    }



    public void drawPolyline() {
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                LineString lineString = LineString.fromLngLats(this.pointList);
                Feature feature = Feature.fromGeometry(lineString);
                Source polylineSource = new GeoJsonSource("polyline-source", feature);
                style.addSource(polylineSource);

                LineLayer polylineLayer = new LineLayer("polyline-layer", "polyline-source");
                polylineLayer.setProperties(
                        PropertyFactory.lineColor(Color.RED),
                        PropertyFactory.lineWidth(4f)
                );
                style.addLayer(polylineLayer);
            });
        });
    }
}
