package com.isc.hermes.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isc.hermes.R;
import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewIncidentsController extends Fragment {
    private LinearLayout incidentsVisualizationForm;
    private ImageButton viewIncidentsFormButton;
    private Button viewTrafficButton;
    private Button viewSocialEventButton;
    private Button viewNaturalDisasterButton;
    private OkHttpClient client;
    private boolean buttonMarked;
    List<List<Point>> allPolygons = new ArrayList<>();
    private Context context;

    public ViewIncidentsController(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        initComponents();
    }

    public void initComponents() {
        this.incidentsVisualizationForm = ((AppCompatActivity) context).findViewById(R.id.viewIncidentsForm);
        this.viewIncidentsFormButton = ((AppCompatActivity) context).findViewById(R.id.viewIncidentsButton);
        this.viewTrafficButton = ((AppCompatActivity) context).findViewById(R.id.viewTrafficButton);
        this.viewSocialEventButton = ((AppCompatActivity) context).findViewById(R.id.viewSocialIncidentsButton);
        this.viewNaturalDisasterButton = ((AppCompatActivity) context).findViewById(R.id.viewNaturalDisasterButton);
        this.client = new OkHttpClient();
        initEyeButtonFunctionality();
        initViewDifferentIncidentsTypeButton();
    }

    private void initEyeButtonFunctionality() {
        viewIncidentsFormButton.setOnClickListener(v -> {
            if (!buttonMarked) incidentsVisualizationForm.setVisibility(View.VISIBLE);
            else incidentsVisualizationForm.setVisibility(View.GONE);
            buttonMarked = !buttonMarked;
        });
    }

    private void initViewDifferentIncidentsTypeButton() {

        viewTrafficButton.setOnClickListener(v -> {

        });

        viewSocialEventButton.setOnClickListener(v -> {

        });

        viewNaturalDisasterButton.setOnClickListener(v -> {
            String url = "https://api-rest-hermes.onrender.com/incidents?types=Danger%20Zone,Natural%20Disaster";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
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

                    for (JsonElement jsonElement : jsonArray) {
                        List<Point> polygonArray = new ArrayList<>();
                        JsonObject incidentObject = jsonElement.getAsJsonObject();
                        String id = incidentObject.get("_id").getAsString();
                        String type = incidentObject.get("type").getAsString();
                        String reason = incidentObject.get("reason").getAsString();
                        String dateCreated = incidentObject.get("dateCreated").getAsString();
                        String deathDate = incidentObject.get("deathDate").getAsString();
                        JsonObject geometry = incidentObject.get("geometry").getAsJsonObject();
//                        JsonObject geometry = jsonElement.getAsJsonObject().get("geometry").getAsJsonObject();
                        String geometryType = geometry.get("type").getAsString();
                        JsonArray coordinatesArray = geometry.get("coordinates").getAsJsonArray();

                        coordinatesArray.forEach(point -> {
                            Log.i("incidents", String.valueOf(point));
                           /* Double[] coordinate = new Double[2];
                            coordinate[0] = point.getAsJsonArray().get(0).getAsDouble();
                            coordinate[1] = point.getAsJsonArray().get(1).getAsDouble();
                            polygonArray.add(Point.fromLngLat(coordinate[0], coordinate[1]));*/
                        });
                        allPolygons.add(polygonArray);
                    }
                    Log.i("incidents", String.valueOf(allPolygons));
                }
            });

            allPolygons.clear();
        });
    }
}
