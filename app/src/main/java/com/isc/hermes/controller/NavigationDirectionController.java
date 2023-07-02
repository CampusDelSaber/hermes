package com.isc.hermes.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.navigation.directions.DirectionEnum;
import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.isc.hermes.utils.Animations;

/**
 * Class to get the turn by turn directions from the route chosen
 */
public class NavigationDirectionController {
    private final Context context;
    private final RelativeLayout directionsForm;
    private TextView streetName;
    private TextView direction;
    private Button closeButton;
    private ImageView indicationImage;

    /**
     * Constructor for the NavigationDirectionController class.
     *
     * @param context               The application context.
     */
    public NavigationDirectionController(Context context) {
        this.context = context;
        directionsForm = ((AppCompatActivity) context).findViewById(R.id.directions_form_real_time);
        streetName = ((AppCompatActivity) context).findViewById(R.id.street_name);
        direction = ((AppCompatActivity) context).findViewById(R.id.indication_text);
        closeButton = ((AppCompatActivity) context).findViewById(R.id.close_button_2);
        indicationImage = ((AppCompatActivity) context).findViewById(R.id.indication_image);
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
        direction.setText(next.getDirection().getText());
        streetName.setText(current.getStreetName());
        setDirectionIcon(next.getDirection());
    }

    public void setDirectionIcon(DirectionEnum directionEnum) {
        switch (directionEnum) {
            case GO_STRAIGHT:
                indicationImage.setImageResource(R.drawable.go_straight_icon);
                break;
            case TURN_LEFT:
                indicationImage.setImageResource(R.drawable.turn_left_icon);
                break;
            case TURN_RIGHT:
                indicationImage.setImageResource(R.drawable.turn_right_icon);
                break;
        }
    }
}
