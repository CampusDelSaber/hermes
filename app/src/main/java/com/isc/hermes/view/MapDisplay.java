package com.isc.hermes.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.isc.hermes.model.Utils.PolylineManager;
import com.isc.hermes.utils.MapClickEventsManager;
import com.isc.hermes.utils.MapConfigure;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for displaying a map using a MapView object and a MapConfigure object.
 */
public class MapDisplay {
    private final MapView mapView;
    private final MapConfigure mapConfigure;
    private final Context context;
    private MapboxMap mapboxMap;

    /**
     * Constructor to create a MapDisplay object.
     *
     * @param mapView       the MapView object to display the map
     * @param mapConfigure  the MapConfigure object to configure the map
     */
    public MapDisplay(Context context, MapView mapView, MapConfigure mapConfigure) {
        this.mapView = mapView;
        this.mapConfigure = mapConfigure;
        this.context = context;
        mapConfigure.setContext(context);
       // mapView.getMapAsync(new OnMapReadyCallback() {
            /*@Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                System.out.println("holaaaaaaaaaaaaa");
                List<LatLng> latLngList = new ArrayList<>();
                LineManager lineManager = new LineManager(mapView, mapboxMap, mapboxMap.getStyle());
                latLngList.add(new LatLng(37.42310572139881,-122.08530802399956));
                latLngList.add(new LatLng(37.42368853165682,-122.09017088513937));
                latLngList.add(new LatLng(37.419042832716755,-122.0933129947299));
                lineManager.create(new LineOptions()
                        .withLatLngs(latLngList)
                        .withLineColor(String.valueOf(Color.parseColor("#FF0000")))
                        .withLineWidth(4f));
                System.out.println("siuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
            }

        });*/
    }

    public void addPolylineToMap() {
        // Las coordenadas están hardcodeadas para este ejemplo
        List<Point> polylineCoordinates = new ArrayList<>();
        polylineCoordinates.add(Point.fromLngLat(-122.4194, 37.7749)); // Punto 1
        polylineCoordinates.add(Point.fromLngLat(-118.2437, 34.0522)); // Punto 2
        polylineCoordinates.add(Point.fromLngLat(-74.0060, 40.7128));  // Punto 3
        polylineCoordinates.add(Point.fromLngLat(-122.4194, 37.7749)); // Cerrar el triángulo

        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.getStyle(style -> {
                LineString lineString = LineString.fromLngLats(polylineCoordinates);
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



    /**
     * Method for creating the map and configuring it using the MapConfigure object.
     *
     * @param savedInstanceState the saved state of the instance
     */
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            mapConfigure.configure(mapboxMap);
        });
    }



    /**
     * Method for starting the MapView object instance.
     */
    public void onStart() {
        mapView.onStart();
    }

    /**
     * Method for resuming the MapView object instance.
     */
    public void onResume() {
        mapView.onResume();
    }

    /**
     * Method for pausing the MapView object instance.
     */
    public void onPause() {
        mapView.onPause();
    }

    /**
     * Method for stopping the MapView object instance.
     */
    public void onStop() {
        mapView.onStop();
    }

    /**
     * Method for handling low memory situations.
     */
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    /**
     * Method for destroying the MapView object instance.
     */
    public void onDestroy() {
        mapView.onDestroy();
    }

    /**
     * Method for saving the state of the MapView object instance.
     *
     * @param outState the state of the instance to save
     */
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    /** Getter for the MapboxMap object.
     *
     * @return the MapboxMap object
     */
    public MapboxMap getMapboxMap() {
        return mapboxMap;
    }

    /**
     * Method to set the map style to satellite, dark and default.
     *
     * @param mapStyle is the style for the map.
     */
    public void setMapStyle(String mapStyle) {
        if (mapView != null && mapStyle != null) {
            mapView.getMapAsync(mapboxMap -> {
                if (mapStyle.equals("satellite")) {
                    mapboxMap.setStyle(Style.SATELLITE_STREETS);
                } else if (mapStyle.equals("dark")) {
                    mapboxMap.setStyle(Style.DARK);
                } else {
                    mapboxMap.setStyle(Style.MAPBOX_STREETS);
                }
            });
        }
    }
}
