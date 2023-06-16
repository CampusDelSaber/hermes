package com.isc.hermes.controller.interfaces;

import com.mapbox.mapboxsdk.maps.MapboxMap;

public interface MapClickConfigurationController extends MapboxMap.OnMapClickListener {

    void discardFromMap(MapboxMap mapboxMap);
}
