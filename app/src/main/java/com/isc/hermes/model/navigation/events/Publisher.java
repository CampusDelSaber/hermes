package com.isc.hermes.model.navigation.events;

public interface Publisher {
    void doNotify();
    void subscribe(Subscriber subscriber);
}
