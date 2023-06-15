package com.isc.hermes.controller.incidents;

import android.content.Context;

import com.isc.hermes.R;
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
        for (int i = 0; i < pointList.getIncidentList().size(); i++) {
            System.out.println("COMO EEEEEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSSSS" +  pointList.getIncidentList().size());
            LatLng pointCoordinates = pointList.getIncidentList().get(i).getPointCoordinates();
            IconFactory iconFactory = IconFactory.getInstance(context);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.mapbox_marker_icon_default);
            drawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            Icon icon = iconFactory.fromBitmap(bitmap);

            MarkerOptions waypoint = new MarkerOptions()
                    .position(pointCoordinates)
                    .icon(icon);

            if (pointList.getIncidentList().get(i).getType().equals("Accident")){
                mapboxMap.addMarker(waypoint);
            }else if (pointList.getIncidentList().get(i).getType().equals("Social-Event")){
                mapboxMap.addMarker(waypoint);
            } else if (pointList.getIncidentList().get(i).getType().equals("Street obstruction")) {
                mapboxMap.addMarker(waypoint);
            }
        }
    }

}