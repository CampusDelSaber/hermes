package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.SendEmailManager;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Validator;

/**
 * The class {@code PopUpEditAccount} extends {@code PopUp} and represents a specific type of pop-up
 * for editing an account. It provides functionality for editing account information.
 */
public class PopUpOverwriteInformationAccount extends PopUp{

    private Button button;
    private Button buttonUploadImage;
    private AutoCompleteTextView fullName;
    private AutoCompleteTextView username;
    private AutoCompleteTextView comboBoxField;
    private boolean isModifiable;

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
        }); isModifiable = true;
    }

    /**
     * Handles the click event for the view.
     *
     * @param view The view that was clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view == super.confirmButton) {
            updateTypeUser();
            button.setVisibility(View.INVISIBLE);
            fullName.setEnabled(false);
            username.setEnabled(false);
            comboBoxField.setEnabled(false);
            buttonUploadImage.setVisibility(View.GONE);
        } dismiss();
    }

    /**
     * Updates the type of user if the user's type is "Administrator" and the object is not modifiable.
     * It sends an email and starts the EmailVerificationActivity for further verification.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTypeUser() {
        if (UserRepository.getInstance().getUserContained().getTypeUser().
                equals("Administrator") && !isModifiable) {
            UserRepository.getInstance().getUserContained().setTypeUser("Administrator");
            sendEmail();
            Intent intent = new Intent(activity, EmailVerificationActivity.class);
            activity.startActivity(intent);
        }
    }

    /**
     * Sends an email containing a verification code to the user's email address.
     * The email is sent using the SendEmailManager class.
     * The verification code is obtained from the Validator class.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendEmail(){
        Validator validator = new Validator(UserRepository.getInstance().getUserContained());
        validator.obtainVerificationCode();
        SendEmailManager sendEmailManager = new SendEmailManager();
        sendEmailManager.addEmail(UserRepository.getInstance().getUserContained().getEmail(),
                validator.getCode());
    }

    /**
     * Sets the information to be edited in the AbelEdit form.
     *
     * @param button The button used for editing the information.
     * @param fullName The AutoCompleteTextView for entering the full name.
     * @param username The AutoCompleteTextView for entering the username.
     * @param comboBoxField The AutoCompleteTextView for selecting a field from a combo box.
     */
    public void setInformationToAbleEdit(Button button, AutoCompleteTextView fullName, AutoCompleteTextView username,
                                         AutoCompleteTextView comboBoxField, Button buttonUploadImage) {
        this.button = button;
        this.fullName = fullName;
        this.username = username;
        this.comboBoxField = comboBoxField;
        this.buttonUploadImage = buttonUploadImage;
    }

    /**
     * Sets the modifiability status of the object.
     *
     * @param modifiable the modifiability status to be set
     */
    public void setIsModifiable(boolean modifiable) {
        isModifiable = modifiable;
    }
}
