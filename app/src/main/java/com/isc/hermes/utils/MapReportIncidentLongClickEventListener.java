package com.isc.hermes.utils;


import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to configure the event of do a long click on a map
 */
public class MapReportIncidentLongClickEventListener implements MapboxMap.OnMapLongClickListener{

    private final MapboxMap mapboxMap;

    public MapReportIncidentLongClickEventListener(MapboxMap mapboxMap){
        this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapLongClickListener(this);
    }


    /**
     * Method to add markers to map variable
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions().position(point);
        mapboxMap.addMarker(markerOptions);
        return true;
    }


}
