package com.isc.hermes.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import com.isc.hermes.R;
import com.isc.hermes.SignUpActivityView;
import com.isc.hermes.controller.authentication.GoogleAuthentication;

/**
 * This is the controller class for the notification popup when we try to delete our account
 * It extends from the Dialog class
 * It implements a listener which will listen to the option we choose.
 */
public class DeleteAccountWarningDialog extends Dialog implements View.OnClickListener {
    private Button confirmButton;
    private Button refuseButton;
    private Activity activity;
    private GoogleAuthentication googleAuthentication;

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public DeleteAccountWarningDialog(Activity activity){
        super(activity);
        initializeDialog();
        initializeRefuseButton();
        initializeConfirmButton();
        this.activity = activity;
        this.googleAuthentication = new GoogleAuthentication();
    }

    /**
     * This method initializes the dialog
     */
    private void initializeDialog(){
        setContentView(R.layout.delete_account_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * This method initializes the "Im Sure" button in the view
     */
    public void initializeConfirmButton(){
        confirmButton = findViewById(R.id.imSureButton);
        confirmButton.setOnClickListener(this);
    }

    /**
     * This method initializes the "Better Not" button in the view
     */
    public void initializeRefuseButton(){
        refuseButton = findViewById(R.id.betterNotButton);
        refuseButton.setOnClickListener(this);
    }

    /**
     * This method verifies the button which we are clicking and performs its corresponding action.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == confirmButton){
            googleAuthentication.revokeAccess(getContext());
            dismiss();
            Intent intent = new Intent(this.activity, SignUpActivityView.class);
            activity.startActivity(intent);
        }
        else {
            dismiss();
        }
    }
}
