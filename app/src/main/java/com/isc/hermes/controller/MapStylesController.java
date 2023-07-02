package com.isc.hermes.controller;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.model.Utils.MapStylePreference;
import com.isc.hermes.utils.MapManager;
import com.mapbox.mapboxsdk.maps.Style;

/**
 * Class controller of map styles that handles the styles view and the stored map style.
 */
public class MapStylesController {
    private String mapStyle;
    private final AppCompatActivity activity;

    /**
     * Constructor method.
     * @param activity the main activity of the app.
     */
    public MapStylesController(AppCompatActivity activity) {
        this.activity = activity;
        this.mapStyle = MapStylePreference.getMapStyle(activity.getBaseContext());
    }

    /**
     * Opens the styles menu by toggling its visibility.
     */
    public void openStylesMenu() {
        LinearLayout styleOptionsWindow = activity.findViewById(R.id.styleOptionsWindow);
        styleOptionsWindow.setVisibility(View.VISIBLE);
    }

    /**
     * Method to set the new map style on the current map.
     *
     * @param view The button's view of the style that has been pressed.
     */
    public void changeMapStyle(View view) {
        LinearLayout styleOptionsWindow = activity.findViewById(R.id.styleOptionsWindow);
        styleOptionsWindow.setVisibility(View.GONE);
        mapStyle = ((ImageButton) view).getTag().toString();
        MapManager.getInstance().getMapboxMap().setStyle(mapStyle);
        updateMapStyle();
    }

    /**
     * Set the current map style into the style utils.
     */
    private void updateMapStyle(){
        MapStylePreference.setMapStyle(activity.getBaseContext(), mapStyle);
    }

    /**
     * Recover the style map if it was set previously.
     *
     * @return the previous style if it was set or a the default style if not.
     */
    public String getMapStyle(){
        if (mapStyle == null) {
            mapStyle = Style.MAPBOX_STREETS;
        }
        return mapStyle;
    }
}
