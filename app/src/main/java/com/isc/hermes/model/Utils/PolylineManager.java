package com.isc.hermes.model.Utils;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import java.util.ArrayList;
import java.util.List;

//LatLng [latitude=37.42310572139881, longitude=-122.08530802399956, altitude=0.0]
//LatLng [latitude=37.42368853165682, longitude=-122.09017088513937, altitude=0.0]
//LatLng [latitude=37.419042832716755, longitude=-122.0933129947299, altitude=0.0]
public class PolylineManager implements OnMapReadyCallback {
    private List<LatLng> latLngList;
    private List<Point> points;
    private MapView mapView;

    private LineManager lineManager;


    public PolylineManager(MapView mapView) {
        this.latLngList = new ArrayList<>();
        this.points = new ArrayList<>();
        this.mapView = mapView;
        latLngList.add(new LatLng(37.42310572139881,-122.08530802399956));
        latLngList.add(new LatLng(37.42368853165682,-122.09017088513937));
        latLngList.add(new LatLng(37.419042832716755,-122.0933129947299));
        points.add(Point.fromLngLat(-122.08530802399956, 37.42310572139881));
        points.add(Point.fromLngLat(-122.09017088513937, 37.42368853165682));
        points.add(Point.fromLngLat(-122.0933129947299, 37.419042832716755));
        //onMapReady(MapClickEventsManager.getInstance().getMapboxMap());

        System.out.println("holaaaaaaaa");
        //mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        LineManager lineManager = new LineManager(mapView, mapboxMap, mapboxMap.getStyle());
        LineString lineString = LineString.fromLngLats(points);

        lineManager.create(new LineOptions()
                .withLatLngs(latLngList)
                .withLineColor(String.valueOf(Color.parseColor("#FF0000")))
                .withLineWidth(4f));
    }
}
