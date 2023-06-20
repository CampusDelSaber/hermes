package com.isc.hermes.controller.PopUp;

/**
 * This interface represents a DialogListener, which defines the contract for an object that listens to dialog events.
 */
public interface DialogListener {
    /**
     * This method is called when a dialog is closed.
     *
     * @param text The text received from the dialog.
     */
    void dialogClosed(String text);
}