package com.isc.hermes.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.utils.Animations;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class to configure the event of do click on a map
 */
public class MapController implements MapboxMap.OnMapClickListener {
    private final MapboxMap mapboxMap;
    private final WaypointOptionsController waypointOptionsController;
    private boolean isMarked;
    private int markerCount = 0;
    private final Context context;

    private LatLng previousPoint;
    private Polyline drawnLine;

    /**
     * This is the constructor method.
     *
     * @param mapboxMap Is the map.
     * @param context   Is the context application.
     */
    public MapController(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        waypointOptionsController = new WaypointOptionsController(context, this);
        mapboxMap.addOnMapClickListener(this);
        isMarked = false;
        Animations.loadAnimations();
    }

    /**
     * Method to add markers to map variable
     *
     * @param point The projected map coordinate the user long clicked on.
     * @return true
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        doMarkOnMapAction(point);
        return true;
    }

    /**
     * Method to perform action to click on map
     *
     * @param point Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction(LatLng point) {
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (isMarked) {
            deleteMarks();
            if (waypointOptionsController.getWaypointOptions().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getWaypointOptions().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getWaypointOptions().setVisibility(View.GONE);
            }
            if (waypointOptionsController.getIncidentFormController().getIncidentForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getIncidentFormController().getIncidentForm().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getIncidentFormController().getIncidentForm().setVisibility(View.GONE);
            }
            if (waypointOptionsController.getTrafficFormController().getTrafficForm().getVisibility() == View.VISIBLE) {
                waypointOptionsController.getTrafficFormController().getTrafficForm().startAnimation(Animations.exitAnimation);
                waypointOptionsController.getTrafficFormController().getTrafficForm().setVisibility(View.GONE);
                doMarkOnMapAction2(point);
            }
            isMarked = false;
        } else {
            MarkerOptions markerOptions = new MarkerOptions().position(point);
            mapboxMap.addMarker(markerOptions);
            waypointOptionsController.getWaypointOptions().startAnimation(Animations.entryAnimation);
            waypointOptionsController.getWaypointOptions().setVisibility(View.VISIBLE);
            isMarked = true;

        }
    }

    /**
     * Method to perform action to click on map and
     * will help to control that the point is placed at a point
     * and from that point create the traffic line that is there.
     *
     * @param point2 Is point passed as parameter with its latitude and longitude
     */
    private void doMarkOnMapAction2(LatLng point2) {
        PointF screenPoint2 = mapboxMap.getProjection().toScreenLocation(point2);
        List<Feature> features2 = mapboxMap.queryRenderedFeatures(screenPoint2);
        if (previousPoint != null && (features2.get(0).geometry().type().equals("MultiLineString") || features2.get(0).geometry().type().equals("LineString"))) {
            Geometry targetGeometry = features2.get(0).geometry();
            List<LatLng> points = findPathBetweenPoints(previousPoint, point2, targetGeometry);
            if (points != null) {
                if (drawnLine != null) {
                    mapboxMap.removePolyline(drawnLine);
                }
                drawnLine = mapboxMap.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .color(Color.RED)
                        .width(5f));
            } else {
                Toast.makeText(context, "No valid path found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Touch on street or avenue", Toast.LENGTH_SHORT).show();
        }

        previousPoint = point2;
    }
    private List<LatLng> findPathBetweenPoints(LatLng startPoint, LatLng endPoint, Geometry targetGeometry) {
        List<LatLng> pathPoints = new ArrayList<>();
        pathPoints.add(startPoint);
        while (true) {
            LatLng lastPoint = pathPoints.get(pathPoints.size() - 1);
            LatLng nextPoint = findNextPoint(lastPoint, endPoint, targetGeometry);
            if (nextPoint != null) {
                pathPoints.add(nextPoint);
                if (nextPoint.equals(endPoint)) {
                    break;
                }
            } else {
                return null; // No valid path found
            }
        }
        return pathPoints;
    }

