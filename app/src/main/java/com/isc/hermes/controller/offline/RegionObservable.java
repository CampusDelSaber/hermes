package com.isc.hermes.controller.offline;

/**
 * This interface defines the contract for an object that can be observed by RegionObservers.
 * It provides methods to add observers and notify them of changes.
 */
public interface RegionObservable {
    /**
     * Adds a RegionObserver to the observer list.
     *
     * @param observer The RegionObserver to be added.
     */
    void addObserver(RegionObserver observer);

    /**
     * Notifies all registered observers of a change in the region.
     */
    void notifyObservers();
}