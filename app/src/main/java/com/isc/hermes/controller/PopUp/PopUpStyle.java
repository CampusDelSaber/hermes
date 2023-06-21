package com.isc.hermes.controller.PopUp;

/**
 * The {@code PopUpStyle} interface defines methods for obtaining various properties
 * related to the appearance and content of pop-up windows or notifications.
 */
public interface PopUpStyle {

    /**
     * Returns the title of the pop-up window.
     *
     * @return the title of the pop-up window
     */
    String getTittlePopUP();

    /**
     * Returns the warning message for the pop-up window.
     *
     * @return the warning message for the pop-up window
     */
    String getWarningPopUp();

    /**
     * Returns the path or name of the icon image to be displayed in the pop-up window.
     *
     * @return the xml of the icon image for the pop-up window
     */
    int getIconImagePopUp();
}
