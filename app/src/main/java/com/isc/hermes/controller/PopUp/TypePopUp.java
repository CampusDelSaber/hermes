package com.isc.hermes.controller.PopUp;

/**
 * An enum that represents different types of popup dialogs.
 */
public enum TypePopUp {
    DELETE_POP_UP("Are you sure you want to delete your account?",
            "By accepting your data will be permanently deleted",
            "app/src/main/res/drawable/img_delete_icon_blue.png"),
    EDIT_POP_UP("Are you sure you want to overwrite your account data?",
            "By accepting your data will be change",
            "app/src/main/res/drawable/img_edit_icon_blue.png");

    private final String tittlePopUP;
    private final String warningPopUp;
    private final String iconImagePopUp;

    /**
     * Constructs a TypePopUp object with the specified title and warning message.
     *
     * @param tittlePopUp  The title of the popup.
     * @param warningPopUp The warning message of the popup.
     * @param iconImagePopUp The image of the popUp.
     * */
    TypePopUp(String tittlePopUp, String warningPopUp, String iconImagePopUp) {
        this.tittlePopUP = tittlePopUp;
        this.warningPopUp = warningPopUp;
        this.iconImagePopUp = iconImagePopUp;
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

    /**
     * Returns the path of the popup icon.
     *
     * @return The image path of the icon popup.
     */
    public String getIconImagePopUp() {
        return iconImagePopUp;
    }
}
