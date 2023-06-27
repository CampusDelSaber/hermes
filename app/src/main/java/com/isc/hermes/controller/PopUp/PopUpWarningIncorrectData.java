package com.isc.hermes.controller.PopUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User.UserRepository;

/**
 * The class extends {@code PopUp} and represents a specific type of popup window. In case the end
 * user does not enter any verification number and wants to enter, this popup will appear
 */
public class PopUpWarningIncorrectData extends PopUp{

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUpWarningIncorrectData(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "The provided code is invalid"; }
            @Override
            public String getWarningPopUp() {
                return "The error is due to the fact that the data has been entered incorrectly"; }
            @Override
            public int getIconImagePopUp() {
                return R.drawable.baseline_error_24; }
        });
        updateButtons();
    }


    /**
     * Updates the buttons on the screen.
     * Sets the X position and text of the confirmButton,
     * and makes the betterNotButton invisible.
     */
    @SuppressLint("SetTextI18n")
    private void updateButtons() {
        super.confirmButton.setX(170);
        super.confirmButton.setText("Okay!");
        super.betterNotButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton) dismiss();
    }
}
