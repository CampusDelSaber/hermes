package com.isc.hermes.model.navigation;

import java.util.HashMap;
import java.util.Map;

/**
 * The RoutesRepository class represents a repository for storing and retrieving route information.
 * It follows the Singleton pattern to ensure only one instance is created.
 */
public class RoutesRepository {
    private static RoutesRepository self;
    private Map<String, String> availableRoutes;

    /**
     * Constructs a new RoutesRepository object.
     * Initializes the availableRoutes map.
     */
    private RoutesRepository() {
        availableRoutes = new HashMap<>();
    }

    /**
     * Returns the instance of the RoutesRepository.
     *
     * @return the instance of the RoutesRepository
     */
    public static RoutesRepository getInstance() {
        if (self == null) {
            self = new RoutesRepository();
        }
        return self;
    }

    /**
     * Populates the repository with the provided routes.
     *
     * @param routes the map of routes to populate the repository with
     */
    public void populate(Map<String, String> routes) {
        self.availableRoutes = routes;
    }

    /**
     * Retrieves the route information for the given key.
     *
     * @param key the key of the route
     * @return the route information associated with the key, or null if not found
     */
    public String getRouteInformation(String key) {
        return availableRoutes.get(key);
    }

    /**
     * Clears the repository, removing all routes.
     */
    public void clean() {
        availableRoutes.clear();
    }
}
