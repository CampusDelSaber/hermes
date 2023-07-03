package com.isc.hermes.model.navigation;

import com.isc.hermes.model.navigation.events.Publisher;
import com.isc.hermes.model.navigation.events.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class UserRouteTrackerNotifier implements Publisher {
    private final List<Subscriber> subscribers;

    public UserRouteTrackerNotifier() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void doNotify() {
        subscribers.forEach(Subscriber::update);
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}
