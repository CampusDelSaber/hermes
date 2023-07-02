package com.isc.hermes.model.navigation.events;

public interface Publisher {

    void notifyRouteTrackUpdated(int routeSegmentIndex);

    void subscribe(Subscriber subscriber);
}
