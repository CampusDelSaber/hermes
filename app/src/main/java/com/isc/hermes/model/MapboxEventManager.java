package com.isc.hermes.model;

import android.widget.Toast;

import com.isc.hermes.SearchViewActivity;
import com.isc.hermes.model.MapboxMapListener;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class MapboxEventManager {
    private static MapboxEventManager instance;
    private MapboxMapListener mapboxMapListener;
    private MapboxMap mapboxMap;
    private List<WayPoint> wayPoints;

    private MapboxEventManager() {
    }

    public static MapboxEventManager getInstance() {
        if (instance == null) {
            instance = new MapboxEventManager();
        }
        return instance;
    }

    public void setMapboxMapListener(MapboxMapListener listener) {
        mapboxMapListener = listener;
    }

    public void setMapboxMap(MapboxMap mapboxMap) {
        this.mapboxMap = (this.mapboxMap == null) ? mapboxMap : this.mapboxMap;
    }

    public boolean addMarker(WayPoint wayPoint) {
        /*if (mapboxMapListener != null) {
            mapboxMapListener.addMarker(markerOptions);
            return true;
        }*/
        if (mapboxMap != null) {
            LatLng selectedLocation = new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(selectedLocation)
                    .title(wayPoint.getPlaceName());
            mapboxMap.addMarker(markerOptions);

            CameraPosition position = new CameraPosition.Builder()
                    .target(selectedLocation)
                    .zoom(10)
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            return true;
        }
        return false;
    }
}