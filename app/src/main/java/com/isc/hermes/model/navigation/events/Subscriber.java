package com.isc.hermes.model.navigation.events;

/**
 * The Subscriber interface represents an object that can receive updates.
 * Implementing classes must define the behavior for handling updates.
 */
public interface Subscriber {
    /**
     * This method is called to notify the subscriber of an update.
     * Implementing classes should define the specific action to be taken upon receiving an update.
     */
    void update();
}
