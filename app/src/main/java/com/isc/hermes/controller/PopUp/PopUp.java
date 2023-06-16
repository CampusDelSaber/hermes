package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.isc.hermes.R;
import com.isc.hermes.controller.authentication.GoogleAuthentication;

/**
 * This is the controller class for the notification popup when we try to delete our account
 * It extends from the Dialog class
 * It implements a listener which will listen to the option we choose.
 */
public abstract class PopUp extends Dialog implements View.OnClickListener {
    protected boolean status;
    protected Button confirmButton;
    protected TextView deleteAccountText;
    protected TextView warningText;
    protected final Activity activity;
    protected GoogleAuthentication googleAuthentication;

    /**
     * Warning Popup constructor class within which the dialog, activity and buttons are initialized
     *
     * @param activity The activity we were in before the popup opened
     */
    public PopUp(Activity activity, TypePopUp typePopUp){
        super(activity);
        this.activity = activity;
        initializeDialog();
        initializeRefuseButton();
        initializeConfirmButton();
        assignValuesToComponents(typePopUp);
    }

    public void assignValuesToComponents(TypePopUp typePopUp) {
        this.googleAuthentication = new GoogleAuthentication();
        this.warningText = findViewById(R.id.warningText);
        this.deleteAccountText = findViewById(R.id.deleteAccountText);
        this.warningText.setText(typePopUp.getWarningPopUp());
        this.deleteAccountText.setText(typePopUp.getTittlePopUP());
    }

    /**
     * This method initializes the dialog
     */
    private void initializeDialog(){
        setContentView(R.layout.popup);
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
        Button refuseButton = findViewById(R.id.betterNotButton);
        refuseButton.setOnClickListener(this);
    }

    public boolean isStatus() {
        return status;
    }

    /**
     * This method verifies the button which we are clicking and performs its corresponding action.
     *
     * @param v The view that was clicked.
     */
    @Override
    public abstract void onClick(View v);
}
