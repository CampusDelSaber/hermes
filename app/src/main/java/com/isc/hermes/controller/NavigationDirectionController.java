package com.isc.hermes.controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.isc.hermes.utils.Animations;

/**
 * Class to get the turn by turn directions from the route chosen
 */
public class NavigationDirectionController {
    private final Context context;
    private final RelativeLayout directionsForm;
    private TextView directionsList;
    private Button closeButton;

    /**
     * Constructor for the NavigationDirectionController class.
     *
     * @param context               The application context.
     */
    public NavigationDirectionController(Context context) {
        this.context = context;
        directionsForm = ((AppCompatActivity) context).findViewById(R.id.directions_form);
        directionsList = ((AppCompatActivity) context).findViewById(R.id.directions_list);
        closeButton = ((AppCompatActivity) context).findViewById(R.id.close_button);
        setButtonsEvent();
    }

    /**
     * Sets the start navigation and recalculate buttons on click listeners actions
     */
    private void setButtonsEvent(){
        closeButton.setOnClickListener(event -> {
            directionsForm.startAnimation(Animations.exitAnimation);
            closeDirectionsForm();
        });
    }

    private void closeDirectionsForm() {
        directionsForm.setVisibility(View.GONE);
    }

    /**
     * Gets the directions form layout.
     *
     * @return The directions form layout.
     */
    public RelativeLayout getDirectionsForm() {
        return directionsForm;
    }

    public void update(DirectionsRecord current, DirectionsRecord next){
        //
    }
}
