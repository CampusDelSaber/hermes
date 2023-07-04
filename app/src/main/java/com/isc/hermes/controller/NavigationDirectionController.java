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

    public void update(LatLng start, LatLng end) {
        CompletableFuture.supplyAsync(() -> {
            Point endPoint = Point.fromLngLat(end.getLongitude(), end.getLatitude());
            String endFeatures = reverseGeocode(endPoint);
            return endFeatures.isEmpty() ? "" : endFeatures;
        }).thenAccept(endStreetName -> ((AppCompatActivity) context).runOnUiThread(() -> {
            DirectionEnum directionEnum = directionsParser.determineDirection(
                    start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude()
            );
            direction.setText(directionEnum.getText());
            streetName.setText(endStreetName);
            setDirectionIcon(directionEnum);
        }));
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
