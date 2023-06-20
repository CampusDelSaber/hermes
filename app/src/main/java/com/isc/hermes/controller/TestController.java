package com.isc.hermes.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.Animations;
import com.isc.hermes.view.IncidentTypeButton;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.text.DecimalFormat;
import timber.log.Timber;


public class TestController {
    private boolean isActive, isLocationStartChosen;
    private final Context context;
    private final RelativeLayout navOptionsForm;
    private final Button cancelButton, startButton, chooseStartPointButton,
            startPointButton, finalPointButton, currentLocationButton;
    private final LinearLayout transportationTypesContainer;
    private final TextView navOptionsText;
    private final MapWayPointController mapWayPointController;
    private LatLng startPoint, finalPoint;


    /**
     * This is the constructor method. Init all the necessary components.
     *
     * @param context Is the context application.
     * @param mapWayPointController Is the controller of the map.
     */
    public TestController(Context context, MapWayPointController mapWayPointController) {
        this.context = context;
        isActive = false;
        isLocationStartChosen = true;
        this.mapWayPointController = mapWayPointController;
        navOptionsForm = ((AppCompatActivity)context).findViewById(R.id.navOptions_form);
        cancelButton = ((AppCompatActivity) context).findViewById(R.id.cancel_navOptions_button);
        startButton = ((AppCompatActivity) context).findViewById(R.id.start_button_nav);
        chooseStartPointButton = ((AppCompatActivity) context).findViewById(R.id.choose_startPoint_button);
        currentLocationButton = ((AppCompatActivity) context).findViewById(R.id.current_location_button);
        startPointButton = ((AppCompatActivity) context).findViewById(R.id.startPoint_button);;
        finalPointButton = ((AppCompatActivity) context).findViewById(R.id.finalPoint_Button);;
        transportationTypesContainer = ((AppCompatActivity) context).findViewById(R.id.transportationTypesContainer);
        navOptionsText = ((AppCompatActivity) context).findViewById(R.id.navOptions_Text);
        setNavOptionsUiComponents();
        setButtons();
    }

    private void setButtons() {
        chooseStartPointButton.setOnClickListener(v -> handleChooseStartPointButton());
        currentLocationButton.setOnClickListener(v -> handleCurrentLocationChosen());
        cancelButton.setOnClickListener(v -> {
            isLocationStartChosen = false;
            handleHiddeItemsView();
            startPoint = null;
            finalPoint = null;
            isActive = false;
        });
    }

    @SuppressLint("SetTextI18n")
    private void handleCurrentLocationChosen() {
        isLocationStartChosen = true;
        startPointButton.setText("Your Location");

    }

    private void handleChooseStartPointButton() {
        isActive = true;
        isLocationStartChosen = false;
        handleHiddeItemsView();
    }

    /**
     * This method handles the actions performed when the cancel button is clicked.
     */
    public void handleHiddeItemsView() {
        mapWayPointController.setMarked(false);
        getNavOptionsForm().startAnimation(Animations.exitAnimation);
        getNavOptionsForm().setVisibility(View.GONE);
        mapWayPointController.deleteMarks();
    }


    /**
     * This method assigns values to the incident components.
     *
     * <p>
     *     This method assign values and views to the incident components such as the incident type
     *     spinner, incident estimated time spinner and incident estimated time number picker.
     * </p>
     */
    public void setNavOptionsUiComponents() {
        String[] navOptionsTypes = context.getResources().getStringArray(R.array.navOptions_type);
        String[] navOptionTypeColors = context.getResources().getStringArray(R.array.navOptions_type_colors);
        String[] navOptionTypeIcons = context.getResources().getStringArray(R.array.navOptions_type_icons);

        if (navOptionsTypes.length == navOptionTypeColors.length &&
                navOptionTypeIcons.length == navOptionsTypes.length) {
            for (int i = 0; i < navOptionsTypes.length; i++) {
                Button button = IncidentTypeButton.getIncidentTypeButton(context, navOptionsTypes[i].toLowerCase(),
                        Color.parseColor((String) navOptionTypeColors[i]), navOptionTypeIcons[i]);
                transportationTypesContainer.addView(button);
            }
        } else {
            Timber.i(String.valueOf(R.string.array_size_text_timber));
        }
    }


    public boolean isStartPointFromMapSelected() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setStartPoint(LatLng point) {
        isLocationStartChosen = false;
        isActive = true;
        startPoint = point;
        updateUiPointsComponents();
    }

    private void updateUiPointsComponents() {
        setPointsButtonShownTexts();
    }

    private String formatLatLng(double latitude, double longitude) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        String formattedLatitude = decimalFormat.format(latitude);
        String formattedLongitude = decimalFormat.format(longitude);
        return "Lt: "+formattedLatitude + "\n" + "Lg: "+formattedLongitude;
    }

    /**
     * This is a getter method to Incident form layout.
     * @return Return a layout.
     */
    public RelativeLayout getNavOptionsForm() {
        return navOptionsForm;
    }
    private void setPointsButtonShownTexts() {
        startPointButton.setText((!isLocationStartChosen)?
                formatLatLng(startPoint.getLatitude(),startPoint.getLongitude()):"Your Location");
        finalPointButton.setText((finalPoint != null)?
                formatLatLng(finalPoint.getLatitude(),finalPoint.getLongitude()):"Not selected");
    }

    public void setFinalNavigationPoint(LatLng point) {
        isActive = true;
        this.finalPoint = point;
        updateUiPointsComponents();
    }
}
