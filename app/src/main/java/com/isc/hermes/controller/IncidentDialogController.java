package com.isc.hermes.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.isc.hermes.R;
import com.isc.hermes.model.incidents.IncidentGetterModel;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;

public class IncidentDialogController {
    private static IncidentDialogController instance;
    private final Context context;
    private Dialog dialog;
    private IncidentGetterModel incidents = new IncidentGetterModel();

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

    public static synchronized IncidentDialogController getInstance(Context context) {
        if (instance == null) {
            instance = new IncidentDialogController(context);
        }
        return instance;
    }

}