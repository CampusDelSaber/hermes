package com.isc.hermes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
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
    private ImageView imageView;

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
        goBackMainPage();
    }

    /**
     * This method initializes the RecyclerView and related classes.
     */
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycle_view_searched);
        searchView = findViewById(R.id.searcher_view);
        imageView = findViewById(R.id.back_arrow_main);

        searcher = new Searcher(this.getApplicationContext());
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
        Toast.makeText(this, wayPoint.getPlaceName(), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("com.isc.hermes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("placeName", wayPoint.getPlaceName());
        editor.putFloat("latitude", (float)wayPoint.getLatitude());
        editor.putFloat("longitude", (float)wayPoint.getLongitude());
        editor.apply();
        finish();
    }

    /**
     * This method is called when the back arrow is clicked.
     */
    private void goBackMainPage() {
        imageView.setOnClickListener(v -> {
            searcherController.shutdown();
            finish();
        });
    }
}