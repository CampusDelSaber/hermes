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

/**
 * This activity is used for handling the search view operation.
 * It implements the WayPointClickListener interface to listen for click events on the items in the RecyclerView.
 */
public class SearchViewActivity extends AppCompatActivity implements WayPointClickListener {

    private RecyclerView recyclerView;
    private SearcherAdapter adapter;
    private Searcher searcher;
    private SearchView searchView;
    private SearcherController searcherController;
    private SearcherAdapterUpdater adapterUpdater;

    /**
     * This method initializes the activity.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        initRecyclerView();
        setupSearchView();
    }

    /**
     * This method initializes the RecyclerView and related classes.
     */
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

    /**
     * This method sets up the SearchView and related classes.
     */
    private void setupSearchView() {
        searcherController = new SearcherController(searcher, adapterUpdater);
        searchView.setOnQueryTextListener(searcherController.getOnQueryTextListener());

        searchView.requestFocus();
    }

    /**
     * This method is called when the activity is about to be destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        searcherController.shutdown();
        finish();
    }

    /**
     * This method is called when an item in the RecyclerView is clicked.
     * @param wayPoint The item that was clicked.
     */
    @Override
    public void onItemClick(WayPoint wayPoint) {
        // Add listener
    }
}