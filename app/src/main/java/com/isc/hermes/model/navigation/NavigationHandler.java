package com.isc.hermes.model.navigation;

import java.util.Map;

public class NavigationHandler {
    private Map<String, UserRouteTracker> availableRoutes;

    public void startNavigationMode(){
        Map<String, String> routes = RoutesRepository.getInstance().getRoutes();
    }

    private UserRouteTracker addRoute(String geoJSONRoute){
        final UserRouteTracker[] routeTracker = {null};

        new Thread(new Runnable() {
            @Override
            public void run() {
               routeTracker[0] = new UserRouteTracker(geoJSONRoute);
            }
        });

        return routeTracker[0];
    }
}
