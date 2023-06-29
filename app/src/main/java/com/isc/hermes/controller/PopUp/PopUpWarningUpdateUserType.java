package com.isc.hermes.controller.PopUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.isc.hermes.AccountInformation;
import com.isc.hermes.R;

public class PopUpWarningUpdateUserType extends PopUp{

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUpWarningUpdateUserType(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Action not allowed for a 'General User'"; }
            @Override
            public String getWarningPopUp() {
                return "To report or generate incidents, please request Administrator user access"; }
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
        super.confirmButton.setText("Okay!");
        super.betterNotButton.setText("Request Admin \nuser access");
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton) {
            Intent intent = new Intent(activity, AccountInformation.class);
            activity.startActivity(intent);
        } dismiss();
    }

}
