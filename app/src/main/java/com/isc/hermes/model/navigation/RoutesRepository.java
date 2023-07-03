package com.isc.hermes.model.navigation;

import java.util.HashMap;
import java.util.Map;

public class RoutesRepository {
    private Map<String, String> availableRoutes;
    private static RoutesRepository self;

    private RoutesRepository(){
        availableRoutes = new HashMap<>();
    }

    public static RoutesRepository getInstance() {
        if (self == null){
            self = new RoutesRepository();
        }
        return self;
    }

    public void populate(Map<String, String> routes){
        self.availableRoutes = routes;
    }

    public String getRouteInformation(String key){
        return availableRoutes.get(key);
    }

    public void clean(){
        availableRoutes.clear();
    }
}
