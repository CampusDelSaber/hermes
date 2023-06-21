package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/**
 * The class {@code PopUpEditAccount} extends {@code PopUp} and represents a specific type of pop-up
 * for editing an account. It provides functionality for editing account information.
 */
public class PopUpEditAccount extends PopUp{

    private Button button;
    private AutoCompleteTextView fullName;
    private AutoCompleteTextView username;
    private AutoCompleteTextView comboBoxField;

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity  The activity we were in before the popup opened
     */
    public PopUpEditAccount(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Are you sure you want to overwrite your account data?"; }
            @Override
            public String getWarningPopUp() {
                return "By accepting your data will be change"; }
            @Override
            public String getIconImagePopUp() {
                return "app/src/main/res/drawable/img_edit_icon_blue.png"; }
        });
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton) {
            button.setVisibility(View.INVISIBLE);
            fullName.setEnabled(false);
            username.setEnabled(false);
            comboBoxField.setEnabled(false);
        } dismiss();
    }

    /**
     * Sets the button and text fields for the account edit.
     *
     * @param button    The button for account editing.
     * @param fullName  The text field for the full name.
     * @param username  The text field for the username.
     * @param comboBoxField The text field fo combo-box.
     */
    public void setInformationToAbelEdit(Button button, AutoCompleteTextView fullName,
                                         AutoCompleteTextView username, AutoCompleteTextView comboBoxField) {
        this.button = button;
        this.fullName = fullName;
        this.username = username;
        this.comboBoxField = comboBoxField;
    }
}
