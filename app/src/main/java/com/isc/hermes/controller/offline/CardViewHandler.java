package com.isc.hermes.controller.offline;

import java.util.ArrayList;
import java.util.List;
/**
 * This class implements the RegionObservable interface and serves as a handler for CardView-related operations.
 */
public class CardViewHandler implements RegionObservable {
    private static CardViewHandler cardViewHandler;

    /**
     * This method returns the singleton instance of CardViewHandler.
     *
     * @return The singleton instance of CardViewHandler.
     */
    public static CardViewHandler getInstance() {
        if (cardViewHandler == null) {
            cardViewHandler = new CardViewHandler();
        }
        return cardViewHandler;
    }

    private List<RegionObserver> observers;

    /**
     * This method adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(RegionObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    /**
     * This method notifies all observers of a region-related event.
     */
    @Override
    public void notifyObservers() {
        if (observers == null) {
            return;
        }
        observers.forEach(RegionObserver::update);
    }
}

