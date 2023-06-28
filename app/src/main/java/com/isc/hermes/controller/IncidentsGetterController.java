package com.isc.hermes.controller;

import android.content.Context;
import android.widget.Toast;

import com.isc.hermes.R;
import com.isc.hermes.controller.incidents.IncidentPointVisualizationController;
import com.isc.hermes.database.IncidentsDataProcessor;
import com.isc.hermes.model.incidents.PointIncident;
import com.isc.hermes.model.incidents.IncidentGetterModel;
import com.isc.hermes.utils.ISO8601Converter;
import com.isc.hermes.utils.MapManager;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The controller responsible for getting incidentsPointView near a specific location within a radius.
 */
public class IncidentsGetterController {
    private final ISO8601Converter iso8601Converter;
    private final IncidentsDataProcessor incidentsDataProcessor;
    private static IncidentsGetterController instance;
    private IncidentGetterModel incidentGetterModel;
    private final ExecutorService executorService;
    private IncidentPointVisualizationController incidentsPointView;
    private MapboxMap mapboxMap;

    /**
     * Constructs an instance of IncidentsGetterController.
     */
    public IncidentsGetterController() {
        this.iso8601Converter = ISO8601Converter.getInstance();
        this.incidentsDataProcessor = IncidentsDataProcessor.getInstance();
        this.incidentGetterModel = new IncidentGetterModel();
        this.executorService = Executors.newSingleThreadExecutor();
        mapboxMap = MapManager.getInstance().getMapboxMap();
    }

    /**
     * Retrieves incidentsPointView near the specified location within a radius.
     *
     */
    public void getNearIncidentsWithinRadius(Context context) {
        incidentsPointView = IncidentPointVisualizationController.getInstance(mapboxMap, context);
        //mapboxMap.addOnCameraIdleListener(() -> loadNearIncidents(mapboxMap, context));
        loadNearIncidents(context);
    }

    private void loadNearIncidents(Context context){
        executorService.execute(() -> {
            LatLng cameraFocus = mapboxMap.getCameraPosition().target;
            int cameraZoom = (int) mapboxMap.getCameraPosition().zoom;
            int zoom = cameraZoom > 40 ? 40 : Math.max(cameraZoom, 12);
            JSONArray incidentsArray = incidentsDataProcessor.getNearIncidents(cameraFocus, zoom);

            incidentGetterModel.setIncidentList(parseIncidentResponse(incidentsArray));
            try {
                incidentsPointView.displayPoint(incidentGetterModel.getIncidentList());
            } catch (JSONException e) {
                Toast.makeText(
                        context, R.string.incidents_fail_to_load,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    /**
     * Parses the JSON array of incidentsPointView and converts them into a list of PointIncidet objects.
     *
     * @param incidentsArray The JSON array of incidentsPointView.
     * @return The list of PointIncidet objects.
     */
    private List<PointIncident> parseIncidentResponse(JSONArray incidentsArray) {
        List<PointIncident> pointIncidents = new ArrayList<>();
        try{
            for (int i = 0; i < incidentsArray.length(); i++) {
                JSONObject incidentObj = incidentsArray.getJSONObject(i);
                String id = incidentObj.getString("_id");
                String type = incidentObj.getString("type");
                String reason = incidentObj.getString("reason");
                Date dateCreated =
                        iso8601Converter.convertISO8601ToDate(incidentObj.getString("dateCreated"));
                Date deathDate =
                        iso8601Converter.convertISO8601ToDate(incidentObj.getString("deathDate"));
                JSONObject geometry = incidentObj.getJSONObject("geometry");
                if (geometry.getString("type").equalsIgnoreCase("point")) {
                    PointIncident pointIncident =
                            new PointIncident(id, type, reason, dateCreated, deathDate, geometry);
                    pointIncidents.add(pointIncident);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return pointIncidents;
    }
    public IncidentGetterModel getIncidentGetterModel(){
        return incidentGetterModel;
    }

    /**
     * Retrieves the singleton instance of IncidentsGetterController.
     *
     * @return The singleton instance.
     */
    public static IncidentsGetterController getInstance(){
        if (instance == null) instance = new IncidentsGetterController();
        return instance;
    }
}
