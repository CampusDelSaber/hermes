package com.isc.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.controller.SearcherController;
import com.isc.hermes.model.MapboxEventManager;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.PlacesAdapter;
import com.isc.hermes.utils.WayPointClickListener;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity implements WayPointClickListener {

    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private Searcher searcher;
    private SearchView searchView;
    private SearcherController searcherController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        recyclerView = findViewById(R.id.recycle_view_searched);
        searchView = findViewById(R.id.searcher_view);

        searcher = new Searcher();
        List<WayPoint> wayPoints = new ArrayList<>();
        adapter = new PlacesAdapter(wayPoints, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searcherController = new SearcherController(searcher, adapter);
        searchView.setOnQueryTextListener(searcherController.getOnQueryTextListener(this));

        searchView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searcherController.shutdown();
        finish();
    }

    @Override
    public void onItemClick(WayPoint wayPoint) {
        Toast.makeText(this, wayPoint.getPlaceName(), Toast.LENGTH_SHORT).show();
        LatLng selectedLocation = new LatLng(wayPoint.getLatitude(), wayPoint.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions()
                .position(selectedLocation)
                .title(wayPoint.getPlaceName());



        MapboxEventManager mapboxEventManager = MapboxEventManager.getInstance();
        if(mapboxEventManager.addMarker(markerOptions)){
            Toast.makeText(this, wayPoint.getPlaceName(), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

}
