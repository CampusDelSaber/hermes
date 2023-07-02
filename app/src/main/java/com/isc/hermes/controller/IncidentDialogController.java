package com.isc.hermes.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;
import com.isc.hermes.R;
import com.isc.hermes.model.incidents.IncidentGetterModel;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;

public class IncidentDialogController {
    private static IncidentDialogController instance;
    private final Context context;
    private Dialog dialog;
    private IncidentGetterModel incidents = new IncidentGetterModel();
    private final int MAX_CHARACTERS = 25;
    private final String REGEX_SPACES = ".*\\s{3,}.*";

    private IncidentDialogController(Context context) {
        this.context = context;
    }

    public void showDialogCorrect(LatLng clickedLatLng) throws JSONException {
        for (int i = 0; i < incidents.getIncidentList().size(); i++) {
            if(incidents.getIncidentList().get(i).getGeometry().equals(clickedLatLng)){
                show(incidents.getIncidentList().get(i).getType(),incidents.getIncidentList().get(i).getReason(),incidents.getIncidentList().get(i).getDeathDate().toString(), (incidents.getIncidentList().get(i).getPointCoordinates()).toString());
                break;
            }
        }
    }
    public void show(String title, String reason, String waitTime, String coordinates) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.incidents_dialog);
        dialog.setCancelable(true);

        TextView titleTextView = dialog.findViewById(R.id.titleIncidentDialog);
        TextView reasonTextView = dialog.findViewById(R.id.reasonTextIncidentDialog);
        TextView waitTimeTextView = dialog.findViewById(R.id.waitTimeTextIncidentDialog);
        TextView coordinatesTextView = dialog.findViewById(R.id.coordinatesTextIncidentDialog);

        titleTextView.setText(title);
        reasonTextView.setText(reason);
        waitTimeTextView.setText(waitTime);
        coordinatesTextView.setText(coordinates);

        Button closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * This method validates an incident input text
     *
     * @param text the text to be validated
     * @return if the text is correct or not
     */
    public boolean validateInput(String text) {
        boolean textCorrect = true;
        if (text.length() > MAX_CHARACTERS){
            textCorrect = false;
            showAlertDialog("This description is too long", "You surpassed the 25 character limit.");
        } else if (text.length() == 0) {
            textCorrect = false;
            showAlertDialog("The description is empty", "Please write a correct description.");
        } else if (text.matches(REGEX_SPACES)) {
            textCorrect = false;
            showAlertDialog("The description contains only spaces", "Please write a correct description.");
        }
        return textCorrect;
    }

    /**
     * This method shows an dialog alert
     *
     * @param title the title of the dialog
     * @param message the message of the dialog
     */
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static synchronized IncidentDialogController getInstance(Context context) {
        if (instance == null) {
            instance = new IncidentDialogController(context);
        }
        return instance;
    }

}