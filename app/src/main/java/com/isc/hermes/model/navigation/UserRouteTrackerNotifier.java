package com.isc.hermes.model.navigation;

import com.isc.hermes.model.navigation.events.Publisher;
import com.isc.hermes.model.navigation.events.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserRouteTrackerNotifier class implements the Publisher interface
 * to notify subscribers about updates in the user's route tracking.
 */
public class UserRouteTrackerNotifier implements Publisher {
    private final List<Subscriber> subscribers;

    /**
     * Constructs a new UserRouteTrackerNotifier object.
     * Initializes the list of subscribers.
     */
    public UserRouteTrackerNotifier() {
        subscribers = new ArrayList<>();
    }

    /**
     * Notifies all subscribed subscribers by calling the update method on each subscriber.
     */
    @Override
    public void doNotify() {
        subscribers.forEach(Subscriber::update);
    }

    /**
     * Subscribes a new subscriber to receive updates.
     *
     * @param subscriber the subscriber to subscribe
     */
    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}
