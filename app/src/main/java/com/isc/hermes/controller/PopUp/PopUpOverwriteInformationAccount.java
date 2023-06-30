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
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.database.SendEmailManager;
import com.isc.hermes.model.User.User;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Validator;
import com.isc.hermes.model.signup.SignUpTransitionHandler;

/**
 * The class {@code PopUpEditAccount} extends {@code PopUp} and represents a specific type of pop-up
 * for editing an account. It provides functionality for editing account information.
 */
public class PopUpOverwriteInformationAccount extends PopUp{

    private Button button;
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
     * @param v The view that was clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton) {
            updateTypeUser();
            button.setVisibility(View.INVISIBLE);
            fullName.setEnabled(false);
            username.setEnabled(false);
            comboBoxField.setEnabled(false);
        } dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTypeUser() {
        if (UserRepository.getInstance().getUserContained().getTypeUser().
                equals("Administrator") && !isModifiable) {
            UserRepository.getInstance().getUserContained().setTypeUser("Administrator");
            Validator validator = new Validator(UserRepository.getInstance().getUserContained());
            validator.obtainVerificationCode();
            SendEmailManager sendEmailManager = new SendEmailManager();
            sendEmailManager.addEmail(UserRepository.getInstance().getUserContained().getEmail(),
                    validator.getCode());
            Intent intent = new Intent(activity, EmailVerificationActivity.class);
            activity.startActivity(intent);
        }
    }

    /**
     * Sets the information to be edited in the AbelEdit form.
     *
     * @param button The button used for editing the information.
     * @param fullName The AutoCompleteTextView for entering the full name.
     * @param username The AutoCompleteTextView for entering the username.
     * @param comboBoxField The AutoCompleteTextView for selecting a field from a combo box.
     */
    public void setInformationToAbelEdit(Button button, AutoCompleteTextView fullName, AutoCompleteTextView username,
                                         AutoCompleteTextView comboBoxField) {
        this.button = button;
        this.fullName = fullName;
        this.username = username;
        this.comboBoxField = comboBoxField;
    }

    public void setIsModifiable(boolean modifiable) {
        isModifiable = modifiable;
    }
}