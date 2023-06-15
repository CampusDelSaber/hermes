package com.isc.hermes.controller.incidents;

import android.content.Context;

import com.isc.hermes.R;
import com.isc.hermes.model.incidents.Incident;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import com.isc.hermes.model.incidents.IncidentGetterModel;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class IncidentPointVisualizationController {

    private final MapboxMap mapboxMap;
    private final Context context;
    IncidentGetterModel pointList = new IncidentGetterModel();

    public IncidentPointVisualizationController(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        try {
            displayPoint(pointList);
        } catch (JSONException e){
            Toast.makeText(context, R.string.incidents_uploaded, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method creates and displays waypoints on the map.
     * @param pointList list of places that will be marked on the map.
     */
    public void displayPoint(IncidentGetterModel pointList) throws JSONException {
        IconFactory iconFactory = IconFactory.getInstance(context);

        for (Incident incident : pointList.getIncidentList()) {
            LatLng pointCoordinates = incident.getPointCoordinates();
            Icon icon = createIcon();

            MarkerOptions waypoint = createMarkerOptions(pointCoordinates, icon);

            if (isDesiredIncidentType(incident.getType())) {
                mapboxMap.addMarker(waypoint);
            }
        }
    }

    private Icon createIcon() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.mapbox_marker_icon_default);
        drawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        Bitmap bitmap = createBitmapFromDrawable(drawable);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    private Bitmap createBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private MarkerOptions createMarkerOptions(LatLng position, Icon icon) {
        return new MarkerOptions()
                .position(position)
                .icon(icon);
    }

    private boolean isDesiredIncidentType(String incidentType) {
        Set<String> desiredTypes = new HashSet<>(Arrays.asList("Accident", "Social-Event", "Street obstruction"));
        return desiredTypes.contains(incidentType);
    }

}