package com.isc.hermes.controller;

import com.isc.hermes.database.collection.Incidents;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

public class IncidentsGetterController {
    private static final double BASE_RADIUS = 1000.0; // Base radius in meters
    private static final int MAX_ZOOM = 18; // Maximum supported zoom level
    private static final double MIN_DISTANCE = 10.0; // Minimum distance in kilometers


    public void getIncidentsWithinRadius(MapboxMap mapboxMap) {
        LatLng cameraFocus = mapboxMap.getCameraPosition().target;
        int zoom = (int) mapboxMap.getCameraPosition().zoom;

        double radius = calculateRadiusFromZoom(zoom);

        Date currentDate = new Date();

        Bson filter = Filters.and(
                Filters.nearSphere(
                        "geometry", toPoint(cameraFocus), radius / 6371000,
                        MIN_DISTANCE), // Divide by Earth's radius in meters (approx. 6371000)
                Filters.gt("deathDate", currentDate)
        );

        MongoCollection<Document> collection = Incidents.getInstance().getCollection();
        FindIterable<Document> documents = collection.find(filter);

        MongoCursor<Document> cursor = documents.iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(document.toJson());
        }

        cursor.close();
    }

    private double calculateRadiusFromZoom(int zoom) {
        double baseRadius = BASE_RADIUS;
        int maxZoom = MAX_ZOOM;

        return baseRadius * Math.pow(2, (maxZoom - zoom));
    }

    private Point toPoint(LatLng latLng) {
        Position position = new Position(latLng.getLongitude(), latLng.getLatitude());
        return new Point(position);
    }
}
