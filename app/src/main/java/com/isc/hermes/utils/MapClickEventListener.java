package com.isc.hermes.utils;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to configure the event of do click on a map
 */
public class MapClickEventListener implements MapboxMap.OnMapClickListener {

    private LatLng latLngPoint;
    private final MapboxMap mapboxMap;

    public MapClickEventListener(MapboxMap mapboxMap){
        this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
    }

    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        latLngPoint = point;
        MarkerOptions markerOptions = new MarkerOptions().position(latLngPoint);
        mapboxMap.addMarker(markerOptions);

        return true;
    }

    public LatLng getLatLngPoint() {return latLngPoint;}
}
