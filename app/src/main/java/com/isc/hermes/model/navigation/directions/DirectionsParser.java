package com.isc.hermes.model.navigation.directions;

import static com.isc.hermes.model.navigation.directions.DirectionEnum.GO_STRAIGHT;
import static com.isc.hermes.model.navigation.directions.DirectionEnum.TURN_LEFT;
import static com.isc.hermes.model.navigation.directions.DirectionEnum.TURN_RIGHT;

import android.location.Address;
import android.location.Geocoder;

import com.isc.hermes.MainActivity;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.List;

public class DirectionsParser {
    private LatLng prevPoint;
    private final Geocoder geocoder;
    private boolean hasAnchor;

    public DirectionsParser() {
        prevPoint = null;
        geocoder = new Geocoder(MainActivity.context);
        hasAnchor = false;
    }

    public boolean hasAnchor() {
        return hasAnchor;
    }

    public void setAnchor(LatLng prevPoint) {
        hasAnchor = true;
        this.prevPoint = prevPoint;
    }

    public DirectionsRecord translate(LatLng point){
        String streetName = getStreetNameForCoordinates(point.getLatitude(), point.getLongitude());
        DirectionEnum direction = determineDirection(prevPoint.getLatitude(), prevPoint.getLongitude(), point.getLatitude(), point.getLongitude());
        prevPoint = point;
        return new DirectionsRecord(streetName, direction);
    }

    /**
     * Retrieves the street name for the given coordinates using a Geocoder.
     *
     * @param latitude  The latitude of the coordinates.
     * @param longitude The longitude of the coordinates.
     * @return The street name associated with the coordinates.
     */
    private String getStreetNameForCoordinates(double latitude, double longitude) {
        String streetName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                streetName = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return streetName;
    }

    /**
     * Determines the direction based on the previous and current coordinates.
     *
     * @param prevLatitude  The latitude of the previous coordinate.
     * @param prevLongitude The longitude of the previous coordinate.
     * @param latitude      The latitude of the current coordinate.
     * @param longitude     The longitude of the current coordinate.
     * @return The direction based on the angle between the coordinates.
     */
    public static DirectionEnum determineDirection(double prevLatitude, double prevLongitude, double latitude, double longitude) {
        double angle = Math.atan2(latitude - prevLatitude, longitude - prevLongitude);
        DirectionEnum direction = GO_STRAIGHT;

        if (angle > Math.PI / 2) {
            angle -= Math.PI * 2;
        }

        if (angle > Math.PI / 4 && angle < Math.PI * 3 / 4) {
            direction = TURN_LEFT;
        } else if (angle < -Math.PI / 4 && angle > -Math.PI * 3 / 4) {
            direction = TURN_RIGHT;
        }

        return direction;
    }
}
