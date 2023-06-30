package com.isc.hermes.controller.PopUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import com.isc.hermes.R;
import com.isc.hermes.controller.IncidentFormController;

public class PopUpConfirmIncidentCanceling extends PopUp{

    /**
     * Confirmation Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUpConfirmIncidentCanceling(Activity activity) {
        super(activity, new PopUpStyle() {
            @Override
            public String getTittlePopUP() {
                return "Incident Canceling Confirmation"; }
            @Override
            public String getWarningPopUp() {
                return "Are you sure you want to cancel the report of this Incident?"; }
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
        super.confirmButton.setText("Yes");
        super.betterNotButton.setText("No");
    }

    /**
     * Handles the click event for the view.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == super.confirmButton) {
            IncidentFormController.getInstance(null, null).handleCancelButtonClick();
        } dismiss();
    }
}
