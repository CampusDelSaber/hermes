package com.isc.hermes.controller;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;


public class MapPolygonController implements MapClickConfigurationController {
    private final MapboxMap mapboxMap;
    private final PolygonOptionsController polygonOptionsController;

    private final List<List<Point>> allPolygonsPoints;
    private final List<Point> polygonPoints;


    public MapPolygonController(MapboxMap mapboxMap, Context context ) {
        this.mapboxMap = mapboxMap;
        polygonOptionsController = new PolygonOptionsController(context, this);
        polygonOptionsController.displayComponents();
        Animations.loadAnimations();
        this.allPolygonsPoints = new ArrayList<>();
        this.polygonPoints = new ArrayList<>();
        this.allPolygonsPoints.add(polygonPoints);
    }

    public boolean onMapClick(@NonNull LatLng point) {
        polygonPoints.add(Point.fromLngLat(point.getLongitude(),point.getLatitude()));
        mapboxMap.setStyle(MapClickEventsManager.getInstance().getMapboxMap().getStyle().getUri(), style -> {
            style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(allPolygonsPoints)));
            style.addLayerBelow(new FillLayer("layer-id", "source-id").withProperties(
                    fillColor(Color.parseColor("#3bb2d0")),
                    fillOpacity(0.5f)
                    ), "settlement-label"
            );
        });
        return false;
    }

    public void discardFromMap(MapboxMap mapboxMap) {
        mapboxMap.removeOnMapClickListener(this);
    }

    public void deleteMarks() {
        for (Marker marker:mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }

}
