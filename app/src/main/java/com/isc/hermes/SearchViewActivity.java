package com.isc.hermes;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.controller.SearcherController;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.SearcherAdapter;
import com.isc.hermes.utils.SearcherAdapterUpdater;
import com.isc.hermes.utils.WayPointClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity implements WayPointClickListener {
    private RecyclerView recyclerView;
    private SearcherAdapter adapter;
    private Searcher searcher;
    private SearchView searchView;
    private SearcherController searcherController;
    private SearcherAdapterUpdater adapterUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        initRecyclerView();
        setupSearchView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycle_view_searched);
        searchView = findViewById(R.id.searcher_view);

        searcher = new Searcher();
        List<WayPoint> wayPoints = new ArrayList<>();
        adapter = new SearcherAdapter(wayPoints, this);
        adapterUpdater = new SearcherAdapterUpdater(this, adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSearchView() {
        searcherController = new SearcherController(searcher, adapterUpdater);
        searchView.setOnQueryTextListener(searcherController.getOnQueryTextListener());

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
        // Add listener
    }
}