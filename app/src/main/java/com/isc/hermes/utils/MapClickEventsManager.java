package com.isc.hermes.utils;


import android.content.Context;

import com.isc.hermes.controller.MapWayPointController;
import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to manage click on mapBox map
 */
public class MapClickEventsManager {

    private static MapClickEventsManager instance;
    private MapboxMap mapboxMap;
    private Context context;

    private MapClickConfigurationController configurationController;


    public static MapClickEventsManager getInstance(){
        if (instance==null) instance = new MapClickEventsManager();
        return instance;
    }

    public void setMapboxMap(MapboxMap mapboxMap){this.mapboxMap = mapboxMap;}
    public void setContext(Context context){this.context = context;}
    public MapboxMap getMapboxMap(){return this.mapboxMap;}
    public void setMapClickConfiguration(MapClickConfigurationController controller){
        this.configurationController = controller;
        mapboxMap.addOnMapClickListener(configurationController);
    }

    public void removeCurrentClickController(){
        configurationController.discardFromMap(this.getMapboxMap());
    }
}
