package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User.EmailVerificationRunnable;
import com.isc.hermes.model.User.UserRelatedThreadManager;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Utils.DataAccountOffline;

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
    private TextInputLayout textInputLayout;
    private boolean isModifiable;
    private boolean typeUserIsSame;

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
            updateDataUser();
            if (!typeUserIsSame) {
                updateTypeUser();
            }
            disableOptions();
        } dismiss();
    }

    /**
     * Disables the options in the account information activity.
     * This method makes the specified components invisible or disabled, preventing user interaction.
     */
    public void disableOptions() {
        button.setVisibility(View.INVISIBLE);
        fullName.setEnabled(false);
        username.setEnabled(false);
        comboBoxField.setEnabled(false);
        textInputLayout.setEnabled(false);
        buttonUploadImage.setVisibility(View.GONE);
    }

    /**
     * This method saves the changes of the account information in the database.
     */
    private void updateDataUser(){
        UserRepository.getInstance().getUserContained().setUserName(String.valueOf(username.getText()));
        UserRepository.getInstance().getUserContained().setFullName(String.valueOf(fullName.getText()));

        AccountInfoManager manager = new AccountInfoManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            manager.editUser(UserRepository.getInstance().getUserContained());
        DataAccountOffline.getInstance(activity).saveDataLoggedAccount(UserRepository.getInstance().getUserContained());
    }

    /**
     * Updates the type of user if the user's type is "Administrator" and the object is not modifiable.
     * It sends an email and starts the EmailVerificationActivity for further verification.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTypeUser() {
        UserRelatedThreadManager.getInstance().doActionForThread(new EmailVerificationRunnable());
        Intent intent = new Intent(activity, EmailVerificationActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Sets the information to be edited in the AbleEdit form.
     *
     * @param button              The button used for editing the information.
     * @param fullName            The AutoCompleteTextView for entering the full name.
     * @param username            The AutoCompleteTextView for entering the username.
     * @param comboBoxField       The AutoCompleteTextView for selecting a field from a combo box.
     * @param buttonUploadImage   The button used for uploading an image.
     * @param textInputLayout     The TextInputLayout that contains the AutoCompleteTextViews.
     */
    public void setInformationToAbleEdit(Button button, AutoCompleteTextView fullName, AutoCompleteTextView username,
                                         AutoCompleteTextView comboBoxField, Button buttonUploadImage,
                                         TextInputLayout textInputLayout, boolean typeUserIsSame) {
        this.button = button;
        this.fullName = fullName;
        this.username = username;
        this.comboBoxField = comboBoxField;
        this.buttonUploadImage = buttonUploadImage;
        this.textInputLayout = textInputLayout;
        this.typeUserIsSame = typeUserIsSame;
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
