package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.isc.hermes.R;
import com.isc.hermes.controller.authentication.GoogleAuthentication;

/**
 * The abstract class {@code PopUp} extends {@code Dialog} and implements {@code View.OnClickListener},
 * providing functionality for displaying a pop-up dialog in an Android application.
 */
public abstract class PopUp extends Dialog implements View.OnClickListener {
    protected boolean status;
    protected Button confirmButton;
    protected TextView deleteAccountText;
    protected TextView warningText;
    protected ImageView iconMessagePopUp;
    protected GoogleAuthentication googleAuthentication;
    protected final Activity activity;

    /**
     * Constructs a Warning Popup with the provided activity and pop-up style.
     *
     * @param activity The activity that was in use before the pop-up was opened.
     * @param typePopUp The PopUpStyle object specifying the style and content of the pop-up.
     */
    public PopUp(Activity activity, PopUpStyle typePopUp){
        super(activity);
        this.activity = activity;
        initializeDialog();
        initializeRefuseButton();
        initializeConfirmButton();
        assignValuesToComponents(typePopUp);
    }

    /**
     * Assigns values to the components based on the provided PopUpStyle.
     *
     * @param typePopUp The PopUpStyle object containing the values to be assigned.
     */
    public void assignValuesToComponents(PopUpStyle typePopUp) {
        this.googleAuthentication = new GoogleAuthentication();
        this.warningText = findViewById(R.id.warningText);
        this.iconMessagePopUp = findViewById(R.id.iconMessagePopUp);
        this.deleteAccountText = findViewById(R.id.deleteAccountText);
        this.iconMessagePopUp.setImageURI(Uri.parse(typePopUp.getIconImagePopUp()));
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
