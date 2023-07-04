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
import com.isc.hermes.model.navigation.directions.DirectionsParser;
import com.isc.hermes.model.navigation.directions.DirectionsRecord;
import com.isc.hermes.model.navigation.route_segments.RouteSegmentRecord;
import com.isc.hermes.utils.Animations;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    private DirectionsParser directionsParser;

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
        directionsParser = new DirectionsParser();
    }

    /**
     * Sets the close button on click listeners actions
     */
    private void setButtonsEvent(){
        closeButton.setOnClickListener(event -> {
            directionsForm.startAnimation(Animations.exitAnimation);
            closeDirectionsForm();
        });
    }

    /**
     * This method hide the view.
     */
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
        System.out.println("UPDATE");
        direction.setText(next.getDirection().getText());
        streetName.setText(current.getStreetName());
        setDirectionIcon(next.getDirection());
    }

    public void update(LatLng start, LatLng end) {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("COMO ES KARISIRIIIIIIIIIIIII");
            // Convert LatLng coordinates to Mapbox Point
            Point startPoint = Point.fromLngLat(start.getLongitude(), start.getLatitude());
            Point endPoint = Point.fromLngLat(end.getLongitude(), end.getLatitude());

            System.out.println("COMO ES KARISIRIIIIIIIIIIIII  1");

            // Retrieve the instructions using Mapbox Geocoder
            String startFeatures = reverseGeocode(startPoint);
            String endFeatures = reverseGeocode(endPoint);

            System.out.println("COMO ES KARISIRIIIIIIIIIIIII  2");

            String startStreetName = startFeatures.isEmpty() ? "" : startFeatures;
            String endStreetName = endFeatures.isEmpty() ? "" : endFeatures;

            System.out.println("COMO ES KARISIRIIIIIIIIIIIII  3");
            System.out.println(startStreetName);
            System.out.println(endStreetName);

            System.out.println("COMO ES KARISIRIIIIIIIIIIIII  4");

            System.out.println("COMO ES KARISIRIIIIIIIIIIIII  5");

            return endStreetName; // Return the endStreetName as the result
        }).thenAccept(endStreetName -> {
            ((AppCompatActivity) context).runOnUiThread(() -> {
                DirectionEnum directionEnum = directionsParser.determineDirection(
                        start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude()
                );
                direction.setText(directionEnum.getText());
                streetName.setText(endStreetName);
                setDirectionIcon(directionEnum);
            });
        });
    }

    private DirectionEnum getTurnDirection(LatLng start, LatLng end) {
        double bearing = calculateBearing(start, end);

        // Define the threshold angles for determining the turn direction
        double leftTurnThreshold = 45.0;
        double rightTurnThreshold = 315.0;

        if (bearing >= leftTurnThreshold && bearing < rightTurnThreshold) {
            return DirectionEnum.TURN_LEFT;
        } else {
            return DirectionEnum.TURN_RIGHT;
        }
    }

    private double calculateBearing(LatLng start, LatLng end) {
        double startLat = Math.toRadians(start.getLatitude());
        double startLng = Math.toRadians(start.getLongitude());
        double endLat = Math.toRadians(end.getLatitude());
        double endLng = Math.toRadians(end.getLongitude());

        double deltaLng = endLng - startLng;

        double y = Math.sin(deltaLng) * Math.cos(endLat);
        double x = Math.cos(startLat) * Math.sin(endLat) - Math.sin(startLat) * Math.cos(endLat) * Math.cos(deltaLng);

        double bearing = Math.atan2(y, x);
        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;

        return bearing;
    }

    private String reverseGeocode(Point point) {
        try {
            String apiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat={latitude}&lon={longitude}";
            apiUrl = apiUrl.replace("{latitude}", String.valueOf(point.latitude()));
            apiUrl = apiUrl.replace("{longitude}", String.valueOf(point.longitude()));

            System.out.println(apiUrl);
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            JSONObject jsonObject = new JSONObject(response);

            // Get the street name from the address object
            String streetName = jsonObject.getString("display_name");

            // Return the street name
            return streetName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method set the image of the indication.
     *
     * @param directionEnum is the indication type.
     */
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
