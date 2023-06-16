package com.isc.hermes.utils;


import android.content.Context;
import com.isc.hermes.controller.interfaces.MapClickConfigurationController;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Class to controll click controller that mapbox map will adopt
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

    /**
     * Method to set mapbox map when this instance will init
     * @param mapboxMap is map will be set
     */
    public void setMapboxMap(MapboxMap mapboxMap){this.mapboxMap = mapboxMap;}

    /**
     * Method to set context when this instance will init
     * @param context is context will be set
     */
    public void setContext(Context context){this.context = context;}

    /**
     * Method to add a new mapbox map click configuration
     * @param controller is controller that will map adopt to control its click event
     */
    public void setMapClickConfiguration(MapClickConfigurationController controller){
        this.configurationController = controller;
        mapboxMap.addOnMapClickListener(configurationController);
    }

    /**
     * Method to remove current click controller map have
     */
    public void removeCurrentClickController(){configurationController.discardFromMap(this.getMapboxMap());}
    public MapboxMap getMapboxMap(){return this.mapboxMap;}
}
