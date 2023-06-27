package com.isc.hermes.controller.offline;

/**
 * This interface represents a RegionObserver, which defines the contract for an object that observes changes in a region.
 */
public interface RegionObserver {
    /**
     * This method is called to notify the observer of an update in the region.
     */
    void update();
}