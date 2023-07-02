package com.isc.hermes.model.navigation.exceptions;

/**
 * The RouteOutOfTracksException class is an exception that is thrown when a route has run out of tracks.
 * This exception indicates that there are no more tracks available to continue the route.
 */
public class RouteOutOfTracksException extends RuntimeException {

    /**
     * Constructs a new RouteOutOfTracksException with the specified error message.
     *
     * @param message The error message.
     */
    public RouteOutOfTracksException(String message) {
        super(message);
    }
}
