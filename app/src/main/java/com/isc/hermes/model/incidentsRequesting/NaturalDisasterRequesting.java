package com.isc.hermes.model.incidentsRequesting;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class to obtain all polygons from database
 */
public class NaturalDisasterRequesting {
    private static final String NATURAL_DISASTER_API_URL = "https://api-rest-hermes.onrender.com/incidents?types=Danger%20Zone,Natural%20Disaster";
    private final OkHttpClient client;
    private Request request;
    private final List<List<Point>> allPolygonPoints;

   public NaturalDisasterRequesting(){
       this.client = new OkHttpClient();
       this.allPolygonPoints = new ArrayList<>();
       initRequest();
       initCallBack();
   }

    /**
     * Method to init requesting action from natural disaster url
     */
   private void initRequest(){
       this.request = new Request.Builder()
               .url(NATURAL_DISASTER_API_URL)
               .build();
   }


    /**
     * Method to init the requesting from client to database
     */
    private void initCallBack() {
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(@NonNull Call call, @NonNull IOException e) {
               e.printStackTrace();
           }

           @Override
           public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               if (!response.isSuccessful()) return;
               String myResponse = response.body().string();
               JsonArray jsonArray = JsonParser.parseString(myResponse).getAsJsonArray();
               addPolygonFromJsonArray(jsonArray);
           }
       });
    }

    /**
     * Method to fill last polygons list from json array
     * @param jsonArray Is json containing all polygons
     */
    private void addPolygonFromJsonArray(JsonArray jsonArray){
        for (JsonElement jsonElement : jsonArray) {
            JsonObject incidentObject = jsonElement.getAsJsonObject();
            JsonObject geometry = incidentObject.get("geometry").getAsJsonObject();
            JsonArray coordinatesArray = geometry.get("coordinates").getAsJsonArray();
            List<Point> polygonCoordinates = fillOnePolygonFromArrayCoordinates(coordinatesArray);
            allPolygonPoints.add(polygonCoordinates);
        }
    }

    /**
     * Method to obtain a single polygon from a json array with its coordinates
     * @param coordinatesArray Is array with all polygon points
     * @return polygon coordinates
     */
    private List<Point> fillOnePolygonFromArrayCoordinates(JsonArray coordinatesArray){
        List<Point> polygon = new ArrayList<>();
        coordinatesArray.forEach(point -> {
            Point singlePolygonPoint = Point.fromLngLat(point.getAsJsonArray().get(0).getAsDouble(), point.getAsJsonArray().get(1).getAsDouble());
            polygon.add(singlePolygonPoint);
        });
        return polygon;
    }

    /**
     * Method to return the final result to requesting from database
     * @return All polygons with its coordinates located in database
     */
    public List<List<Point>> getAllPolygonPoints() {return allPolygonPoints;}
}
