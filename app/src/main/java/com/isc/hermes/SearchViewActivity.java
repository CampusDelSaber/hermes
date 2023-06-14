package com.isc.hermes;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.model.Place;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.PlacesAdapter;
import com.isc.hermes.utils.WayPointClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity implements WayPointClickListener {
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private Searcher searcher;
    private SearchView searchView;

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.clearWayPoints();
                List<WayPoint> wayPoints = searcher.getSearcherSuggestionsPlacesInfo(newText);
                for (WayPoint wayPoint : wayPoints) {
                    adapter.addWayPoint(wayPoint);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(WayPoint wayPoint) {
        // Add listener
    }
}
