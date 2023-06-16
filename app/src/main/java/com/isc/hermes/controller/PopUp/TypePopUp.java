package com.isc.hermes.controller.PopUp;

/**
 * An enum that represents different types of popup dialogs.
 */
public enum TypePopUp {
    DELETE_POP_UP("Are you sure you want to delete your account?",
            "By accepting your data will be permanently deleted"),
    EDIT_POP_UP("Are you sure you want to overwrite your account data?",
            "By accepting your data will be change");

    private final String tittlePopUP;
    private final String warningPopUp;

    /**
     * Constructs a TypePopUp object with the specified title and warning message.
     *
     * @param tittlePopUp  The title of the popup.
     * @param warningPopUp The warning message of the popup.
     * */
    TypePopUp(String tittlePopUp, String warningPopUp) {
        this.tittlePopUP = tittlePopUp;
        this.warningPopUp = warningPopUp;
    }

    /**
     * Returns the title of the popup.
     *
     * @return The title of the popup.
     */
    public String getTittlePopUP() {
        return tittlePopUP;
    }

    /**
     * Returns the warning message of the popup.
     *
     * @return The warning message of the popup.
     */
    public String getWarningPopUp() {
        return warningPopUp;
    }
}
