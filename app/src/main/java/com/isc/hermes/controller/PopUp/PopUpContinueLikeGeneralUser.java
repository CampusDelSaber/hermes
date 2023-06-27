package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User.UserRepository;

/**
 * The {@code PopUpContinueLikeGeneralUser} class extends {@code PopUp} and represents a specific
 * type of popup window. so that the end user can continue as a general user. It provides
 * functionality to be a general user.
 */
public class PopUpContinueLikeGeneralUser extends PopUp{

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUpContinueLikeGeneralUser(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Are you sure you don't want to verify your account?"; }
            @Override
            public String getWarningPopUp() {
                return "By agreeing to be registered as a general user until you enter the verification code."; }
            @Override
            public int getIconImagePopUp() {
                return R.drawable.baseline_manage_accounts_24; }
        });
    }

    /**
     * Uploads the general user information to the database.
     * Sets the user type to "General" and adds the user to the AccountInfoManager.
     */
    private void uploadGeneralUserToDB() {
        UserRepository.getInstance().getUserContained().setTypeUser("General");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new AccountInfoManager().addUser(UserRepository.getInstance().getUserContained().getEmail(),
                    UserRepository.getInstance().getUserContained().getFullName(),
                    UserRepository.getInstance().getUserContained().getUserName(),
                    "General", UserRepository.getInstance().getUserContained().getPathImageUser());
        }
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton){
            Intent intent = new Intent(this.activity, MainActivity.class);
            uploadGeneralUserToDB();
            this.activity.startActivity(intent);
        } dismiss();
    }
}
