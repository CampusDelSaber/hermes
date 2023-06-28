package com.isc.hermes.controller.PopUp;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.isc.hermes.R;

/**
 * This class represents a TextInputPopup, which is a popup dialog for inputting text.
 * It provides methods to create, display, and handle the dialog.
 */
public class TextInputPopup {
    private AlertDialog alertDialog;
    private final DialogListener dialogListener;
    private Button ok;
    private Button close;
    private EditText inputText;

    /**
     * Constructs a new TextInputPopup with the specified activity and dialog listener.
     *
     * @param activity       The activity in which the popup is created.
     * @param dialogListener The listener for dialog events.
     */
    public TextInputPopup(Activity activity, DialogListener dialogListener) {
        createTextInputPopup(activity);
        this.dialogListener = dialogListener;
    }

    /**
     * Creates the TextInputPopup by inflating the layout, setting up the dialog, and initializing its elements.
     *
     * @param activity The activity in which the popup is created.
     */
    private void createTextInputPopup(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.name_a_region_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        initializeElements(view);
        builder.setView(view);
        alertDialog = builder.create();
    }

    /**
     * Initializes the elements of the TextInputPopup, such as inputText, close, and ok buttons.
     *
     * @param view The root view of the dialog layout.
     */
    private void initializeElements(View view) {
        inputText = view.findViewById(R.id.inputName);
        close = view.findViewById(R.id.cancel_name_region_popup);
        ok = view.findViewById(R.id.ok_name_region_popup);
        setOnClickListener();
    }

    /**
     * Sets the click listeners for the close and ok buttons.
     */
    private void setOnClickListener() {
        close.setOnClickListener(v -> closePopup());
        ok.setOnClickListener(v -> validateInput());
    }

    /**
     * This method validates the input text from the TextInputPopup.
     */
    private void validateInput() {
        String text = inputText.getText().toString().trim();
        if (TextUtils.isEmpty(text) || validateSpaceInput(text)) {
            setErrorMessage("This field is required");
        } else if (text.length() > 15) {
            setErrorMessage("Max 15 characters allowed");
        } else {
            handleValidInput(text);
        }
    }

    /**
     * This method validates if the string inserted is filled for all spaces
     *
     * @return boolean validate
     */
    private boolean validateSpaceInput(String regionName) {
        return TextUtils.isEmpty(regionName.replace(" ",""));
    }

    /**
     * This method handles the valid input text by invoking the dialogClosed method on the dialogListener
     * or closing the popup if no listener is set.
     *
     * @param text The valid input text.
     */
    private void handleValidInput(String text) {
        if (dialogListener != null) {
            dialogListener.dialogClosed(text);
        } else {
            closePopup();
        }
    }

    /**
     * This method closes the TextInputPopup.
     */
    public void closePopup() {
        alertDialog.dismiss();
    }

    /**
     * Shows the TextInputPopup.
     */
    public void showPopup() {
        alertDialog.show();
    }

    /**
     * Sets an error message for the input field and clears the text.
     *
     * @param errorMessage The error message to be displayed.
     */
    public void setErrorMessage(String errorMessage) {
        inputText.setError(errorMessage);
        inputText.setText("");
    }
}
