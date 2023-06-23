package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.isc.hermes.R;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User;

/**
 * The class {@code PopUpEditAccount} extends {@code PopUp} and represents a specific type of pop-up
 * for editing an account. It provides functionality for editing account information.
 */
public class PopUpOverwriteInformationAccount extends PopUp{

    private Button button;
    private AutoCompleteTextView fullName;
    private AutoCompleteTextView username;
    private AutoCompleteTextView comboBoxField;
    private User userToUpdateInformation;

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity  The activity we were in before the popup opened
     */
    public PopUpOverwriteInformationAccount(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Are you sure you want to overwrite your account data?"; }
            @Override
            public String getWarningPopUp() {
                return "By accepting your data will be change"; }
            @Override
            public int getIconImagePopUp() {
                return R.drawable.img_edit_icon_blue; }
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
            updateUserInformation();
            button.setVisibility(View.INVISIBLE);
            fullName.setEnabled(false);
            username.setEnabled(false);
            comboBoxField.setEnabled(false);
        } dismiss();
    }

    /**
     * Updates the user information.
     * This method is responsible for updating the user information by calling the `editUser()` method
     * of the `AccountInfoManager` class.
     *
     * @throws UnsupportedOperationException if the device's SDK version is lower than Android Oreo.
     */
    private void updateUserInformation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            new AccountInfoManager().editUser(userToUpdateInformation);
    }

    /**
     * Sets the information to be edited in the AbelEdit form.
     *
     * @param button The button used for editing the information.
     * @param fullName The AutoCompleteTextView for entering the full name.
     * @param username The AutoCompleteTextView for entering the username.
     * @param comboBoxField The AutoCompleteTextView for selecting a field from a combo box.
     * @param user The user object containing the user's information to be edited.
     */
    public void setInformationToAbelEdit(Button button, AutoCompleteTextView fullName, AutoCompleteTextView username,
                                         AutoCompleteTextView comboBoxField, User user) {
        this.button = button;
        this.fullName = fullName;
        this.username = username;
        this.comboBoxField = comboBoxField;
        this.userToUpdateInformation = user;
    }
}
