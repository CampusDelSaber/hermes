package com.isc.hermes.model.navigation.events;

/**
 * The Publisher interface represents an object that can notify subscribers and manage subscriptions.
 */
public interface Publisher {
    /**
     * Notifies all subscribed subscribers.
     * Implementing classes should define the specific action to be taken when notifying subscribers.
     */
    void doNotify();

    /**
     * Subscribes a subscriber to receive updates.
     *
     * @param subscriber the subscriber to subscribe
     */
    void subscribe(Subscriber subscriber);
}