    private LatLng findNextPoint(LatLng startPoint, LatLng endPoint, Geometry targetGeometry) {
        if (targetGeometry instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) targetGeometry;
            List<LineString> lineStrings = multiLineString.lineStrings();
            Graph graph = buildGraphFromLineStrings(lineStrings);
            return findShortestPath(startPoint, endPoint, graph);
        } else if (targetGeometry instanceof LineString) {
            LineString lineString = (LineString) targetGeometry;
            List<LatLng> linePoints = convertCoordinatesToLatLng(lineString.coordinates());
            Graph graph = buildGraphFromLinePoints(linePoints);
            return findShortestPath(startPoint, endPoint, graph);
        }

        return null;
    }

    private Graph buildGraphFromLineStrings(List<LineString> lineStrings) {
        Graph graph = new Graph();

        for (LineString lineString : lineStrings) {
            List<LatLng> linePoints = convertCoordinatesToLatLng(lineString.coordinates());
            for (int i = 0; i < linePoints.size() - 1; i++) {
                LatLng startPoint = linePoints.get(i);
                LatLng endPoint = linePoints.get(i + 1);
                graph.addEdge(startPoint, endPoint);
            }
        }

        return graph;
    }

    private Graph buildGraphFromLinePoints(List<LatLng> linePoints) {
        Graph graph = new Graph();

        for (int i = 0; i < linePoints.size() - 1; i++) {
            LatLng startPoint = linePoints.get(i);
            LatLng endPoint = linePoints.get(i + 1);
            graph.addEdge(startPoint, endPoint);
        }

        return graph;
    }

    private List<LatLng> convertCoordinatesToLatLng(List<Point> coordinates) {
        List<LatLng> latLngs = new ArrayList<>();
        for (Point point : coordinates) {
            latLngs.add(new LatLng(point.latitude(), point.longitude()));
        }
        return latLngs;
    }

    private LatLng findShortestPath(LatLng startPoint, LatLng endPoint, Graph graph) {
        PriorityQueue<GraphNode> queue = new PriorityQueue<>();
        Map<LatLng, Double> distanceMap = new HashMap<>();
        Map<LatLng, LatLng> parentMap = new HashMap<>();

        queue.add(new GraphNode(startPoint, 0.0));
        distanceMap.put(startPoint, 0.0);

        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            LatLng currentPoint = current.getPoint();

            if (currentPoint.equals(endPoint)) {
                return currentPoint; // Found the shortest path
            }

            if (current.getDistance() > distanceMap.get(currentPoint)) {
                continue; // Skip outdated nodes
            }

            List<LatLng> neighbors = graph.getNeighbors(currentPoint);
            for (LatLng neighbor : neighbors) {
                double distance = current.getDistance() + currentPoint.distanceTo(neighbor);

                if (!distanceMap.containsKey(neighbor) || distance < distanceMap.get(neighbor)) {
                    distanceMap.put(neighbor, distance);
                    parentMap.put(neighbor, currentPoint);
                    queue.add(new GraphNode(neighbor, distance));
                }
            }
        }

        return null; // No path found
    }

    class GraphNode implements Comparable<GraphNode> {
        private LatLng point;
        private double distance;

        public GraphNode(LatLng point, double distance) {
            this.point = point;
            this.distance = distance;
        }

        public LatLng getPoint() {
            return point;
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(GraphNode other) {
            return Double.compare(distance, other.distance);
        }
    }

    class Graph {
        private Map<LatLng, List<LatLng>> adjacencyMap;

        public Graph() {
            adjacencyMap = new HashMap<>();
        }

        public void addEdge(LatLng start, LatLng end) {
            adjacencyMap.putIfAbsent(start, new ArrayList<>());
            adjacencyMap.putIfAbsent(end, new ArrayList<>());
            adjacencyMap.get(start).add(end);
            adjacencyMap.get(end).add(start);
        }

        public List<LatLng> getNeighbors(LatLng point) {
            return adjacencyMap.getOrDefault(point, new ArrayList<>());
        }
    }


    /**
     * Method to delete all the marks in the map.
     */
    public void deleteMarks() {
        for (Marker marker : mapboxMap.getMarkers()) {
            mapboxMap.removeMarker(marker);
        }
    }

    /**
     * This is a setter method to isMarked attribute.
     *
     * @param marked Is the new value to isMarked.
     */
    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
