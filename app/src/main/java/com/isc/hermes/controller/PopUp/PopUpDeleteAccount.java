package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.isc.hermes.R;
import com.isc.hermes.SignUpActivityView;
import com.isc.hermes.database.AccountInfoManager;

/**
 * A class that represents a popup dialog for deleting an account.
 * Extends the PopUp class.
 */
public class PopUpDeleteAccount extends PopUp{

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUpDeleteAccount(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Are you sure you want to delete your account?"; }
            @Override
            public String getWarningPopUp() {
                return "By accepting your data will be permanently deleted"; }
            @Override
            public int getIconImagePopUp() {
                return R.drawable.img_delete_icon_blue; }
        });
    }

    /**
     * Closes the authentication process and redirects the user to the sign-up activity.
     */
    private void closeAuthentication() {
        googleAuthentication.revokeAccess(getContext());
        Intent intent = new Intent(this.activity, SignUpActivityView.class);
        activity.startActivity(intent);
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton){
            AccountInfoManager accountInfoManager = new AccountInfoManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                accountInfoManager.deleteUser(UserRepository.getInstance().getUserContained().getId());
            } closeAuthentication();
        } dismiss();
    }
}
